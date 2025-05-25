package com.project.Plant.WorkerManagement.DTO;

import com.project.Plant.WorkerManagement.Entity.WorkerEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class WorkerAuthResponse {
    private boolean success;
    private String message;
    private WorkerEntity worker;
}
