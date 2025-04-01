package com.shop.backend.controller;

import com.shop.backend.model.entity.User;
import com.shop.backend.repository.UserRepository;
import com.shop.backend.security.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
@Slf4j
@CrossOrigin(origins = "*", maxAge = 3600)
public class UserController {

    private final UserRepository userRepository;

    /**
     * 獲取當前登入用戶的個人資料
     * GET /users/profile
     */
    @GetMapping("/profile")
    public ResponseEntity<?> getUserProfile() {
        try {
            // 獲取認證信息
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            log.info("認證信息: {}", authentication);
            
            if (authentication == null) {
                log.error("認證信息為空");
                return ResponseEntity.status(401).body(Map.of("error", "未授權", "message", "認證信息為空"));
            }
            
            log.info("認證主體: {}", authentication.getPrincipal());
            log.info("認證是否已認證: {}", authentication.isAuthenticated());
            log.info("認證權限: {}", authentication.getAuthorities());
            
            // 獲取用戶詳情
            if (!(authentication.getPrincipal() instanceof UserDetailsImpl)) {
                log.error("認證主體不是UserDetailsImpl類型: {}", authentication.getPrincipal().getClass().getName());
                return ResponseEntity.status(401).body(Map.of("error", "未授權", "message", "認證主體類型錯誤"));
            }
            
            UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
            log.info("用戶名: {}", userDetails.getUsername());
            
            // 從數據庫獲取用戶
            User user = userRepository.findByUsername(userDetails.getUsername())
                    .orElseThrow(() -> {
                        log.error("用戶不存在: {}", userDetails.getUsername());
                        return new RuntimeException("用戶不存在");
                    });
            log.info("成功獲取用戶: {}", user.getUsername());
            
            // 構建響應
            Map<String, Object> response = new HashMap<>();
            response.put("id", user.getId());
            response.put("username", user.getUsername());
            response.put("email", user.getEmail());
            response.put("fullName", user.getFullName());
            response.put("phone", user.getPhone());
            response.put("address", user.getAddress());
            response.put("role", user.getRole().name());
            response.put("createdAt", user.getCreatedAt());
            response.put("updatedAt", user.getUpdatedAt());
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("獲取用戶資料時發生錯誤", e);
            return ResponseEntity.status(500).body(Map.of("error", "服務器錯誤", "message", e.getMessage()));
        }
    }
} 