package com.example.ecommercebackend.Repositories;

import com.example.ecommercebackend.Modules.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public interface CategoryRepo extends JpaRepository <Category, Long> {
    Category save(Category category);
    List<Category> findAll();
    Optional<Category> findById(Long Id);
    void  deleteById (Long Id);

    Category findByCategoryName(String categoryName);
}
