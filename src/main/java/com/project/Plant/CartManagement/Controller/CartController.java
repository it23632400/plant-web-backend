package com.project.Plant.CartManagement.Controller;

import com.project.Plant.CartManagement.Dto.CartDto;
import com.project.Plant.CartManagement.Service.CartService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/cart")
@RequiredArgsConstructor
public class CartController {

    private final CartService cartService;

    @PostMapping("/{userId}/add")
    public ResponseEntity<CartDto.CartResponse> addToCart(
            @PathVariable Long userId,
            @RequestBody CartDto.CartRequest request) {
        CartDto.CartResponse response = cartService.addToCart(userId, request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping("/{userId}")
    public ResponseEntity<List<CartDto.CartResponse>> getUserCart(@PathVariable Long userId) {
        List<CartDto.CartResponse> cartItems = cartService.getUserCart(userId);
        return ResponseEntity.ok(cartItems);
    }

    @PutMapping("/{userId}/update")
    public ResponseEntity<CartDto.CartResponse> updateCartItem(
            @PathVariable Long userId,
            @RequestBody CartDto.CartUpdateRequest request) {
        CartDto.CartResponse response = cartService.updateCartItem(userId, request);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{userId}/remove/{cartId}")
    public ResponseEntity<Void> removeCartItem(
            @PathVariable Long userId,
            @PathVariable Long cartId) {
        cartService.removeCartItem(userId, cartId);
        return ResponseEntity.noContent().build();
    }
}