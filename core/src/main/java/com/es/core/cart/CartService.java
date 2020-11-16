package com.es.core.cart;

import com.es.core.exception.*;

public interface CartService {

    Cart getCart();

    void addPhone(Long phoneId, Long quantity) throws OutOfStockException;

    void update(Long phoneId, Long quantity) throws OutOfStockException;

    void remove(Long phoneId);
}
