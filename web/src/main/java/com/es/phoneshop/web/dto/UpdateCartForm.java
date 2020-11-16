package com.es.phoneshop.web.dto;

import javax.validation.Valid;
import java.util.List;

public class UpdateCartForm {

    @Valid
    private List<@Valid UpdateCartDto> updateCartList;

    public List<@Valid UpdateCartDto> getUpdateCartList() {
        return updateCartList;
    }

    public void setUpdateCartList(List<@Valid UpdateCartDto> updateCartList) {
        this.updateCartList = updateCartList;
    }
}
