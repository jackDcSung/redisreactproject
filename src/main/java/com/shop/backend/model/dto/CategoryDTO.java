package com.shop.backend.model.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CategoryDTO implements Serializable {
    
    private Long id;
    
    @NotBlank(message = "分類名稱不能為空")
    private String name;
    
    private String description;
    
    private String imageUrl;
    
    private Boolean isActive;
    
    private int productCount;
} 