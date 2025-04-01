package com.shop.backend.repository;

import com.shop.backend.model.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {
    
    /**
     * 根據名稱查詢分類
     * @param name 分類名稱
     * @return 分類信息
     */
    Optional<Category> findByName(String name);
    
    /**
     * 檢查分類名稱是否存在
     * @param name 分類名稱
     * @return 是否存在
     */
    boolean existsByName(String name);
    
    /**
     * 查詢所有啟用的分類
     * @return 分類列表
     */
    List<Category> findByIsActiveTrue();
    
    /**
     * 根據ID查詢分類及其關聯的產品
     * @param id 分類ID
     * @return 分類及其產品信息
     */
    @Query("SELECT c FROM Category c LEFT JOIN FETCH c.products WHERE c.id = :id")
    Optional<Category> findByIdWithProducts(Long id);
} 