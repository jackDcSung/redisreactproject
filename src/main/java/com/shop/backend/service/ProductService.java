package com.shop.backend.service;

import com.shop.backend.model.dto.ProductDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ProductService {
    
    Page<ProductDTO> getAllProducts(Pageable pageable);
    
    ProductDTO getProductById(Long id);
    
    Page<ProductDTO> getProductsByCategory(Long categoryId, Pageable pageable);
    
    Page<ProductDTO> searchProducts(String keyword, Pageable pageable);
    
    ProductDTO saveProduct(ProductDTO productDTO);
    
    void deleteProduct(Long id);
    
    List<ProductDTO> getLatestProducts(int limit);
    
    List<ProductDTO> getRandomProducts(int limit);
} 