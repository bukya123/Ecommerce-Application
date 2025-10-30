package com.example.ecommercebackend.DTOs;

import lombok.*;
import org.springframework.context.annotation.Bean;


@Getter
@Setter
public class CategoryRequestDTO {
    private Long categoryId;
    private String categoryName;
}
