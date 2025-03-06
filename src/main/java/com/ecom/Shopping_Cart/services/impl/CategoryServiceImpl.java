package com.ecom.Shopping_Cart.services.impl;

import java.util.List;

import org.hibernate.id.IntegralDataTypeHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.ecom.Shopping_Cart.model.Category;
import com.ecom.Shopping_Cart.repository.CategoryRepository;
import com.ecom.Shopping_Cart.services.CategoryService;
import org.springframework.util.ObjectUtils;

@Service
public class CategoryServiceImpl implements CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;

    @Override
    public Category saveCategory(Category category) {
        return categoryRepository.save(category);
    }

    @Override
    public List<Category> getAllCategory() {
        return categoryRepository.findAll();
    }

    @Override
    public boolean deleteCategory(int id) {
        Category category=categoryRepository.findById(id).orElse(null);
        if(!ObjectUtils.isEmpty(category)){
            categoryRepository.delete(category);
            return true;
        }
        return false;
    }

    @Override
    public Category getCategoryById(int id) {
        Category category = categoryRepository.findById(id).orElse(null);
        return category;
    }

    @Override
    public List<Category> getAllActiveCategory() {
         List<Category> categories = categoryRepository.findByIsActiveTrue();
         return categories;
    }

    @Override
    public Page<Category> getAllCategoryPagination(Integer pageNo, Integer pageSize) {
        Pageable pageable = PageRequest.of(pageNo, pageSize);
        return categoryRepository.findAll(pageable);
    }


    @Override
    public Boolean existCategory(String name) {
        return categoryRepository.existsByName(name);
    }

}