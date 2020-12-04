package com.es.core.dao;

import com.es.core.enumeration.OrderStatus;
import com.es.core.model.order.Order;

public interface OrderDao {
    Order getBySecureId(String key);
    void save(Order order);
    void updateStatus(Long orderId, OrderStatus orderStatus);
}
