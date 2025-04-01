package com.shop.backend.service.impl;

import com.shop.backend.exception.ResourceNotFoundException;
import com.shop.backend.model.dto.ProductDTO;
import com.shop.backend.model.entity.Category;
import com.shop.backend.model.entity.Product;
import com.shop.backend.repository.CategoryRepository;
import com.shop.backend.repository.ProductRepository;
import com.shop.backend.service.ProductService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;

    @Override
    public Page<ProductDTO> getAllProducts(Pageable pageable) {
        Page<Product> productPage = productRepository.findAll(pageable);
        List<ProductDTO> productDTOs = productPage.getContent().stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
        
        return new PageImpl<>(productDTOs, pageable, productPage.getTotalElements());
    }

    @Override
    public ProductDTO getProductById(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("產品不存在，ID: " + id));
        
        return mapToDTO(product);
    }

    @Override
    public Page<ProductDTO> getProductsByCategory(Long categoryId, Pageable pageable) {
        Page<Product> productPage = productRepository.findByCategoryId(categoryId, pageable);
        List<ProductDTO> productDTOs = productPage.getContent().stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
        
        return new PageImpl<>(productDTOs, pageable, productPage.getTotalElements());
    }

    @Override
    public Page<ProductDTO> searchProducts(String keyword, Pageable pageable) {
        Page<Product> productPage = productRepository.searchProducts(keyword, pageable);
        List<ProductDTO> productDTOs = productPage.getContent().stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
        
        return new PageImpl<>(productDTOs, pageable, productPage.getTotalElements());
    }

    @Override
    @Transactional
    public ProductDTO saveProduct(ProductDTO productDTO) {
        Product product;
        
        if (productDTO.getId() != null) {
            // 更新現有產品
            product = productRepository.findById(productDTO.getId())
                    .orElseThrow(() -> new ResourceNotFoundException("產品不存在，ID: " + productDTO.getId()));
            
            product.setName(productDTO.getName());
            product.setDescription(productDTO.getDescription());
            product.setPrice(productDTO.getPrice());
            product.setImageUrl(productDTO.getImageUrl());
            product.setStock(productDTO.getStock());
            product.setUpdatedAt(LocalDateTime.now());
            
            if (productDTO.getCategoryId() != null && 
                    (product.getCategory() == null || !product.getCategory().getId().equals(productDTO.getCategoryId()))) {
                Category category = categoryRepository.findById(productDTO.getCategoryId())
                        .orElseThrow(() -> new ResourceNotFoundException("分類不存在，ID: " + productDTO.getCategoryId()));
                product.setCategory(category);
            }
        } else {
            // 創建新產品
            Category category = null;
            if (productDTO.getCategoryId() != null) {
                category = categoryRepository.findById(productDTO.getCategoryId())
                        .orElseThrow(() -> new ResourceNotFoundException("分類不存在，ID: " + productDTO.getCategoryId()));
            }
            
            product = Product.builder()
                    .name(productDTO.getName())
                    .description(productDTO.getDescription())
                    .price(productDTO.getPrice())
                    .imageUrl(productDTO.getImageUrl())
                    .stock(productDTO.getStock())
                    .category(category)
                    .isActive(true)
                    .build();
        }
        
        Product savedProduct = productRepository.save(product);
        return mapToDTO(savedProduct);
    }

    @Override
    @Transactional
    public void deleteProduct(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("產品不存在，ID: " + id));
        
        productRepository.delete(product);
    }

    @Override
    public List<ProductDTO> getLatestProducts(int limit) {
        Pageable pageable = Pageable.ofSize(limit);
        return productRepository.findLatestProducts(pageable).stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<ProductDTO> getRandomProducts(int limit) {
        Pageable pageable = Pageable.ofSize(limit);
        return productRepository.findRandomProducts(pageable).stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    private ProductDTO mapToDTO(Product product) {
        return ProductDTO.builder()
                .id(product.getId())
                .name(product.getName())
                .description(product.getDescription())
                .price(product.getPrice())
                .imageUrl(product.getImageUrl())
                .stock(product.getStock())
                .categoryId(product.getCategory() != null ? product.getCategory().getId() : null)
                .categoryName(product.getCategory() != null ? product.getCategory().getName() : null)
                .isActive(product.getIsActive())
                .createdAt(product.getCreatedAt())
                .updatedAt(product.getUpdatedAt())
                .build();
    }
} 