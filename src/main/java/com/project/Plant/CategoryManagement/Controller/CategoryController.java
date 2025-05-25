package com.project.Plant.CategoryManagement.Controller;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.project.Plant.CategoryManagement.DTO.CategoryDTO;
import com.project.Plant.CategoryManagement.DTO.ApiResponse;
import com.project.Plant.CategoryManagement.Service.CategoryService;



@RestController
@RequestMapping("/api/categories")

public class CategoryController {

    private final CategoryService categoryService;

    @Autowired
    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @PostMapping("/add-category")
    public ResponseEntity<ApiResponse> addCategory(
            @RequestParam("category") String categoryJson,
            @RequestParam(value = "image", required = false) MultipartFile image) {

        ApiResponse response = categoryService.addCategory(categoryJson, image);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/all")
    public ResponseEntity<List<CategoryDTO>> getAllCategories() {
        List<CategoryDTO> categories = categoryService.getAllCategories();
        return ResponseEntity.ok(categories);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CategoryDTO> getCategoryById(@PathVariable Long id) {
        CategoryDTO category = categoryService.getCategoryById(id);
        if (category != null) {
            return ResponseEntity.ok(category);
        }
        return ResponseEntity.notFound().build();
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<ApiResponse> updateCategory(
            @PathVariable Long id,
            @RequestParam("category") String categoryJson,
            @RequestParam(value = "image", required = false) MultipartFile image) {

        ApiResponse response = categoryService.updateCategory(id, categoryJson, image);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<ApiResponse> deleteCategory(@PathVariable Long id) {
        ApiResponse response = categoryService.deleteCategory(id);
        return ResponseEntity.ok(response);
    }
    @GetMapping("/categories")
    public ResponseEntity<List<String>> getAllCategoryNames() {
        List<String> categories = categoryService.getAllCategoryNames();
        return new ResponseEntity<>(categories, HttpStatus.OK);
    }
}