package com.ecom.Shopping_Cart.services.impl;

import com.ecom.Shopping_Cart.model.Cart;
import com.ecom.Shopping_Cart.model.Product;
import com.ecom.Shopping_Cart.model.UserDtls;
import com.ecom.Shopping_Cart.repository.CartRepository;
import com.ecom.Shopping_Cart.repository.ProductRepository;
import com.ecom.Shopping_Cart.repository.UserRepository;
import com.ecom.Shopping_Cart.services.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.List;

@Service
public class CartServiceImpl implements CartService {

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ProductRepository productRepository;

    @Override
    public Cart saveCart(Integer productId, Integer userId) {
        UserDtls userDtls = userRepository.findById(userId).get();
        Product product = productRepository.findById(productId).get();

        Cart cartStatus = cartRepository.findByProductIdAndUserId(productId, userId);
        Cart cart = null;

        if(ObjectUtils.isEmpty(cartStatus)){
            cart=new Cart();
            cart.setProduct(product);
            cart.setUser(userDtls);
            cart.setQuantity(1);
            cart.setTotalPrice(1*product.getDiscountPrice());
        }else {
            cart=cartStatus;
            cart.setQuantity(cart.getQuantity()+1);
            cart.setTotalPrice(cart.getQuantity()*cart.getProduct().getDiscountPrice());
        }
        Cart saveCart = cartRepository.save(cart);
        return saveCart;
    }

    @Override
    public List<Cart> getCartByUser(Integer userId) {
        return List.of();
    }
}
