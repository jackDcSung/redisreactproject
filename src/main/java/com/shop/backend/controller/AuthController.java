package com.shop.backend.controller;

import com.shop.backend.model.dto.JwtResponse;
import com.shop.backend.model.dto.LoginRequest;
import com.shop.backend.model.dto.MessageResponse;
import com.shop.backend.model.dto.RegisterRequest;
import com.shop.backend.model.entity.User;
import com.shop.backend.repository.UserRepository;
import com.shop.backend.security.UserDetailsImpl;
import com.shop.backend.security.jwt.JwtUtils;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 認證相關控制器
 * 處理用戶登入註冊等功能
 */
@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Slf4j
public class AuthController {
    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final PasswordEncoder encoder;
    private final JwtUtils jwtUtils;

    @PostMapping("/login")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {
        log.info("登入請求: {}", loginRequest.getUsername());
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtUtils.generateJwtToken(authentication);
        
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        List<String> roles = userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());

        log.info("用戶 {} 登入成功", userDetails.getUsername());
        return ResponseEntity.ok(new JwtResponse(
                jwt,
                userDetails.getId(), 
                userDetails.getUsername(), 
                userDetails.getEmail(), 
                roles.get(0).replace("ROLE_", "")));
    }

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@Valid @RequestBody RegisterRequest signUpRequest) {
        log.info("註冊請求: {}", signUpRequest.getUsername());
        if (userRepository.existsByUsername(signUpRequest.getUsername())) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("錯誤: 用戶名已被使用!"));
        }

        if (userRepository.existsByEmail(signUpRequest.getEmail())) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("錯誤: 郵箱已被使用!"));
        }

        // 創建新用戶
        User user = User.builder()
                .username(signUpRequest.getUsername())
                .email(signUpRequest.getEmail())
                .password(encoder.encode(signUpRequest.getPassword()))
                .fullName(signUpRequest.getFullName())
                .phone(signUpRequest.getPhone())
                .address(signUpRequest.getAddress())
                .role(User.Role.USER)
                .isActive(true)
                .build();

        userRepository.save(user);
        log.info("用戶 {} 註冊成功", user.getUsername());
        return ResponseEntity.ok(new MessageResponse("用戶註冊成功!"));
    }
    
    @GetMapping("/check-username")
    public ResponseEntity<?> checkUsernameAvailability(@RequestParam String username) {
        boolean exists = userRepository.existsByUsername(username);
        return ResponseEntity.ok(new MessageResponse(exists ? "用戶名已被使用" : "用戶名可用"));
    }
    
    @GetMapping("/check-email")
    public ResponseEntity<?> checkEmailAvailability(@RequestParam String email) {
        boolean exists = userRepository.existsByEmail(email);
        return ResponseEntity.ok(new MessageResponse(exists ? "郵箱已被使用" : "郵箱可用"));
    }
} 