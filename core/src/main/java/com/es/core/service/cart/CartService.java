package com.es.core.service.cart;

import com.es.core.exception.*;
import com.es.core.model.cart.Cart;

public interface CartService {

    Cart getCart();

    void addPhone(Long phoneId, Long quantity) throws OutOfStockException, ItemNotFoundException;

    void quickAddPhone(String model, Long quantity) throws OutOfStockException, ItemNotFoundException;

    void update(Long phoneId, Long quantity) throws OutOfStockException;

    void remove(Long phoneId);

    void clearCart();
}

