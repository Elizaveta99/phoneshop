package com.es.phoneshop.web.dto;

import javax.validation.constraints.Pattern;

public class QuickAddToCartForm {

    private String model = "";

    @Pattern(message = "wrong input", regexp="^0*[1-9][0-9]*$")
    private String quantity = "1";

    public QuickAddToCartForm() { }

    public QuickAddToCartForm(String model, @Pattern(message = "wrong input", regexp = "^0*[1-9][0-9]*$") String quantity) {
        this.model = model;
        this.quantity = quantity;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }
}
