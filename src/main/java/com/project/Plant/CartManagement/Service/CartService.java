package com.project.Plant.CartManagement.Service;

import com.project.Plant.CartManagement.Dto.CartDto;
import com.project.Plant.CartManagement.Entity.Cart;
import com.project.Plant.CartManagement.Repository.CartRepository;
import com.project.Plant.ItemManagement.Entity.Item;
import com.project.Plant.ItemManagement.Repository.ItemRepository;
import com.project.Plant.UserManagement.Entity.UserEntity;
import com.project.Plant.UserManagement.Repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CartService {

    private final CartRepository cartRepository;
    private final UserRepository userRepository;
    private final ItemRepository itemRepository;

    @Transactional
    public CartDto.CartResponse addToCart(Long userId, CartDto.CartRequest request) {
        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));

        Item item = itemRepository.findById(request.getItemId())
                .orElseThrow(() -> new EntityNotFoundException("Item not found"));

        // Check if item is already in cart
        Cart cart = cartRepository.findByUserAndItem(user, item)
                .orElse(new Cart());

        if (cart.getId() == null) {
            cart.setUser(user);
            cart.setItem(item);
            cart.setQuantity(request.getQuantity());
        } else {
            cart.setQuantity(cart.getQuantity() + request.getQuantity());
        }

        // Calculate total price
        cart.setTotalPrice(item.getPrice() * cart.getQuantity());

        // Save cart
        Cart savedCart = cartRepository.save(cart);

        // Map to response
        return mapToCartResponse(savedCart);
    }

    @Transactional(readOnly = true)
    public List<CartDto.CartResponse> getUserCart(Long userId) {
        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));

        List<Cart> cartItems = cartRepository.findByUser(user);

        return cartItems.stream()
                .map(this::mapToCartResponse)
                .collect(Collectors.toList());
    }

    @Transactional
    public CartDto.CartResponse updateCartItem(Long userId, CartDto.CartUpdateRequest request) {
        Cart cart = cartRepository.findById(request.getCartId())
                .orElseThrow(() -> new EntityNotFoundException("Cart item not found"));

        // Verify the cart item belongs to the user
        if (!cart.getUser().getId().equals(userId)) {
            throw new IllegalStateException("Cart item does not belong to the user");
        }

        cart.setQuantity(request.getQuantity());
        cart.setTotalPrice(cart.getItem().getPrice() * request.getQuantity());

        Cart updatedCart = cartRepository.save(cart);

        return mapToCartResponse(updatedCart);
    }

    @Transactional
    public void removeCartItem(Long userId, Long cartId) {
        Cart cart = cartRepository.findById(cartId)
                .orElseThrow(() -> new EntityNotFoundException("Cart item not found"));

        // Verify the cart item belongs to the user
        if (!cart.getUser().getId().equals(userId)) {
            throw new IllegalStateException("Cart item does not belong to the user");
        }

        cartRepository.delete(cart);
    }

    private CartDto.CartResponse mapToCartResponse(Cart cart) {
        return CartDto.CartResponse.builder()
                .id(cart.getId())
                .userId(cart.getUser().getId())
                .itemId(cart.getItem().getItemId())
                .itemName(cart.getItem().getName())
                .quantity(cart.getQuantity())
                .price(cart.getItem().getPrice())
                .totalPrice(cart.getTotalPrice())
                .build();
    }
}