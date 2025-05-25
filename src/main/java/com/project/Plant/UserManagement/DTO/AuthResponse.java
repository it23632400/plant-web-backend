package com.project.Plant.UserManagement.DTO;

import com.project.Plant.UserManagement.Entity.UserEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class AuthResponse {
    private boolean success;
    private String message;
    private UserEntity user;

}

