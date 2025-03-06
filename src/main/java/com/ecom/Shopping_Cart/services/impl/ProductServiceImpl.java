package com.ecom.Shopping_Cart.services.impl;

import com.ecom.Shopping_Cart.model.Product;
import com.ecom.Shopping_Cart.repository.ProductRepository;
import com.ecom.Shopping_Cart.services.ProductService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.Map;

@Service
public class ProductServiceImpl implements ProductService {

    @Autowired
    private ProductRepository productRepository;

    @Override
    public Product saveProduct(Product product) {
        return productRepository.save(product);
    }

    @Override
    public List<Product> getAllProducts() {

        return productRepository.findAll();
    }

    @Override
    public boolean deleteProduct(int id) {
        Product product= productRepository.findById(id).orElse(null);
        if(!ObjectUtils.isEmpty(product)){
            productRepository.delete(product);
            return true;
        }
        return false;
    }

    @Override
    public Product getProductById(int id) {
        Product product = productRepository.findById(id).orElse(null);
        return product;
    }

    @Override
    public Product updateProduct(Product product, MultipartFile image) {
        Product dbProduct = getProductById(product.getId());
        String imageName = image.isEmpty() ? dbProduct.getImage() : image.getOriginalFilename();
        dbProduct.setTitle(product.getTitle());
        dbProduct.setDescription(product.getDescription());
        dbProduct.setCategory(product.getCategory());
        dbProduct.setPrice(product.getPrice());
        dbProduct.setStock(product.getStock());
        dbProduct.setImage(imageName);
        dbProduct.setDiscount(product.getDiscount());
        dbProduct.setIsActive(product.getIsActive());

        // 5 = 100*(5/100); 100-5=95
        Double discount = product.getPrice()*(product.getDiscount()/100.0);
        Double discountedPrice = product.getPrice()-discount;
        dbProduct.setDiscountPrice(discountedPrice);

        Product updateProduct = productRepository.save(dbProduct);
        if (!ObjectUtils.isEmpty(updateProduct)) {

            if (!image.isEmpty()) {
                try {
                    File saveFile = new ClassPathResource("static/img").getFile();

                    Path path = Paths.get(saveFile.getAbsolutePath() + File.separator + "category_img" + File.separator
                            + image.getOriginalFilename());

                    // System.out.println(path);
                    Files.copy(image.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
                } catch (Exception e){
                    e.printStackTrace();
                }
            }

            return product;
        }

        return null;
    }

    @Override
    public List<Product> getAllActiveProducts(String category) {
        List<Product> products=null;
        if(ObjectUtils.isEmpty(category)){
            products=productRepository.findByIsActiveTrue();
        } else {
            products=productRepository.findByCategory(category);
        }
        return products;
    }

    @Override
    public List<Product> searchProduct(String ch) {
        return productRepository.findByTitleContainingIgnoreCaseOrCategoryContainingIgnoreCase(ch, ch);
    }

    @Override
    public Page<Product> searchProductPagination(Integer pageNo, Integer pageSize, String ch) {
        Pageable pageable = PageRequest.of(pageNo, pageSize);
        return productRepository.findByTitleContainingIgnoreCaseOrCategoryContainingIgnoreCase(ch, ch, pageable);
    }

    @Override
    public Page<Product> getAllProductsPagination(Integer pageNo, Integer pageSize) {
        Pageable pageable = PageRequest.of(pageNo, pageSize);
        return productRepository.findAll(pageable);
    }


    @Override
    public Page<Product> getAllActiveProductPagination(Integer pageNo, Integer pageSize, String category) {

        Pageable pageable = PageRequest.of(pageNo, pageSize);
        Page<Product> pageProduct=null;
        if(ObjectUtils.isEmpty(category)){
            pageProduct=productRepository.findByIsActiveTrue(pageable);
        } else {
            pageProduct=productRepository.findByCategory(pageable, category);
        }
        return pageProduct;
    }


}
