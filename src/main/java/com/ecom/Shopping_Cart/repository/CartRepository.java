package com.ecom.Shopping_Cart.repository;

import com.ecom.Shopping_Cart.model.Cart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.http.client.support.InterceptingHttpAccessor;

public interface CartRepository extends JpaRepository<Cart, Integer> {

    public Cart findByProductIdAndUserId(Integer productId, Integer userId);
}
