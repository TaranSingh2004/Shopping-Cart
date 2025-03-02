package com.ecom.Shopping_Cart.services;

import com.ecom.Shopping_Cart.model.OrderRequest;
import com.ecom.Shopping_Cart.model.Product;
import com.ecom.Shopping_Cart.model.ProductOrder;

import java.util.List;

public interface OrderService {

    public void saveOrder(Integer userid, OrderRequest orderRequest);

    public List<ProductOrder> getOrdersByUsers(int userId);

    public Boolean updateOrderStatus(Integer id, String status);
}
