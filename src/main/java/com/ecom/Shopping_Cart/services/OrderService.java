package com.ecom.Shopping_Cart.services;

import com.ecom.Shopping_Cart.model.OrderRequest;
import com.ecom.Shopping_Cart.model.Product;
import com.ecom.Shopping_Cart.model.ProductOrder;
import jakarta.mail.MessagingException;

import java.io.UnsupportedEncodingException;
import java.util.List;

public interface OrderService {

    public void saveOrder(Integer userid, OrderRequest orderRequest) throws MessagingException, UnsupportedEncodingException;

    public List<ProductOrder> getOrdersByUsers(int userId);

    public ProductOrder updateOrderStatus(Integer id, String status);

    public List<ProductOrder> getAllOrders();

    public ProductOrder getOrdersByOrderId(String odrderId);
}
