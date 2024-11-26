package com.interverse.demo.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.interverse.demo.dto.CartDTO;
import com.interverse.demo.dto.CartResponseDTO;
import com.interverse.demo.service.CartService;


@RestController
@RequestMapping("/cart")
public class CartController {

    @Autowired
    private CartService cartService;

    @PostMapping("/add")
    public ResponseEntity<CartResponseDTO> addToCart(@RequestBody CartDTO cartDto) {
        CartResponseDTO savedCart = cartService.addOrUpdateCart(cartDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedCart);
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<CartResponseDTO>> getUserCart(@PathVariable Integer userId) {
        List<CartResponseDTO> userCart = cartService.getCartItemsByUser(userId);
        return ResponseEntity.ok(userCart);
    }

    @PutMapping("/update")
    public ResponseEntity<CartResponseDTO> updateCartItem(@RequestBody CartDTO cartDto) {
        CartResponseDTO updatedCart = cartService.updateCartItemQuantity(cartDto);
        return ResponseEntity.ok(updatedCart);
    }

    @DeleteMapping("/delete")
    public ResponseEntity<String> deleteCartItem(@RequestParam Integer userId, @RequestParam Integer productId) {
        cartService.deleteCartItem(userId, productId);
        return ResponseEntity.ok("Cart item deleted successfully");
    }

    @DeleteMapping("/clear/{userId}")
    public ResponseEntity<String> clearUserCart(@PathVariable Integer userId) {
        cartService.clearUserCart(userId);
        return ResponseEntity.ok("User cart cleared successfully");
    }

    @GetMapping("/count/{userId}")
    public ResponseEntity<Integer> getCartItemCount(@PathVariable Integer userId) {
        int count = cartService.getCartItemCount(userId);
        return ResponseEntity.ok(count);
    }

    @GetMapping("/all")
    public ResponseEntity<List<CartResponseDTO>> getAllCartItems() {
        List<CartResponseDTO> allItems = cartService.getAllCartItems();
        return ResponseEntity.ok(allItems);
    }

    @PostMapping("/clear-after-order/{userId}")
    public ResponseEntity<String> clearCartAfterOrder(@PathVariable Integer userId) {
        cartService.clearUserCart(userId);
        return ResponseEntity.ok("User cart cleared successfully after order creation");
    }
    
    // 錯誤處理方法
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<String> handleRuntimeException(RuntimeException ex) {
        return ResponseEntity.badRequest().body(ex.getMessage());
    }
    
    
}
