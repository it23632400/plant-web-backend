package com.project.Plant.WishlistManagement.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

public class WishlistDTO {

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class WishlistRequest {
        private Long userId;
        private Long itemId;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class WishlistResponse {
        private Long id;
        private Long userId;
        private Long itemId;
        private String name;
        private String description;
        private Double price;
        private String category;
        private byte[] image;
    }
}