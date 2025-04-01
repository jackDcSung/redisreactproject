package com.shop.backend.config;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.stereotype.Component;

/**
 * Redis初始化配置類 - 用於應用啟動時初始化Redis相關設置
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class RedisInitConfig implements ApplicationRunner {

    private final RedisConnectionFactory redisConnectionFactory;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        try {
            log.info("正在初始化Redis連接...");
            redisConnectionFactory.getConnection().ping();
            log.info("Redis連接初始化成功");
        } catch (Exception e) {
            log.error("Redis連接失敗，應用將在無Redis的模式下運行", e);
            log.warn("請確保Redis服務已啟動，並檢查application.properties中的Redis配置是否正確");
            // 不拋出異常，允許應用在無Redis的情況下啟動
        }
    }

    @PostConstruct
    public void init() {
        log.info("Redis初始化配置已加載");
    }
} 