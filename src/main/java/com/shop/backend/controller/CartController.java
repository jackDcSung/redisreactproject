package com.shop.backend.controller;

import com.shop.backend.model.dto.CartItemDTO;
import com.shop.backend.service.CartService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/cart")
@RequiredArgsConstructor
public class CartController {

    private final CartService cartService;

    @GetMapping
    public ResponseEntity<List<CartItemDTO>> getCartItems() {
        Long userId = getCurrentUserId();
        return ResponseEntity.ok(cartService.getCartItems(userId));
    }

    @PostMapping
    public ResponseEntity<CartItemDTO> addToCart(@RequestBody CartItemDTO cartItemDTO) {
        Long userId = getCurrentUserId();
        cartItemDTO.setUserId(userId);
        return ResponseEntity.ok(cartService.addToCart(cartItemDTO));
    }

    @PutMapping("/{id}")
    public ResponseEntity<CartItemDTO> updateCartItem(@PathVariable Long id, @RequestBody CartItemDTO cartItemDTO) {
        Long userId = getCurrentUserId();
        cartItemDTO.setUserId(userId);
        return ResponseEntity.ok(cartService.updateCartItem(id, cartItemDTO));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> removeFromCart(@PathVariable Long id) {
        cartService.removeFromCart(id);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping
    public ResponseEntity<Void> clearCart() {
        Long userId = getCurrentUserId();
        cartService.clearCart(userId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/count")
    public ResponseEntity<Map<String, Integer>> getCartItemCount() {
        Long userId = getCurrentUserId();
        int count = cartService.getCartItemCount(userId);
        return ResponseEntity.ok(Map.of("count", count));
    }

    @GetMapping("/total")
    public ResponseEntity<Map<String, Double>> getCartTotal() {
        Long userId = getCurrentUserId();
        Double total = cartService.getCartTotal(userId);
        return ResponseEntity.ok(Map.of("total", total));
    }

    private Long getCurrentUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated() || "anonymousUser".equals(authentication.getPrincipal().toString())) {
            throw new IllegalStateException("用戶未登錄");
        }
        return Long.parseLong(authentication.getName());
    }
} 