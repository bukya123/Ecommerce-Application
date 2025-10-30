package com.example.ecommercebackend.DTOs;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProductRequestDTO {
    private Long productId;
    private String productName;
    private String productDescription;
    private Double price;
    private Integer quantity;
    private String image;
    private Double discount;
    private Double specialPrice;
}
