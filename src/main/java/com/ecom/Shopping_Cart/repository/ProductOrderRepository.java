package com.ecom.Shopping_Cart.repository;

import com.ecom.Shopping_Cart.model.ProductOrder;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductOrderRepository extends JpaRepository<ProductOrder, Integer> {
    List<ProductOrder> findByUserId(int userId);
}
