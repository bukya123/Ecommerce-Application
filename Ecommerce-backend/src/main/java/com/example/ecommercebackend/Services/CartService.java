package com.example.ecommercebackend.Services;

import com.example.ecommercebackend.DTOs.CartItemRequestDTO;
import com.example.ecommercebackend.DTOs.CartRequestDTO;
import com.example.ecommercebackend.DTOs.CartResponseDTO;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface CartService {
    public CartRequestDTO addProductToCart(Long ProductId, Integer Quantity);
    public List<CartRequestDTO> getAllCarts();
    public CartRequestDTO getCart(String emailId,Long cartId);
    public CartRequestDTO updateProductCart(Long ProductId, Integer Quantity);
    public String deleteProductFromCart(Long CartId,Long ProductId);
    public void updateProductInCarts(Long cartId, Long productId);
    public String addOrUpdateCart(List<CartItemRequestDTO> cartItemRequestDTOS);

}
