package com.es.phoneshop.web.controller;

import com.es.core.cart.CartJsonResponse;
import com.es.core.cart.CartService;
import com.es.core.model.exception.OutOfStockException;
import com.es.core.model.phone.PhoneToCart;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping(value="/ajaxCart")
public class AjaxCartController {
    @Resource
    private CartService cartService;

    @RequestMapping(method=RequestMethod.POST ,produces = "application/json")
    public CartJsonResponse addPhone(@ModelAttribute("phoneToCart") @Valid PhoneToCart phoneToCart, BindingResult result) throws OutOfStockException {
        CartJsonResponse response = new CartJsonResponse();

        if (result.hasErrors()) {
            Map<String, String> errors = result.getFieldErrors().stream()
                    .collect(Collectors.toMap(FieldError::getField, FieldError::getDefaultMessage));
            response.setValidated(false);
            response.setErrorMessages(errors);
        } else {
            cartService.addPhone(phoneToCart.getPhoneId(), phoneToCart.getQuantity());
            response.setValidated(true);
        }

        response.setTotalQuantity(cartService.getCart().getTotalQuantity());
        response.setTotalCost(cartService.getCart().getTotalCost());
        return response;
    }
}
