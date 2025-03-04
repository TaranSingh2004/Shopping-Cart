package com.ecom.Shopping_Cart.services.impl;

import com.ecom.Shopping_Cart.model.*;
import com.ecom.Shopping_Cart.repository.CartRepository;
import com.ecom.Shopping_Cart.repository.ProductOrderRepository;
import com.ecom.Shopping_Cart.services.OrderService;
import com.ecom.Shopping_Cart.util.CommonUtil;
import com.ecom.Shopping_Cart.util.OrderStatus;
import jakarta.mail.MessagingException;
import org.apache.tomcat.util.http.parser.Cookie;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private ProductOrderRepository orderRepository;

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private CommonUtil commonUtil;

    @Override
    public void saveOrder(Integer userid, OrderRequest orderRequest) throws MessagingException, UnsupportedEncodingException {

        List<Cart> carts = cartRepository.findByUserId(userid);

        for(Cart cart: carts){
            ProductOrder order = new ProductOrder();
            order.setOrderId(UUID.randomUUID().toString());
            order.setOrderDate(LocalDate.now());
            order.setProduct(cart.getProduct());
            order.setPrice(cart.getProduct().getDiscountPrice());
            order.setQuantity(cart.getQuantity());
            order.setUser(cart.getUser());
            order.setStatus(OrderStatus.IN_PROGRESS.getName());
            order.setPaymentType(orderRequest.getPaymentType());

            OrderAddress address = new OrderAddress();
            address.setFirstName(orderRequest.getFirstName());
            address.setLastName(orderRequest.getLastName());
            address.setEmail(orderRequest.getEmail());
            address.setMobileNo(orderRequest.getMobileNo());
            address.setAddress(orderRequest.getAddress());
            address.setState(orderRequest.getState());
            address.setCity(orderRequest.getCity());
            address.setPincode(orderRequest.getPincode());

            order.setOrderAddress(address);

            ProductOrder saveOrder = orderRepository.save(order);

            commonUtil.sendMailForProductOrder(saveOrder, "success");
        }
    }

    @Override
    public List<ProductOrder> getOrdersByUsers(int userId) {
        List<ProductOrder> orders = orderRepository.findByUserId(userId);
        return orders;
    }

    @Override
    public ProductOrder updateOrderStatus(Integer id, String status) {
        Optional<ProductOrder> findById = orderRepository.findById(id);
        if(findById.isPresent()){
            ProductOrder productOrder = findById.get();
            productOrder.setStatus(status);
            ProductOrder updateOrder = orderRepository.save(productOrder);
            return updateOrder;
        }
        return null;
    }

    @Override
    public List<ProductOrder> getAllOrders() {
        return orderRepository.findAll();
    }

    @Override
    public ProductOrder getOrdersByOrderId(String orderId) {
        return orderRepository.findByOrderId(orderId);
    }
}
