package com.project.Plant.ItemManagement.DTO;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import com.project.Plant.ItemManagement.Entity.Item;

@Getter
@Setter
@AllArgsConstructor
public class ItemAuthResponse {
    private boolean success;
    private String message;
    private Item item;
}

