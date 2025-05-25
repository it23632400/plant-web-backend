package com.project.Plant.ItemManagement.DTO;

import jakarta.persistence.Column;
import jakarta.persistence.Lob;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ItemAuthRequest {
    private String name;
    private String description;
    private Integer quantity;
    private Double price;
    private String category;
}

