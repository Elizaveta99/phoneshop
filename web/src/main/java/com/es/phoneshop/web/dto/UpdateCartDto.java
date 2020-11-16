package com.es.phoneshop.web.dto;

import com.es.core.model.phone.Phone;

import javax.validation.constraints.Pattern;

public class UpdateCartDto {
    private Phone phone;

    @Pattern(message = "wrong input", regexp="^0*[1-9][0-9]*$")
    private String quantity = "1";

    public UpdateCartDto() { }

    public UpdateCartDto(Phone phone, @Pattern(message = "wrong input", regexp = "^0*[1-9][0-9]*$") String quantity) {
        this.phone = phone;
        this.quantity = quantity;
    }

    public Phone getPhone() {
        return phone;
    }

    public void setPhone(Phone phone) {
        this.phone = phone;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }
}
