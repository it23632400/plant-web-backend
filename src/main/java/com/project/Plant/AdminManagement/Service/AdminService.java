package com.project.Plant.AdminManagement.Service;

import com.project.Plant.AdminManagement.DTO.AuthRequest;
import com.project.Plant.AdminManagement.DTO.AuthResponse;
import com.project.Plant.AdminManagement.Entity.AdminEntity;
import com.project.Plant.AdminManagement.Repository.AdminRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AdminService {
    private static final Logger logger = LoggerFactory.getLogger(AdminService.class);

    private final AdminRepository adminRepository;
    private final PasswordEncoder passwordEncoder;

    public AdminService(AdminRepository adminRepository, PasswordEncoder passwordEncoder) {
        this.adminRepository = adminRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public AuthResponse registerAdmin(AuthRequest request) {
        try {
            logger.info("Attempting to register admin with email: {}", request.getEmail());

            if (adminRepository.findByEmail(request.getEmail()).isPresent()) {
                logger.warn("Email already registered: {}", request.getEmail());
                return new AuthResponse(false, "Email is already registered", null);
            }

            AdminEntity admin = new AdminEntity();
            admin.setEmail(request.getEmail());
            admin.setPassword(passwordEncoder.encode(request.getPassword()));
            try {
                admin.setContactNo(Long.parseLong(request.getContactNo()));
            } catch (NumberFormatException e) {
                logger.error("Invalid contact number format: {}", request.getContactNo());
                return new AuthResponse(false, "Invalid contact number format", null);
            }

            admin.setAddress(request.getAddress());
            adminRepository.save(admin);

            logger.info("Admin registered successfully: {}", request.getEmail());
            return new AuthResponse(true, "Registration successful", admin);

        } catch (Exception e) {
            logger.error("Error during admin registration", e);
            return new AuthResponse(false, "Error during registration: " + e.getMessage(), null);
        }
    }

    public AuthResponse loginAdmin(AuthRequest request) {
        try {
            logger.info("Login attempt for admin: {}", request.getEmail());
            Optional<AdminEntity> adminOpt = adminRepository.findByEmail(request.getEmail());

            if (adminOpt.isPresent()) {
                AdminEntity admin = adminOpt.get();
                if (passwordEncoder.matches(request.getPassword(), admin.getPassword())) {
                    logger.info("Admin logged in successfully: {}", request.getEmail());
                    return new AuthResponse(true, "Login successful", admin);
                }
            }

            logger.warn("Login failed for admin: {}", request.getEmail());
            return new AuthResponse(false, "Invalid email or password", null);

        } catch (Exception e) {
            logger.error("Error during admin login", e);
            return new AuthResponse(false, "Login error: " + e.getMessage(), null);
        }
    }
}
