package com.es.core.order;

import com.es.core.exception.OutOfStockException;
import com.es.core.model.order.Order;

public interface OrderService {
    Order createOrder();
    void placeOrder(Order order) throws OutOfStockException;
}
