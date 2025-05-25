package com.project.Plant.ItemManagement.DTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ItemDTO {
    private Long itemId;
    private byte[] image;
    private String name;
    private String description;
    private Integer quantity;
    private Double price;
}
