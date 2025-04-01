package com.shop.backend.service.impl;

import com.shop.backend.exception.ResourceNotFoundException;
import com.shop.backend.exception.ResourceAlreadyExistsException;
import com.shop.backend.model.dto.CategoryDTO;
import com.shop.backend.model.entity.Category;
import com.shop.backend.repository.CategoryRepository;
import com.shop.backend.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;

    @Override
    @Cacheable(value = "categories")
    public List<CategoryDTO> getAllCategories() {
        return categoryRepository.findByIsActiveTrue().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public Page<CategoryDTO> getAllCategoriesPaged(Pageable pageable) {
        return categoryRepository.findAll(pageable)
                .map(this::convertToDTO);
    }

    @Override
    @Cacheable(value = "categories", key = "#id")
    public CategoryDTO getCategoryById(Long id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("分類不存在，ID: " + id));
        return convertToDTO(category);
    }

    @Override
    @Cacheable(value = "categories", key = "#name")
    public CategoryDTO getCategoryByName(String name) {
        Category category = categoryRepository.findByName(name)
                .orElseThrow(() -> new ResourceNotFoundException("分類不存在，名稱: " + name));
        return convertToDTO(category);
    }

    @Override
    @CachePut(value = "categories", key = "#result.id")
    public CategoryDTO createCategory(CategoryDTO categoryDTO) {
        if (categoryRepository.existsByName(categoryDTO.getName())) {
            throw new ResourceAlreadyExistsException("分類名稱已存在: " + categoryDTO.getName());
        }
        
        Category category = convertToEntity(categoryDTO);
        category.setIsActive(true);
        Category savedCategory = categoryRepository.save(category);
        return convertToDTO(savedCategory);
    }

    @Override
    @CachePut(value = "categories", key = "#id")
    public CategoryDTO updateCategory(Long id, CategoryDTO categoryDTO) {
        Category existingCategory = categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("分類不存在，ID: " + id));
        
        // 檢查名稱是否已被其他分類使用
        if (!existingCategory.getName().equals(categoryDTO.getName()) && 
                categoryRepository.existsByName(categoryDTO.getName())) {
            throw new ResourceAlreadyExistsException("分類名稱已存在: " + categoryDTO.getName());
        }
        
        existingCategory.setName(categoryDTO.getName());
        existingCategory.setDescription(categoryDTO.getDescription());
        existingCategory.setImageUrl(categoryDTO.getImageUrl());
        existingCategory.setIsActive(categoryDTO.getIsActive());
        
        Category updatedCategory = categoryRepository.save(existingCategory);
        return convertToDTO(updatedCategory);
    }

    @Override
    @CacheEvict(value = "categories", key = "#id")
    public void deleteCategory(Long id) {
        Category category = categoryRepository.findByIdWithProducts(id)
                .orElseThrow(() -> new ResourceNotFoundException("分類不存在，ID: " + id));
        
        if (category.getProducts() != null && !category.getProducts().isEmpty()) {
            throw new IllegalStateException("無法刪除分類，該分類下存在產品");
        }
        
        categoryRepository.deleteById(id);
    }
    
    private CategoryDTO convertToDTO(Category category) {
        return CategoryDTO.builder()
                .id(category.getId())
                .name(category.getName())
                .description(category.getDescription())
                .imageUrl(category.getImageUrl())
                .isActive(category.getIsActive())
                .build();
    }
    
    private Category convertToEntity(CategoryDTO categoryDTO) {
        return Category.builder()
                .id(categoryDTO.getId())
                .name(categoryDTO.getName())
                .description(categoryDTO.getDescription())
                .imageUrl(categoryDTO.getImageUrl())
                .isActive(categoryDTO.getIsActive())
                .build();
    }
} 