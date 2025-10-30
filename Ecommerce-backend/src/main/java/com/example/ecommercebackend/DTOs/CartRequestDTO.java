package com.example.ecommercebackend.DTOs;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class CartRequestDTO {
    private Long cartId;
    private double totalPrice;
    private List<ProductRequestDTO> products = new ArrayList<>();
}
