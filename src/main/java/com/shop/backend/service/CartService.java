package com.shop.backend.service;

import com.shop.backend.model.dto.CartItemDTO;

import java.util.List;

public interface CartService {
    
    List<CartItemDTO> getCartItems(Long userId);
    
    CartItemDTO addToCart(CartItemDTO cartItemDTO);
    
    CartItemDTO updateCartItem(Long id, CartItemDTO cartItemDTO);
    
    void removeFromCart(Long id);
    
    void clearCart(Long userId);
    
    int getCartItemCount(Long userId);
    
    Double getCartTotal(Long userId);
} 