package com.example.ecommercebackend.Services;


import com.example.ecommercebackend.DTOs.CategoryRequestDTO;
import com.example.ecommercebackend.DTOs.CategoryResponseDTO;
import com.example.ecommercebackend.Modules.Category;

import java.util.List;

public interface CategoryService {
    public CategoryResponseDTO getCategories(Integer pageNo, Integer pageSize,String sortBy,String sortOrder);
    public CategoryRequestDTO postCategory(CategoryRequestDTO categoryRequestDTO);
    public CategoryRequestDTO putCategory(CategoryRequestDTO categoryRequestDTO, Long categoryId);
    public CategoryRequestDTO deleteCategory(Long categoryId);
}
