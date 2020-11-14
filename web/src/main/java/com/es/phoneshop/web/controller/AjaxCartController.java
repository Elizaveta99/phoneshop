package com.es.phoneshop.web.controller;

import com.es.core.exception.*;
import com.es.core.cart.CartService;
import com.es.phoneshop.web.dto.CartJsonResponse;
import com.es.phoneshop.web.dto.PhoneToCart;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@RestController
@RequestMapping(value = "/ajaxCart")
public class AjaxCartController {
    private static final String OUT_OF_STOCK_ERROR = "Out of stock, max available %d";

    @Resource
    private CartService cartService;

    @RequestMapping(method = RequestMethod.GET, produces = "application/json")
    public CartJsonResponse showCart() {
        CartJsonResponse response = new CartJsonResponse();
        response.setTotalQuantity(cartService.getCart().getTotalQuantity());
        response.setTotalCost(cartService.getCart().getTotalCost());
        return response;
    }

    @RequestMapping(method = RequestMethod.POST, produces = "application/json", consumes = "application/json")
    public CartJsonResponse addPhone(@RequestBody @Valid PhoneToCart phoneToCart, BindingResult result) {
        CartJsonResponse response = new CartJsonResponse();

        if (result.hasErrors()) {
            Map<String, String> errors = result.getFieldErrors().stream()
                    .map(DefaultMessageSourceResolvable::getDefaultMessage)
                    .collect(Collectors.toMap(val -> phoneToCart.getPhoneId().toString(), Function.identity()));
            response.setErrorMessages(errors);
            return response;
        }
        try {
            cartService.addPhone(phoneToCart.getPhoneId(), Long.valueOf(phoneToCart.getQuantity()));
        } catch (OutOfStockException e) {
            Map<String, String> errors = new HashMap<String, String> () {{
                    put(phoneToCart.getPhoneId().toString(), String.format(OUT_OF_STOCK_ERROR, e.getStockAvailable()));
            }};
            response.setErrorMessages(errors);
            return response;
        }

        response.setErrorMessages(new HashMap<>());
        response.setTotalQuantity(cartService.getCart().getTotalQuantity());
        response.setTotalCost(cartService.getCart().getTotalCost());
        return response;
    }
}
