package com.interverse.demo.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.interverse.demo.dto.CartDTO;
import com.interverse.demo.dto.CartResponseDTO;
import com.interverse.demo.model.Cart;
import com.interverse.demo.model.CartId;
import com.interverse.demo.model.CartRepository;
import com.interverse.demo.model.Product;
import com.interverse.demo.model.ProductRepository;
import com.interverse.demo.model.User;
import com.interverse.demo.model.UserRepository;

@Service
public class CartService {

    @Autowired
    private CartRepository cartRepo;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ProductRepository productRepository;

    @Transactional
    public CartResponseDTO addOrUpdateCart(CartDTO cartDTO) {
        User user = userRepository.findById(cartDTO.getUsersId())
            .orElseThrow(() -> new RuntimeException("User not found"));

        Product product = productRepository.findById(cartDTO.getProductsId())
            .orElseThrow(() -> new RuntimeException("Product not found"));

        Cart existingCart = cartRepo.findByUsersIdAndProductsId(cartDTO.getUsersId(), cartDTO.getProductsId());
        if (existingCart != null) {
            existingCart.setVol(existingCart.getVol() + cartDTO.getVol());
            return convertToResponseDTO(cartRepo.save(existingCart));
        } else {
            Cart newCart = new Cart();
            CartId cartId = new CartId(cartDTO.getUsersId(), cartDTO.getProductsId());
            newCart.setCartId(cartId);
            newCart.setVol(cartDTO.getVol());
            newCart.setUsers(user);
            newCart.setProducts(product);
            return convertToResponseDTO(cartRepo.save(newCart));
        }
    }

    public List<CartResponseDTO> getAllCartItems() {
        return cartRepo.findAll().stream()
                .map(this::convertToResponseDTO)
                .collect(Collectors.toList());
    }

    public List<CartResponseDTO> getCartItemsByUser(Integer userId) {
        return cartRepo.findByUsers(userId).stream()
                .map(this::convertToResponseDTO)
                .collect(Collectors.toList());
    }

    @Transactional
    public void deleteCartItem(Integer userId, Integer productId) {
        Cart cart = cartRepo.findByUsersIdAndProductsId(userId, productId);
        if (cart != null) {
            cartRepo.delete(cart);
        }
    }

    @Transactional
    public CartResponseDTO updateCartItemQuantity(CartDTO cartDTO) {
        Cart cart = cartRepo.findByUsersIdAndProductsId(cartDTO.getUsersId(), cartDTO.getProductsId());
        if (cart != null) {
            cart.setVol(cartDTO.getVol());
            return convertToResponseDTO(cartRepo.save(cart));
        }
        throw new RuntimeException("Cart item not found");
    }

    @Transactional
    public void clearUserCart(Integer userId) {
        List<Cart> userCartItems = cartRepo.findByUsers(userId);
        cartRepo.deleteAll(userCartItems);
    }

    public int getCartItemCount(Integer userId) {
        List<Cart> userCartItems = cartRepo.findByUsers(userId);
        return userCartItems.size();
    }

    public CartResponseDTO convertToResponseDTO(Cart cart) {
        CartResponseDTO dto = new CartResponseDTO();
        dto.setUserId(cart.getUsers().getId());
        dto.setProductId(cart.getProducts().getId());
        dto.setVol(cart.getVol());
        dto.setProductName(cart.getProducts().getName());
        dto.setPrice(cart.getProducts().getPrice()); // 添加價格信息
        // 可以添加更多產品信息，如描述等
        return dto;
    }
}