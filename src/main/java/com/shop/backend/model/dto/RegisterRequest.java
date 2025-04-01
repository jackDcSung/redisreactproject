package com.shop.backend.model.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RegisterRequest {
    
    @NotBlank(message = "用戶名不能為空")
    @Size(min = 3, max = 20, message = "用戶名長度應該在3-20個字符之間")
    private String username;
    
    @NotBlank(message = "郵箱不能為空")
    @Email(message = "郵箱格式不正確")
    private String email;
    
    @NotBlank(message = "密碼不能為空")
    @Size(min = 6, max = 40, message = "密碼長度應該在6-40個字符之間")
    private String password;
    
    private String fullName;
    
    private String phone;
    
    private String address;
} 