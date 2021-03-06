package com.es.core.model.cart;

import com.es.core.model.phone.Phone;

import java.io.Serializable;

public class CartItem implements Serializable {

    private Phone phone;
    private Long quantity;

    public CartItem(Phone phone, Long quantity) {
        this.phone = phone;
        this.quantity = quantity;
    }

    @Override
    public String toString() {
        return phone.getId() + ": " + quantity;
    }

    public Phone getPhone() {
        return phone;
    }

    public Long getQuantity() {
        return quantity;
    }

    public void setQuantity(Long quantity) {
        this.quantity = quantity;
    }
}
