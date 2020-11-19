package com.es.phoneshop.web.dto;

import javax.validation.constraints.Pattern;

public class AddToCartForm {

    private Long phoneId;

    @Pattern(message = "wrong input", regexp="^0*[1-9][0-9]*$")
    private String quantity = "1";

    public AddToCartForm() { }

    public AddToCartForm(Long phoneId, @Pattern(message = "wrong input", regexp = "^0*[1-9][0-9]*$") String quantity) {
        this.phoneId = phoneId;
        this.quantity = quantity;
    }

    public Long getPhoneId() {
        return phoneId;
    }

    public void setPhoneId(Long id) {
        this.phoneId = id;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }
}
