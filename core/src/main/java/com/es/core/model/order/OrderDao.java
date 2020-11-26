package com.es.core.model.order;

import com.es.core.model.phone.Phone;

import java.util.List;

public interface OrderDao {
    Order getBySecureId(String key);
    void save(Order order);
    void updateStatus(Long orderId, OrderStatus orderStatus);
}
