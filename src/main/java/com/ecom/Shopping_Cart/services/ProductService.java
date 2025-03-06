package com.ecom.Shopping_Cart.services;

import com.ecom.Shopping_Cart.model.Product;
import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface ProductService {

    public Product saveProduct(Product product);

    public List<Product> getAllProducts();

    public boolean deleteProduct(int id);

    public Product getProductById(int id);

    public Product updateProduct(Product product,  MultipartFile file);

    public List<Product> getAllActiveProducts(String category);

    public List<Product> searchProduct(String ch);

    public Page<Product> getAllActiveProductPagination(Integer pageNo, Integer pageSize, String category);

    public Page<Product> searchProductPagination(Integer pageNo, Integer pageSize,String ch);

    public Page<Product> getAllProductsPagination(Integer pageNo, Integer pageSize);

}
