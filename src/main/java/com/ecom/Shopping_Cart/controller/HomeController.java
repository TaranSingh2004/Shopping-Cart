package com.ecom.Shopping_Cart.controller;

import com.ecom.Shopping_Cart.model.Category;
import com.ecom.Shopping_Cart.model.Product;
import com.ecom.Shopping_Cart.repository.ProductRepository;
import com.ecom.Shopping_Cart.services.CategoryService;
import com.ecom.Shopping_Cart.services.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class HomeController {

    @Autowired
    CategoryService categoryService;

    @Autowired
    ProductService productService;

    @GetMapping("/")
    public String index(){
        return "index";
    }

    @GetMapping("/login")
    public String login(){
        return "login";
    }

    @GetMapping("/register")
    public String register(){
        return "register";
    }

    @GetMapping("/products")
    public String products(Model m, @RequestParam(value = "category", defaultValue = "") String category){
        List<Category> categories = categoryService.getAllActiveCategory();
        List<Product> products = productService.getAllActiveProducts(category);
        m.addAttribute("categories", categories);
        m.addAttribute("products", products);
        m.addAttribute("paramValue", category);
        return "product";
    }

    @GetMapping("/product/{id}")
    public String product(@PathVariable int id, Model m){
        Product productById = productService.getProductById(id);
        m.addAttribute("product", productById);
        return "view_product";
    }

}
