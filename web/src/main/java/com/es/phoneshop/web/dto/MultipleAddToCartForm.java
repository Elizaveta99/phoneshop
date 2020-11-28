package com.es.phoneshop.web.dto;

import javax.validation.Valid;
import java.util.List;

public class MultipleAddToCartForm {

    @Valid
    private List<@Valid AddToCartForm> addToCartFormList;

    public List<@Valid AddToCartForm> getAddToCartFormList() {
        return addToCartFormList;
    }

    public void setAddToCartFormList(List<@Valid AddToCartForm> addToCartFormList) {
        this.addToCartFormList = addToCartFormList;
    }
}
