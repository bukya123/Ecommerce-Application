package com.example.ecommercebackend.Services;

import com.example.ecommercebackend.ControllerAdivce.APIException;
import com.example.ecommercebackend.ControllerAdivce.ResourceNotFoundException;
import com.example.ecommercebackend.DTOs.CategoryRequestDTO;
import com.example.ecommercebackend.DTOs.CategoryResponseDTO;
import com.example.ecommercebackend.Modules.Category;
import com.example.ecommercebackend.Repositories.CategoryRepo;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import org.springframework.data.domain.Pageable;
import java.net.http.HttpHeaders;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class CategoryServiceImp implements CategoryService {
    @Autowired
    private CategoryRepo categoryRepo;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public CategoryResponseDTO getCategories( Integer pageNo, Integer pageSize,String sortBy,String sortOrder) {
        Sort sortByAndOrder = sortOrder.equalsIgnoreCase("asc")
                ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();
        Pageable pageDetails =  PageRequest.of(pageNo, pageSize,sortByAndOrder);
        Page<Category> categoryPage = categoryRepo.findAll(pageDetails);
        List<Category> s = categoryPage.getContent();
        if (s.size() == 0) {
            throw new APIException("No category created till now.");
        }
        CategoryResponseDTO categoryResponseDTO = new CategoryResponseDTO();
        categoryResponseDTO.setContent(new ArrayList<>());
        for(Category c : s) {
            CategoryRequestDTO categoryRequestDTO = new CategoryRequestDTO();
            categoryRequestDTO.setCategoryId(c.getCategoryId());
            categoryRequestDTO.setCategoryName(c.getCategoryName());
            categoryResponseDTO.getContent().add(categoryRequestDTO);

        }
        categoryResponseDTO.setPageNumber(pageNo);
        categoryResponseDTO.setPageSize(pageSize);
        categoryResponseDTO.setTotalPages(categoryPage.getTotalPages());
        categoryResponseDTO.setTotalElements(categoryPage.getTotalElements());
        categoryResponseDTO.setLastPage(categoryPage.isLast());
        return categoryResponseDTO;
    }

    @Override
    public CategoryRequestDTO postCategory(CategoryRequestDTO categoryRequestDTO) {
        Category category = modelMapper.map(categoryRequestDTO,Category.class);
        Category categoryFromDb=categoryRepo.findByCategoryName(categoryRequestDTO.getCategoryName());
        if(categoryFromDb!=null) {
            throw new APIException("Category with the name "+category.getCategoryName()+" already exists !!!");
        }
        Category savedCategory=categoryRepo.save(category);
        return modelMapper.map(savedCategory,CategoryRequestDTO.class);
    }

    @Override
    public CategoryRequestDTO putCategory(CategoryRequestDTO categoryRequestDTO, Long categoryId) {
        Optional<Category> optionalCategory = categoryRepo.findById(categoryId);
        if (!optionalCategory.isPresent()) {
            throw new ResourceNotFoundException("Category","categoryId",categoryId);
        }
        Category category = modelMapper.map(categoryRequestDTO,Category.class);
        category.setCategoryId(categoryId);
        Category savedCategory=categoryRepo.save(category);
        return modelMapper.map(savedCategory,CategoryRequestDTO.class);
    }



    @Override
    public CategoryRequestDTO deleteCategory(Long categoryId) {
        Optional<Category> optionalCategory=categoryRepo.findById(categoryId);
        if(!optionalCategory.isPresent()){
            throw new ResourceNotFoundException("Category","categoryId",categoryId);
        }
        Category category=optionalCategory.get();
        categoryRepo.delete(category);
        return modelMapper.map(category,CategoryRequestDTO.class);

    }

}
