package com.project.Plant.ItemManagement.DTO;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ResponseDTO {
    private String status;
    private String message;
    private Object data;
}