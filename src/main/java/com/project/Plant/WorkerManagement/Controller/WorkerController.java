package com.project.Plant.WorkerManagement.Controller;

import com.project.Plant.ItemManagement.DTO.ResponseDTO;
import com.project.Plant.WorkerManagement.DTO.WorkerAuthRequest;
import com.project.Plant.WorkerManagement.DTO.WorkerAuthResponse;
import com.project.Plant.WorkerManagement.Entity.WorkerEntity;
import com.project.Plant.WorkerManagement.Service.WorkerService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

@RestController
@RequestMapping("/api/worker")
public class WorkerController {
    private static final Logger logger = LoggerFactory.getLogger(WorkerController.class);

    private final WorkerService workerService;

    public WorkerController(WorkerService workerService) {
        this.workerService = workerService;
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody WorkerAuthRequest request) {
        logger.info("Worker registration request received for email: {}", request.getEmail());

        if (request.getEmail() == null || request.getEmail().isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new WorkerAuthResponse(false, "Email is required", null));
        }

        if (request.getPassword() == null || request.getPassword().isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new WorkerAuthResponse(false, "Password is required", null));
        }

        if (request.getContactNo() == null || request.getContactNo().isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new WorkerAuthResponse(false, "Contact No is required", null));
        }

        if (request.getAddress() == null || request.getAddress().isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new WorkerAuthResponse(false, "Address is required", null));
        }

        WorkerAuthResponse response = workerService.registerWorker(request);

        if (response.isSuccess()) {
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }

    @PostMapping("/login")
    public ResponseEntity<WorkerAuthResponse> login(@RequestBody WorkerAuthRequest request) {
        WorkerAuthResponse response = workerService.loginWorker(request);
        return ResponseEntity.ok(response);
    }

    @GetMapping(value = "get-workers")
    public ResponseEntity<List<WorkerEntity>> getAllEmployees() {
        return ResponseEntity.ok(workerService.getAllWorkers());
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<ResponseDTO> deleteWorker(@PathVariable Long id) {
        try {
            boolean deleted = workerService.deleteWorker(id);
            if (deleted) {
                return ResponseEntity.ok(
                        ResponseDTO.builder()
                                .status("success")
                                .message("Worker deleted successfully")
                                .build()
                );
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                        ResponseDTO.builder()
                                .status("error")
                                .message("Worker not found")
                                .build()
                );
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                    ResponseDTO.builder()
                            .status("error")
                            .message("Error deleting Worker: " + e.getMessage())
                            .build()
            );
        }
    }
}
