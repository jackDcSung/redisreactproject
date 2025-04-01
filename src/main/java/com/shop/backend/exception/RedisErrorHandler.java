package com.shop.backend.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.Cache;
import org.springframework.cache.interceptor.CacheErrorHandler;
import org.springframework.stereotype.Component;

/**
 * Redis緩存操作異常處理器
 * 處理Redis連接問題，確保Redis故障不影響應用正常運行
 */
@Component
@Slf4j
public class RedisErrorHandler implements CacheErrorHandler {

    @Override
    public void handleCacheGetError(RuntimeException exception, Cache cache, Object key) {
        handleError(exception, key, "獲取");
    }

    @Override
    public void handleCachePutError(RuntimeException exception, Cache cache, Object key, Object value) {
        handleError(exception, key, "設置");
    }

    @Override
    public void handleCacheEvictError(RuntimeException exception, Cache cache, Object key) {
        handleError(exception, key, "刪除");
    }

    @Override
    public void handleCacheClearError(RuntimeException exception, Cache cache) {
        log.error("Redis緩存清空操作失敗: {}", exception.getMessage());
    }

    private void handleError(RuntimeException exception, Object key, String operation) {
        log.error("Redis緩存{}操作失敗，key: {}，異常: {}", operation, key, exception.getMessage());
        log.debug("Redis異常詳情", exception);
    }
}