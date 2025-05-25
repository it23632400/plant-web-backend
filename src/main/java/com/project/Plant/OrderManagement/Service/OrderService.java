package com.project.Plant.OrderManagement.Service;

import com.project.Plant.CartManagement.Repository.CartRepository;
import com.project.Plant.ItemManagement.Entity.Item;
import com.project.Plant.ItemManagement.Repository.ItemRepository;
import com.project.Plant.OrderManagement.Dto.OrderDto;
import com.project.Plant.OrderManagement.Entity.Order;
import com.project.Plant.OrderManagement.Entity.OrderItem;
import com.project.Plant.OrderManagement.Repository.OrderItemRepository;
import com.project.Plant.OrderManagement.Repository.OrderRepository;
import com.project.Plant.UserManagement.Entity.UserEntity;
import com.project.Plant.UserManagement.Repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final UserRepository userRepository;
    private final ItemRepository itemRepository;
    private final CartRepository cartRepository;

    @Transactional
    public OrderDto.OrderResponse createOrder(OrderDto.OrderRequest orderRequest) {
        // Get user
        UserEntity user = userRepository.findById(orderRequest.getUserId())
                .orElseThrow(() -> new EntityNotFoundException("User not found"));

        // Create order
        Order order = Order.builder()
                .user(user)
                .firstName(orderRequest.getFirstName())
                .lastName(orderRequest.getLastName())
                .email(orderRequest.getEmail())
                .street(orderRequest.getStreet())
                .city(orderRequest.getCity())
                .state(orderRequest.getState())
                .zipCode(orderRequest.getZipCode())
                .contactNo(orderRequest.getContactNo())
                .paymentMethod(orderRequest.getPaymentMethod())
                .itemsTotal(orderRequest.getItemsTotal())
                .shippingCharges(orderRequest.getShippingCharges())
                .orderTotal(orderRequest.getOrderTotal())
                .status(Order.OrderStatus.PENDING)
                .build();

        Order savedOrder = orderRepository.save(order);

        // Create order items
        List<OrderItem> orderItems = new ArrayList<>();
        for (OrderDto.OrderItemRequest itemRequest : orderRequest.getItems()) {
            Item item = itemRepository.findById(itemRequest.getItemId())
                    .orElseThrow(() -> new EntityNotFoundException("Item not found"));

            // Update item quantity in inventory
            if (item.getQuantity() < itemRequest.getQuantity()) {
                throw new IllegalStateException("Not enough inventory for item: " + item.getName());
            }

            item.setQuantity(item.getQuantity() - itemRequest.getQuantity());
            itemRepository.save(item);

            OrderItem orderItem = OrderItem.builder()
                    .order(savedOrder)
                    .item(item)
                    .quantity(itemRequest.getQuantity())
                    .price(itemRequest.getPrice())
                    .totalPrice(itemRequest.getTotalPrice())
                    .build();

            orderItems.add(orderItem);
        }

        List<OrderItem> savedOrderItems = orderItemRepository.saveAll(orderItems);
        savedOrder.setOrderItems(savedOrderItems);

        // Clear the user's cart after successful order creation
        cartRepository.deleteByUser(user);

        return mapToOrderResponse(savedOrder);
    }

    public List<OrderDto.OrderResponse> getUserOrders(Long userId) {
        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));

        List<Order> orders = orderRepository.findByUser(user);

        return orders.stream()
                .map(this::mapToOrderResponse)
                .collect(Collectors.toList());
    }

    public List<OrderDto.AdminOrderResponse> getAllOrders() {
        List<Order> orders = orderRepository.findAllByOrderByOrderDateDesc();

        return orders.stream()
                .map(this::mapToAdminOrderResponse)
                .collect(Collectors.toList());
    }

    public OrderDto.AdminOrderResponse getOrderById(Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new EntityNotFoundException("Order not found"));

        return mapToAdminOrderResponse(order);
    }

    @Transactional
    public OrderDto.AdminOrderResponse updateOrderStatus(Long orderId, String status) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new EntityNotFoundException("Order not found"));

        order.setStatus(Order.OrderStatus.valueOf(status));
        Order updatedOrder = orderRepository.save(order);

        return mapToAdminOrderResponse(updatedOrder);
    }

    @Transactional
    public void deleteOrder(Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new EntityNotFoundException("Order not found with id: " + orderId));

        // Check if order can be deleted (only PENDING and CANCELLED orders)
        if (order.getStatus() != Order.OrderStatus.PENDING &&
                order.getStatus() != Order.OrderStatus.CANCELLED) {
            throw new IllegalStateException("Cannot delete order with status: " + order.getStatus() +
                    ". Only PENDING and CANCELLED orders can be deleted.");
        }

        // If order is PENDING, restore inventory quantities
        if (order.getStatus() == Order.OrderStatus.PENDING) {
            for (OrderItem orderItem : order.getOrderItems()) {
                Item item = orderItem.getItem();
                item.setQuantity(item.getQuantity() + orderItem.getQuantity());
                itemRepository.save(item);
            }
        }

        // Delete order items first (due to foreign key constraints)
        orderItemRepository.deleteAll(order.getOrderItems());

        // Delete the order
        orderRepository.delete(order);
    }

    @Transactional
    public void deleteUserOrder(Long orderId, Long userId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new EntityNotFoundException("Order not found with id: " + orderId));

        // Verify that the order belongs to the user
        if (!order.getUser().getId().equals(userId)) {
            throw new IllegalStateException("You are not authorized to delete this order");
        }

        // Check if order can be deleted (only PENDING and CANCELLED orders)
        if (order.getStatus() != Order.OrderStatus.PENDING &&
                order.getStatus() != Order.OrderStatus.CANCELLED) {
            throw new IllegalStateException("Cannot delete order with status: " + order.getStatus() +
                    ". Only PENDING and CANCELLED orders can be deleted.");
        }

        // If order is PENDING, restore inventory quantities
        if (order.getStatus() == Order.OrderStatus.PENDING) {
            for (OrderItem orderItem : order.getOrderItems()) {
                Item item = orderItem.getItem();
                item.setQuantity(item.getQuantity() + orderItem.getQuantity());
                itemRepository.save(item);
            }
        }

        // Delete order items first (due to foreign key constraints)
        orderItemRepository.deleteAll(order.getOrderItems());

        // Delete the order
        orderRepository.delete(order);
    }

    private OrderDto.OrderResponse mapToOrderResponse(Order order) {
        List<OrderDto.OrderItemResponse> itemResponses = order.getOrderItems().stream()
                .map(item -> OrderDto.OrderItemResponse.builder()
                        .id(item.getId())
                        .itemId(item.getItem().getItemId())
                        .itemName(item.getItem().getName())
                        .quantity(item.getQuantity())
                        .price(item.getPrice())
                        .totalPrice(item.getTotalPrice())
                        .build())
                .collect(Collectors.toList());

        return OrderDto.OrderResponse.builder()
                .id(order.getId())
                .userId(order.getUser().getId())
                .firstName(order.getFirstName())
                .lastName(order.getLastName())
                .email(order.getEmail())
                .street(order.getStreet())
                .city(order.getCity())
                .state(order.getState())
                .zipCode(order.getZipCode())
                .contactNo(order.getContactNo())
                .paymentMethod(order.getPaymentMethod())
                .itemsTotal(order.getItemsTotal())
                .shippingCharges(order.getShippingCharges())
                .orderTotal(order.getOrderTotal())
                .status(order.getStatus().name())
                .orderDate(order.getOrderDate())
                .items(itemResponses)
                .build();
    }

    private OrderDto.AdminOrderResponse mapToAdminOrderResponse(Order order) {
        List<OrderDto.OrderItemResponse> itemResponses = order.getOrderItems().stream()
                .map(item -> OrderDto.OrderItemResponse.builder()
                        .id(item.getId())
                        .itemId(item.getItem().getItemId())
                        .itemName(item.getItem().getName())
                        .quantity(item.getQuantity())
                        .price(item.getPrice())
                        .totalPrice(item.getTotalPrice())
                        .build())
                .collect(Collectors.toList());

        return OrderDto.AdminOrderResponse.builder()
                .id(order.getId())
                .userId(order.getUser().getId())
                .userEmail(order.getUser().getEmail())
                .firstName(order.getFirstName())
                .lastName(order.getLastName())
                .email(order.getEmail())
                .street(order.getStreet())
                .city(order.getCity())
                .state(order.getState())
                .zipCode(order.getZipCode())
                .contactNo(order.getContactNo())
                .paymentMethod(order.getPaymentMethod())
                .itemsTotal(order.getItemsTotal())
                .shippingCharges(order.getShippingCharges())
                .orderTotal(order.getOrderTotal())
                .status(order.getStatus().name())
                .orderDate(order.getOrderDate())
                .items(itemResponses)
                .build();
    }
}