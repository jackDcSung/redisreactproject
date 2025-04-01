package com.shop.backend.service.impl;

import com.shop.backend.model.dto.ProductDTO;
import com.shop.backend.service.ProductService;
import com.shop.backend.service.RecentlyViewedService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class RecentlyViewedServiceImpl implements RecentlyViewedService {
    
    private static final String RECENTLY_VIEWED_KEY_PREFIX = "user:";
    private static final String RECENTLY_VIEWED_KEY_SUFFIX = ":recentlyViewed";
    private static final String TRENDING_PRODUCTS_KEY = "trending:products";
    private static final int MAX_RECENTLY_VIEWED_SIZE = 10;
    private static final int MAX_TRENDING_PRODUCTS_SIZE = 20;
    
    private final RedisTemplate<String, Object> redisTemplate;
    private final ProductService productService;
    
    @Override
    public void addToRecentlyViewed(Long userId, Long productId) {
        if (userId == null || productId == null) {
            log.warn("無法添加至最近瀏覽：userId或productId為空");
            return;
        }
        
        String key = buildRecentlyViewedKey(userId);
        try {
            // 先檢查並移除相同的商品ID（如果存在）
            redisTemplate.opsForList().remove(key, 0, productId);
            
            // 將新商品ID添加到列表的最前面
            redisTemplate.opsForList().leftPush(key, productId);
            
            // 修剪列表，保持不超過設定的大小
            redisTemplate.opsForList().trim(key, 0, MAX_RECENTLY_VIEWED_SIZE - 1);
            
            // 設置過期時間（7天）
            redisTemplate.expire(key, 7, TimeUnit.DAYS);
            
            log.debug("已將商品ID: {} 添加到用戶ID: {} 的最近瀏覽列表", productId, userId);
        } catch (Exception e) {
            log.error("添加商品至最近瀏覽列表時發生錯誤", e);
        }
    }
    
    @Override
    public List<ProductDTO> getRecentlyViewed(Long userId) {
        if (userId == null) {
            log.warn("無法獲取最近瀏覽：userId為空");
            return Collections.emptyList();
        }
        
        String key = buildRecentlyViewedKey(userId);
        try {
            // 從Redis列表獲取所有商品ID
            List<Object> productIds = redisTemplate.opsForList().range(key, 0, -1);
            
            if (productIds == null || productIds.isEmpty()) {
                return Collections.emptyList();
            }
            
            // 將Object轉換為Long，並過濾掉null值
            List<Long> ids = productIds.stream()
                    .map(id -> {
                        if (id instanceof Integer) {
                            return ((Integer) id).longValue();
                        } else if (id instanceof Long) {
                            return (Long) id;
                        } else if (id instanceof String) {
                            try {
                                return Long.parseLong((String) id);
                            } catch (NumberFormatException e) {
                                return null;
                            }
                        }
                        return null;
                    })
                    .filter(Objects::nonNull)
                    .collect(Collectors.toList());
            
            List<ProductDTO> products = new ArrayList<>();
            for (Long id : ids) {
                try {
                    ProductDTO product = productService.getProductById(id);
                    products.add(product);
                } catch (Exception e) {
                    log.warn("獲取商品ID: {} 失敗", id, e);
                }
            }
            
            return products;
        } catch (Exception e) {
            log.error("獲取最近瀏覽商品時發生錯誤", e);
            return Collections.emptyList();
        }
    }
    
    @Override
    public void addToTrendingProducts(Long productId) {
        if (productId == null) {
            return;
        }
        
        try {
            // 檢查商品是否已經在列表中
            Boolean exists = redisTemplate.opsForZSet().score(TRENDING_PRODUCTS_KEY, productId) != null;
            
            // 增加商品的分數（如果存在，則增加1，不存在則設為1）
            redisTemplate.opsForZSet().incrementScore(TRENDING_PRODUCTS_KEY, productId, 1);
            
            // 設置過期時間（1天）
            if (Boolean.TRUE.equals(exists)) {
                redisTemplate.expire(TRENDING_PRODUCTS_KEY, 1, TimeUnit.DAYS);
            }
            
            // 保留分數最高的前N個元素
            redisTemplate.opsForZSet().removeRange(TRENDING_PRODUCTS_KEY, 0, -MAX_TRENDING_PRODUCTS_SIZE - 1);
            
            log.debug("已更新商品ID: {} 的熱度得分", productId);
        } catch (Exception e) {
            log.error("更新商品熱度時發生錯誤", e);
        }
    }
    
    @Override
    public List<ProductDTO> getTrendingProducts(int limit) {
        try {
            // 獲取分數最高的N個商品ID（從高到低排序）
            Set<Object> productIds = redisTemplate.opsForZSet().reverseRange(TRENDING_PRODUCTS_KEY, 0, limit - 1);
            
            if (productIds == null || productIds.isEmpty()) {
                return Collections.emptyList();
            }
            
            // 將Object轉換為Long，並過濾掉null值
            List<Long> ids = productIds.stream()
                    .map(id -> {
                        if (id instanceof Integer) {
                            return ((Integer) id).longValue();
                        } else if (id instanceof Long) {
                            return (Long) id;
                        } else if (id instanceof String) {
                            try {
                                return Long.parseLong((String) id);
                            } catch (NumberFormatException e) {
                                return null;
                            }
                        }
                        return null;
                    })
                    .filter(Objects::nonNull)
                    .collect(Collectors.toList());
            
            List<ProductDTO> products = new ArrayList<>();
            for (Long id : ids) {
                try {
                    ProductDTO product = productService.getProductById(id);
                    products.add(product);
                } catch (Exception e) {
                    log.warn("獲取商品ID: {} 失敗", id, e);
                }
            }
            
            return products;
        } catch (Exception e) {
            log.error("獲取熱門商品時發生錯誤", e);
            return Collections.emptyList();
        }
    }
    
    private String buildRecentlyViewedKey(Long userId) {
        return RECENTLY_VIEWED_KEY_PREFIX + userId + RECENTLY_VIEWED_KEY_SUFFIX;
    }
} 