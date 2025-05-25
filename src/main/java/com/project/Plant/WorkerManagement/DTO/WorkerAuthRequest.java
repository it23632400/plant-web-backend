package com.project.Plant.WorkerManagement.DTO;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class WorkerAuthRequest {
    private String email;
    private String password;
    private String contactNo;
    private String address;
}
