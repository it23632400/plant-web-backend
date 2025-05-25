package com.project.Plant.CategoryManagement.Service;


import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Base64;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.Plant.CategoryManagement.Entity.Category;
import com.project.Plant.CategoryManagement.DTO.CategoryDTO;
import com.project.Plant.CategoryManagement.DTO.ApiResponse;
import com.project.Plant.CategoryManagement.Repository.CategoryRepository;
import com.project.Plant.CategoryManagement.Service.CategoryService;


@Service
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;
    private final ObjectMapper objectMapper;

    @Autowired
    public CategoryServiceImpl(CategoryRepository categoryRepository, ObjectMapper objectMapper) {
        this.categoryRepository = categoryRepository;
        this.objectMapper = objectMapper;
    }

    @Override
    public ApiResponse addCategory(String categoryJson, MultipartFile image) {
        try {
            CategoryDTO categoryDTO = objectMapper.readValue(categoryJson, CategoryDTO.class);

            // Create new category entity
            Category category = new Category();
            category.setName(categoryDTO.getName());
            category.setDescription(categoryDTO.getDescription());

            // Handle image upload
            if (image != null && !image.isEmpty()) {
                try {
                    category.setImage(image.getBytes());
                } catch (IOException e) {
                    return new ApiResponse(false, "Failed to process image: " + e.getMessage());
                }
            }

            // Save to database
            Category savedCategory = categoryRepository.save(category);

            // Map saved entity back to DTO
            CategoryDTO savedDTO = mapToDTO(savedCategory);

            return new ApiResponse(true, "Category added successfully", savedDTO);
        } catch (Exception e) {
            return new ApiResponse(false, "Failed to add category: " + e.getMessage());
        }
    }

    @Override
    public List<CategoryDTO> getAllCategories() {
        return categoryRepository.findAll().stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public CategoryDTO getCategoryById(Long id) {
        return categoryRepository.findById(id)
                .map(this::mapToDTO)
                .orElse(null);
    }

    @Override
    public ApiResponse updateCategory(Long id, String categoryJson, MultipartFile image) {
        try {
            Category existingCategory = categoryRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Category not found with id: " + id));

            CategoryDTO categoryDTO = objectMapper.readValue(categoryJson, CategoryDTO.class);

            // Update fields
            existingCategory.setName(categoryDTO.getName());
            existingCategory.setDescription(categoryDTO.getDescription());

            // Handle image update
            if (image != null && !image.isEmpty()) {
                try {
                    existingCategory.setImage(image.getBytes());
                } catch (IOException e) {
                    return new ApiResponse(false, "Failed to process image: " + e.getMessage());
                }
            }

            // Save updated category
            Category updatedCategory = categoryRepository.save(existingCategory);

            return new ApiResponse(true, "Category updated successfully", mapToDTO(updatedCategory));
        } catch (Exception e) {
            return new ApiResponse(false, "Failed to update category: " + e.getMessage());
        }
    }

    @Override
    public ApiResponse deleteCategory(Long id) {
        try {
            Category category = categoryRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Category not found with id: " + id));

            // Delete from database
            categoryRepository.delete(category);

            return new ApiResponse(true, "Category deleted successfully");
        } catch (Exception e) {
            return new ApiResponse(false, "Failed to delete category: " + e.getMessage());
        }
    }

    // Helper method to map Entity to DTO with Base64 encoding for image
    private CategoryDTO mapToDTO(Category category) {
        CategoryDTO dto = new CategoryDTO();
        dto.setId(category.getId());
        dto.setName(category.getName());
        dto.setDescription(category.getDescription());

        // Convert byte[] to Base64 string if image exists
        if (category.getImage() != null && category.getImage().length > 0) {
            String base64Image = Base64.getEncoder().encodeToString(category.getImage());
            dto.setImageBase64("data:image/jpeg;base64," + base64Image);
        }

        return dto;
    }

    public List<String> getAllCategoryNames() {
        return categoryRepository.findAll()
                .stream()
                .map(Category::getName)
                .collect(Collectors.toList());
    }
}