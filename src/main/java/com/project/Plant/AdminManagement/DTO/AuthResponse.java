package com.project.Plant.AdminManagement.DTO;

import com.project.Plant.AdminManagement.Entity.AdminEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class AuthResponse {
    private boolean success;
    private String message;
    private AdminEntity admin;
}
