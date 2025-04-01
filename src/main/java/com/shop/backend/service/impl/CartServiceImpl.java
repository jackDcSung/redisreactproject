package com.shop.backend.service.impl;

import com.shop.backend.exception.ResourceNotFoundException;
import com.shop.backend.model.dto.CartItemDTO;
import com.shop.backend.model.entity.CartItem;
import com.shop.backend.model.entity.Product;
import com.shop.backend.model.entity.User;
import com.shop.backend.repository.CartItemRepository;
import com.shop.backend.repository.ProductRepository;
import com.shop.backend.repository.UserRepository;
import com.shop.backend.service.CartService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class CartServiceImpl implements CartService {

    private final CartItemRepository cartItemRepository;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;

    @Override
    public List<CartItemDTO> getCartItems(Long userId) {
        List<CartItem> cartItems = cartItemRepository.findByUserId(userId);
        return cartItems.stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public CartItemDTO addToCart(CartItemDTO cartItemDTO) {
        User user = userRepository.findById(cartItemDTO.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("用戶不存在，ID: " + cartItemDTO.getUserId()));

        Product product = productRepository.findById(cartItemDTO.getProductId())
                .orElseThrow(() -> new ResourceNotFoundException("產品不存在，ID: " + cartItemDTO.getProductId()));

        // 檢查購物車中是否已有該商品
        CartItem existingItem = cartItemRepository.findByUserIdAndProductId(user.getId(), product.getId())
                .orElse(null);
        
        if (existingItem != null) {
            // 已有該商品，更新數量
            existingItem.setQuantity(existingItem.getQuantity() + cartItemDTO.getQuantity());
            return mapToDTO(cartItemRepository.save(existingItem));
        } else {
            // 沒有該商品，新增購物車項
            CartItem newCartItem = CartItem.builder()
                    .user(user)
                    .product(product)
                    .quantity(cartItemDTO.getQuantity())
                    .build();
            return mapToDTO(cartItemRepository.save(newCartItem));
        }
    }

    @Override
    @Transactional
    public CartItemDTO updateCartItem(Long id, CartItemDTO cartItemDTO) {
        CartItem cartItem = cartItemRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("購物車項不存在，ID: " + id));
        
        cartItem.setQuantity(cartItemDTO.getQuantity());
        return mapToDTO(cartItemRepository.save(cartItem));
    }

    @Override
    @Transactional
    public void removeFromCart(Long id) {
        cartItemRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("購物車項不存在，ID: " + id));
        
        cartItemRepository.deleteById(id);
    }

    @Override
    @Transactional
    public void clearCart(Long userId) {
        userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("用戶不存在，ID: " + userId));
        
        cartItemRepository.deleteByUserId(userId);
    }

    @Override
    public int getCartItemCount(Long userId) {
        List<CartItem> cartItems = cartItemRepository.findByUserId(userId);
        return cartItems.stream().mapToInt(CartItem::getQuantity).sum();
    }

    @Override
    public Double getCartTotal(Long userId) {
        List<CartItem> cartItems = cartItemRepository.findByUserId(userId);
        return cartItems.stream()
                .mapToDouble(item -> item.getProduct().getPrice().doubleValue() * item.getQuantity())
                .sum();
    }

    private CartItemDTO mapToDTO(CartItem cartItem) {
        return CartItemDTO.builder()
                .id(cartItem.getId())
                .userId(cartItem.getUser().getId())
                .productId(cartItem.getProduct().getId())
                .productName(cartItem.getProduct().getName())
                .productImage(cartItem.getProduct().getImageUrl())
                .productPrice(cartItem.getProduct().getPrice())
                .quantity(cartItem.getQuantity())
                .build();
    }
} 