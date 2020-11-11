package com.es.core.model.phone;

import org.springframework.format.annotation.NumberFormat;

import javax.validation.constraints.Min;

public class PhoneToCart {
    private static final Long DEFAULT_QUANTITY = 1l;

    private Long phoneId;

    @NumberFormat(style = NumberFormat.Style.NUMBER)
    @Min(value = 0, message = "wrong input")
    private Long quantity;

    public PhoneToCart() {
        this.quantity = DEFAULT_QUANTITY;
    }

    public PhoneToCart(Long phoneId, @Min(value = 0, message = "wrong input") Long quantity) {
        this.phoneId = phoneId;
        this.quantity = quantity;
    }

    public Long getPhoneId() {
        return phoneId;
    }

    public void setPhoneId(Long id) {
        this.phoneId = id;
    }

    public Long getQuantity() {
        return quantity;
    }

    public void setQuantity(Long quantity) {
        this.quantity = quantity;
    }
}
