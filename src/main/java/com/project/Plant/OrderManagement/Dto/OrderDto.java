package com.project.Plant.OrderManagement.Dto;

import com.project.Plant.OrderManagement.Entity.Order;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

public class OrderDto {

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class OrderRequest {
        private Long userId;
        private String firstName;
        private String lastName;
        private String email;
        private String street;
        private String city;
        private String state;
        private String zipCode;
        private String contactNo;
        private String paymentMethod;
        private Double itemsTotal;
        private Double shippingCharges;
        private Double orderTotal;
        private List<OrderItemRequest> items;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class OrderItemRequest {
        private Long itemId;
        private Integer quantity;
        private Double price;
        private Double totalPrice;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class OrderResponse {
        private Long id;
        private Long userId;
        private String firstName;
        private String lastName;
        private String email;
        private String street;
        private String city;
        private String state;
        private String zipCode;
        private String contactNo;
        private String paymentMethod;
        private Double itemsTotal;
        private Double shippingCharges;
        private Double orderTotal;
        private String status;
        private LocalDateTime orderDate;
        private List<OrderItemResponse> items;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class OrderItemResponse {
        private Long id;
        private Long itemId;
        private String itemName;
        private Integer quantity;
        private Double price;
        private Double totalPrice;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class AdminOrderResponse {
        private Long id;
        private Long userId;
        private String userEmail;
        private String firstName;
        private String lastName;
        private String email;
        private String street;
        private String city;
        private String state;
        private String zipCode;
        private String contactNo;
        private String paymentMethod;
        private Double itemsTotal;
        private Double shippingCharges;
        private Double orderTotal;
        private String status;
        private LocalDateTime orderDate;
        private List<OrderItemResponse> items;
    }
}