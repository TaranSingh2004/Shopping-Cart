package com.ecom.Shopping_Cart.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ecom.Shopping_Cart.model.Category;

public interface CategoryRepository extends JpaRepository<Category, Integer> {

    public Boolean existsByName(String name);

}