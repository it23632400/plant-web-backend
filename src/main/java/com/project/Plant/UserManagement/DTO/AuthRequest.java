package com.project.Plant.UserManagement.DTO;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AuthRequest {
    private String email;
    private String password;
    private String contactNo;
    private String address;
}

