package com.ecom.Shopping_Cart.services;

import com.ecom.Shopping_Cart.model.Product;

import java.util.List;

public interface ProductService {

    public Product saveProduct(Product product);

    public List<Product> getAllProducts();

    public boolean deleteProduct(int id);
}
