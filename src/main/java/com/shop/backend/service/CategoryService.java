package com.shop.backend.service;

import com.shop.backend.model.dto.CategoryDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface CategoryService {
    
    List<CategoryDTO> getAllCategories();
    
    Page<CategoryDTO> getAllCategoriesPaged(Pageable pageable);
    
    CategoryDTO getCategoryById(Long id);
    
    CategoryDTO getCategoryByName(String name);
    
    CategoryDTO createCategory(CategoryDTO categoryDTO);
    
    CategoryDTO updateCategory(Long id, CategoryDTO categoryDTO);
    
    void deleteCategory(Long id);
} 