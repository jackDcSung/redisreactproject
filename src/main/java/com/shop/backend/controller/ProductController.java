package com.shop.backend.controller;

import com.shop.backend.model.dto.ProductDTO;
import com.shop.backend.service.ProductService;
import com.shop.backend.service.RecentlyViewedService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;
    private final RecentlyViewedService recentlyViewedService;

    @GetMapping
    public ResponseEntity<Page<ProductDTO>> getAllProducts(Pageable pageable) {
        return ResponseEntity.ok(productService.getAllProducts(pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductDTO> getProductById(@PathVariable Long id) {
        // 獲取當前認證用戶
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated() && !"anonymousUser".equals(authentication.getPrincipal().toString())) {
            try {
                // 從Authentication獲取用戶ID (假設用戶ID存儲在principal中)
                Long userId = Long.parseLong(authentication.getName());
                // 添加到最近瀏覽
                recentlyViewedService.addToRecentlyViewed(userId, id);
                // 更新熱門商品計數
                recentlyViewedService.addToTrendingProducts(id);
            } catch (Exception e) {
                // 記錄錯誤但不影響正常流程
            }
        }
        return ResponseEntity.ok(productService.getProductById(id));
    }

    @GetMapping("/category/{categoryId}")
    public ResponseEntity<Page<ProductDTO>> getProductsByCategory(@PathVariable Long categoryId, Pageable pageable) {
        return ResponseEntity.ok(productService.getProductsByCategory(categoryId, pageable));
    }

    @GetMapping("/search")
    public ResponseEntity<Page<ProductDTO>> searchProducts(@RequestParam String keyword, Pageable pageable) {
        return ResponseEntity.ok(productService.searchProducts(keyword, pageable));
    }

    @PostMapping
    public ResponseEntity<ProductDTO> createProduct(@RequestBody ProductDTO productDTO) {
        return ResponseEntity.ok(productService.saveProduct(productDTO));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProductDTO> updateProduct(@PathVariable Long id, @RequestBody ProductDTO productDTO) {
        productDTO.setId(id);
        return ResponseEntity.ok(productService.saveProduct(productDTO));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id) {
        productService.deleteProduct(id);
        return ResponseEntity.noContent().build();
    }
    
    @GetMapping("/recently-viewed")
    public ResponseEntity<List<ProductDTO>> getRecentlyViewedProducts() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated() || "anonymousUser".equals(authentication.getPrincipal().toString())) {
            return ResponseEntity.ok(List.of());
        }
        
        try {
            Long userId = Long.parseLong(authentication.getName());
            return ResponseEntity.ok(recentlyViewedService.getRecentlyViewed(userId));
        } catch (Exception e) {
            return ResponseEntity.ok(List.of());
        }
    }
    
    @GetMapping("/trending")
    public ResponseEntity<List<ProductDTO>> getTrendingProducts(@RequestParam(defaultValue = "10") int limit) {
        return ResponseEntity.ok(recentlyViewedService.getTrendingProducts(limit));
    }
    
    @GetMapping("/latest")
    public ResponseEntity<List<ProductDTO>> getLatestProducts(@RequestParam(defaultValue = "8") int limit) {
        return ResponseEntity.ok(productService.getLatestProducts(limit));
    }
    
    @GetMapping("/random")
    public ResponseEntity<List<ProductDTO>> getRandomProducts(@RequestParam(defaultValue = "8") int limit) {
        return ResponseEntity.ok(productService.getRandomProducts(limit));
    }
} 