package com.example.ecommercebackend.DTOs;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrderItemRequestDTO {
    private Long orderItemId;
    private ProductRequestDTO product;
    private Integer quantity;
    private double discount;
    private double orderedProductPrice;
}
