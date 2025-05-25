package com.project.Plant.CategoryManagement.Service;


import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.project.Plant.CategoryManagement.DTO.CategoryDTO;
import com.project.Plant.CategoryManagement.DTO.ApiResponse;



public interface CategoryService {

    ApiResponse addCategory(String categoryJson, MultipartFile image);

    List<CategoryDTO> getAllCategories();

    CategoryDTO getCategoryById(Long id);

    ApiResponse updateCategory(Long id, String categoryJson, MultipartFile image);

    ApiResponse deleteCategory(Long id);

    List<String> getAllCategoryNames();
}