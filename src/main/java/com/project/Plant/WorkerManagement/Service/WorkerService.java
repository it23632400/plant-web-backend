package com.project.Plant.WorkerManagement.Service;

import com.project.Plant.WorkerManagement.DTO.WorkerAuthRequest;
import com.project.Plant.WorkerManagement.DTO.WorkerAuthResponse;
import com.project.Plant.WorkerManagement.Entity.WorkerEntity;
import com.project.Plant.WorkerManagement.Repository.WorkerRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class WorkerService {
    private static final Logger logger = LoggerFactory.getLogger(WorkerService.class);

    private final WorkerRepository workerRepository;
    private final PasswordEncoder passwordEncoder;

    public WorkerService(WorkerRepository workerRepository, PasswordEncoder passwordEncoder) {
        this.workerRepository = workerRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public WorkerAuthResponse registerWorker(WorkerAuthRequest request) {
        try {
            logger.info("Registering worker with email: {}", request.getEmail());

            if (workerRepository.findByEmail(request.getEmail()).isPresent()) {
                logger.warn("Email already registered: {}", request.getEmail());
                return new WorkerAuthResponse(false, "Email is already registered", null);
            }

            WorkerEntity worker = new WorkerEntity();
            worker.setEmail(request.getEmail());
            worker.setPassword(passwordEncoder.encode(request.getPassword()));

            try {
                worker.setContactNo(Long.parseLong(request.getContactNo()));
            } catch (NumberFormatException e) {
                logger.error("Invalid contact number format: {}", request.getContactNo());
                return new WorkerAuthResponse(false, "Invalid contact number format", null);
            }

            worker.setAddress(request.getAddress());
            workerRepository.save(worker);

            logger.info("Worker registered successfully: {}", request.getEmail());
            return new WorkerAuthResponse(true, "Registration successful", worker);
        } catch (Exception e) {
            logger.error("Error during worker registration", e);
            return new WorkerAuthResponse(false, "Error during registration: " + e.getMessage(), null);
        }
    }

    public WorkerAuthResponse loginWorker(WorkerAuthRequest request) {
        try {
            logger.info("Login attempt for worker email: {}", request.getEmail());

            Optional<WorkerEntity> workerOpt = workerRepository.findByEmail(request.getEmail());
            if (workerOpt.isPresent()) {
                WorkerEntity worker = workerOpt.get();

                if (passwordEncoder.matches(request.getPassword(), worker.getPassword())) {
                    logger.info("Worker logged in successfully: {}", request.getEmail());
                    return new WorkerAuthResponse(true, "Login successful", worker);
                }
            }

            logger.warn("Login failed for worker email: {}", request.getEmail());
            return new WorkerAuthResponse(false, "Invalid email or password", null);
        } catch (Exception e) {
            logger.error("Error during worker login", e);
            return new WorkerAuthResponse(false, "Login error: " + e.getMessage(), null);
        }
    }

    public List<WorkerEntity> getAllWorkers() {
        return workerRepository.findAll();
    }

    public boolean deleteWorker(Long id) {
        Optional<WorkerEntity> worker = workerRepository.findById(id);
        if (worker.isPresent()) {
            workerRepository.deleteById(id);
            return true;
        }
        return false;
    }
}
