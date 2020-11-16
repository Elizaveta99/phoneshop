package com.es.phoneshop.web.controller.pages;

import com.es.core.cart.CartService;
import com.es.core.exception.OutOfStockException;
import com.es.phoneshop.web.dto.UpdateCartDto;
import com.es.phoneshop.web.dto.UpdateCartForm;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
@RequestMapping(value = "/cart")
public class CartPageController {
    private static final String UPDATE_CART_FORM = "updateCartForm";
    private static final String ERRORS = "errors";
    private static final String VALID_ERRORS = "validErrors";
    private static final String MESSAGE = "message";
    private static final String OUT_OF_STOCK_ERROR = "Out of stock, max available %d";
    private static final String SUCCESS_MESSAGE = "Cart updated successfully";
    private static final String ERROR_MESSAGE = "Error occurred while updating cart";

    @Resource
    private CartService cartService;

    @ModelAttribute
    public void setUpdateCartForm(Model model) {
        UpdateCartForm updateCartForm = new UpdateCartForm();
        updateCartForm.setUpdateCartList(cartService.getCart().getItems().stream()
                .map(cartItem -> new UpdateCartDto(cartItem.getPhone(), cartItem.getQuantity().toString()))
                .collect(Collectors.toList()));

        model.addAttribute(UPDATE_CART_FORM, updateCartForm);
    }

    @RequestMapping(method = RequestMethod.GET)
    public String getCart(Model model) {
        setUpdateCartForm(model);
        return "cart";
    }

    @RequestMapping(method = RequestMethod.POST)
    public String updateCart(@ModelAttribute @Valid UpdateCartForm updateCartForm, BindingResult result, Model model) {
        setUpdateCartForm(model);
        if (result.hasErrors()) {
            return handleErrors(result, model, updateCartForm);
        }
        model.addAttribute(MESSAGE, SUCCESS_MESSAGE);
        Map<Long, String> errors = new HashMap<>();
        for (UpdateCartDto updateCartDto: updateCartForm.getUpdateCartList()) {
            try {
                cartService.update(updateCartDto.getPhone().getId(), Long.parseLong(updateCartDto.getQuantity()));
            } catch (OutOfStockException e) {
                errors.put(e.getPhone().getId(), String.format(OUT_OF_STOCK_ERROR, e.getStockAvailable()));
                model.addAttribute(MESSAGE, ERROR_MESSAGE);
            }
        }
        model.addAttribute(ERRORS, errors);
        setUpdateCartForm(model);
        return "cart";
    }

    @RequestMapping(method = RequestMethod.DELETE, value = "/delete/{id}")
    public String deletePhone(@PathVariable String id, Model model) {
        cartService.remove(Long.parseLong(id));
        setUpdateCartForm(model);
        return "cart";
    }

    private String handleErrors(BindingResult result, Model model, UpdateCartForm updateCartForm) {
        Map<String, String> errors = result.getFieldErrors().stream()
                .collect(Collectors.toMap(FieldError::getField, FieldError::getDefaultMessage));
        model.addAttribute(VALID_ERRORS, errors);
        model.addAttribute(MESSAGE, ERROR_MESSAGE);
        return "cart";
    }
}
