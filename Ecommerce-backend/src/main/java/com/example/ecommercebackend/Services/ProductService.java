package com.example.ecommercebackend.Services;

import com.example.ecommercebackend.DTOs.ProductRequestDTO;
import com.example.ecommercebackend.DTOs.ProductResponseDTO;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface ProductService {
    public ProductRequestDTO addProduct(ProductRequestDTO productRequestDTO, Long categoryId);

    public ProductResponseDTO getProducts(Integer pageNumber, Integer pageSize, String sortBy, String sortOrder, String keyword, String category);


    ProductResponseDTO getAllProductsForAdmin(Integer pageNumber, Integer pageSize, String sortBy, String sortOrder);

    ProductResponseDTO getAllProductsForSeller(Integer pageNumber, Integer pageSize, String sortBy, String sortOrder);

    public  ProductResponseDTO getProductsByCategory(Long categoryId, Integer pageNumber, Integer pageSize, String sortBy, String sortOrder);

    public ProductResponseDTO getProductsByKeyword(String keyword,Integer pageNumber, Integer pageSize,String sortBy,String sortOrder);

    public ProductRequestDTO updateProducts(Long productId,ProductRequestDTO productRequestDTO);

    public ProductRequestDTO deleteProduct(Long productId);

    public ProductRequestDTO updateProductImage(Long productId, MultipartFile productImage) throws IOException;

}
