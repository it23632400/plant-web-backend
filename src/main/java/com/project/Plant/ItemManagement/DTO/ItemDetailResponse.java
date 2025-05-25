package com.project.Plant.ItemManagement.DTO;
import com.project.Plant.ItemManagement.Entity.Item;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ItemDetailResponse {
    private boolean success;
    private String message;
    private Item item;
}

