package com.project.Plant.UserManagement.Service;


import com.project.Plant.UserManagement.DTO.AuthRequest;
import com.project.Plant.UserManagement.DTO.AuthResponse;
import com.project.Plant.UserManagement.Entity.UserEntity;
import com.project.Plant.UserManagement.Repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class UserService {
    private static final Logger logger = LoggerFactory.getLogger(UserService.class);

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }


    public AuthResponse registerUser(AuthRequest request) {
        try {
            logger.info("Attempting to register user with email: {}", request.getEmail());
            if (userRepository.findByEmail(request.getEmail()).isPresent()) {
                logger.warn("Email already registered: {}", request.getEmail());
                return new AuthResponse(false, "Email is already registered", null);
            }


            UserEntity user = new UserEntity();
            user.setEmail(request.getEmail());
            user.setPassword(passwordEncoder.encode(request.getPassword()));


            try {
                user.setContactNo(Long.parseLong(request.getContactNo()));
            } catch (NumberFormatException e) {
                logger.error("Invalid contact number format: {}", request.getContactNo());
                return new AuthResponse(false, "Invalid contact number format", null);
            }


            user.setAddress(request.getAddress());


            userRepository.save(user);


            logger.info("User registered successfully: {}", request.getEmail());


            return new AuthResponse(true, "Registration successful", user);
        } catch (Exception e) {
            logger.error("Error during user registration", e);
            return new AuthResponse(false, "Error during registration: " + e.getMessage(), null);
        }
    }

    public AuthResponse loginUser(AuthRequest request) {
        try {
            logger.info("Login attempt for email: {}", request.getEmail());

            Optional<UserEntity> userOpt = userRepository.findByEmail(request.getEmail());
            if (userOpt.isPresent()) {
                UserEntity user = userOpt.get();


                if (passwordEncoder.matches(request.getPassword(), user.getPassword())) {


                    logger.info("User logged in successfully: {}", request.getEmail());
                    return new AuthResponse(true, "Login successful", user);
                }
            }

            logger.warn("Login failed for email: {}", request.getEmail());
            return new AuthResponse(false, "Invalid email or password", null);
        } catch (Exception e) {
            logger.error("Error during login", e);
            return new AuthResponse(false, "Login error: " + e.getMessage(), null);
        }
    }
}
