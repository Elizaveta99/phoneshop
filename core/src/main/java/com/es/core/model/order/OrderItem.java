package com.es.core.model.order;

import com.es.core.model.phone.Phone;

public class OrderItem {

    private String orderId;
    private Phone phone;
    private Long quantity;

    public OrderItem() { }

    public OrderItem(String orderId, Phone phone, Long quantity) {
        this.orderId = orderId;
        this.phone = phone;
        this.quantity = quantity;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public Phone getPhone() {
        return phone;
    }

    public void setPhone(final Phone phone) {
        this.phone = phone;
    }

    public Long getQuantity() {
        return quantity;
    }

    public void setQuantity(final Long quantity) {
        this.quantity = quantity;
    }
}
