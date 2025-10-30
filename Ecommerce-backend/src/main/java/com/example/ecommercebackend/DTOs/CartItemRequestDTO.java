package com.example.ecommercebackend.DTOs;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CartItemRequestDTO {
    private Long productId;
    private Integer quantity;
}
