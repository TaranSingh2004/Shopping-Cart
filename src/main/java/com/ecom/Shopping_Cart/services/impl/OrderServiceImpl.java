package com.ecom.Shopping_Cart.services.impl;

import com.ecom.Shopping_Cart.model.Cart;
import com.ecom.Shopping_Cart.model.OrderAddress;
import com.ecom.Shopping_Cart.model.OrderRequest;
import com.ecom.Shopping_Cart.model.ProductOrder;
import com.ecom.Shopping_Cart.repository.CartRepository;
import com.ecom.Shopping_Cart.repository.ProductOrderRepository;
import com.ecom.Shopping_Cart.services.OrderService;
import com.ecom.Shopping_Cart.util.OrderStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

    @Override
    public void saveOrder(Integer userid, OrderRequest orderRequest) {

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

            orderRepository.save(order);
        }
    }

    @Override
    public List<ProductOrder> getOrdersByUsers(int userId) {
        List<ProductOrder> orders = orderRepository.findByUserId(userId);
        return orders;
    }

    @Override
    public Boolean updateOrderStatus(Integer id, String status) {
        Optional<ProductOrder> findById = orderRepository.findById(id);
        if(findById.isPresent()){
            ProductOrder productOrder = findById.get();
            productOrder.setStatus(status);
            orderRepository.save(productOrder);
            return true;
        }
        return false;
    }
}
