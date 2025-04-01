package com.shop.backend.service;

import com.shop.backend.model.dto.ProductDTO;
import java.util.List;

/**
 * 處理用戶最近瀏覽商品和熱門商品的服務接口
 */
public interface RecentlyViewedService {
    
    /**
     * 將商品添加到用戶的最近瀏覽列表中
     * 
     * @param userId 用戶ID
     * @param productId 商品ID
     */
    void addToRecentlyViewed(Long userId, Long productId);
    
    /**
     * 獲取用戶最近瀏覽的商品列表
     * 
     * @param userId 用戶ID
     * @return 商品DTO列表
     */
    List<ProductDTO> getRecentlyViewed(Long userId);
    
    /**
     * 增加商品的熱度值
     * 
     * @param productId 商品ID
     */
    void addToTrendingProducts(Long productId);
    
    /**
     * 獲取熱門商品列表
     * 
     * @param limit 返回的商品數量限制
     * @return 商品DTO列表
     */
    List<ProductDTO> getTrendingProducts(int limit);
} 