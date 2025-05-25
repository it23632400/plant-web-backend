package com.project.Plant.CategoryManagement.Repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.project.Plant.CategoryManagement.Entity.Category;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {
    // Add custom query methods if needed
}
