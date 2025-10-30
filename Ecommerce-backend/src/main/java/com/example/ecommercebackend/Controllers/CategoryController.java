package com.example.ecommercebackend.Controllers;


import com.example.ecommercebackend.Configs.AppConstants;
import com.example.ecommercebackend.DTOs.CategoryRequestDTO;
import com.example.ecommercebackend.DTOs.CategoryResponseDTO;
import com.example.ecommercebackend.Modules.Category;
import com.example.ecommercebackend.Services.CategoryService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController()
@RequestMapping("/api")
public class CategoryController {
    @Autowired
    private CategoryService categoryService;

    @GetMapping("/public/categories")
    public ResponseEntity<CategoryResponseDTO> getAllCategories(
            @RequestParam (name="pageNo",defaultValue = AppConstants.PAGE_NUMBER,required = false) Integer pageNo,
            @RequestParam (name="pageSize",defaultValue = AppConstants.PAGE_SIZE,required = false) Integer pageSize,
            @RequestParam (name="sortBy",defaultValue = AppConstants.SORT_CATEGORIES_BY,required = false)String sortBy,
            @RequestParam (name="sortOrder",defaultValue = AppConstants.SORT_ORDERS_BY,required = false) String sortOrder
    ){
        CategoryResponseDTO s=categoryService.getCategories(pageNo,pageSize,sortBy,sortOrder);
        ResponseEntity<CategoryResponseDTO> res=new ResponseEntity<>(s, HttpStatus.OK);
        return res;
    }

    @PostMapping("/admin/category")
    public ResponseEntity<CategoryRequestDTO> postCategory(@Valid @RequestBody CategoryRequestDTO categoryRequestDTO){
        CategoryRequestDTO categoryRequestDTO1=categoryService.postCategory(categoryRequestDTO);
        return new ResponseEntity<>(categoryRequestDTO1,HttpStatus.OK);
    }

    @PutMapping("/admin/categories/{categoryId}")
    public ResponseEntity<CategoryRequestDTO> updateCategory(@Valid @RequestBody CategoryRequestDTO categoryRequestDTO, @PathVariable Long categoryId){
        CategoryRequestDTO categoryRequestDTO1=categoryService.putCategory(categoryRequestDTO,categoryId);
        return new ResponseEntity<>(categoryRequestDTO1,HttpStatus.OK);
    }

    @DeleteMapping("/admin/categories/{categoryId}")
    public ResponseEntity<CategoryRequestDTO> deleteCategory(@PathVariable Long categoryId){
        CategoryRequestDTO categoryRequestDTO=categoryService.deleteCategory(categoryId);
        return new ResponseEntity<>(categoryRequestDTO,HttpStatus.OK);
    }

}
