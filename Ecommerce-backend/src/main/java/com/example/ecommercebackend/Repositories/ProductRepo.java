package com.example.ecommercebackend.Repositories;

import com.example.ecommercebackend.Modules.AppUser;
import com.example.ecommercebackend.Modules.Category;
import com.example.ecommercebackend.Modules.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepo extends JpaRepository<Product,Long> {

    List<Product> findByProductNameLikeIgnoreCase(String keyword);

    Page<Product> findByProductName(String productName, Pageable pageable);

    Page<Product> findByProductNameLikeIgnoreCase(String productName, Pageable pageable);


    Page<Product> findByCategoryOrderByPrice(Category category, Pageable pageable);

    Page<Product> findAll(Specification<Product> spec, Pageable pageDetails);

    Page<Product> findByAppUser(AppUser appUser, Pageable pageable);
}
