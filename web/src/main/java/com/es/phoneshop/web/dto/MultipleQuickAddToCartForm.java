package com.es.phoneshop.web.dto;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

public class MultipleQuickAddToCartForm {

    @Valid
    private List<@Valid QuickAddToCartForm> addToCartFormList = new ArrayList<>();

    public List<@Valid QuickAddToCartForm> getAddToCartFormList() {
        return addToCartFormList;
    }

    public void setAddToCartFormList(List<@Valid QuickAddToCartForm> addToCartFormList) {
        this.addToCartFormList = addToCartFormList;
    }
}
