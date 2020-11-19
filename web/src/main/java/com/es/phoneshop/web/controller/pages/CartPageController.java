package com.es.phoneshop.web.controller.pages;

import com.es.core.cart.CartItem;
import com.es.core.cart.CartService;
import com.es.core.exception.ItemNotFoundException;
import com.es.core.exception.OutOfStockException;
import com.es.core.model.phone.Phone;
import com.es.phoneshop.web.dto.AddToCartForm;
import com.es.phoneshop.web.dto.MultipleAddToCartForm;
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
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
@RequestMapping(value = "/cart")
public class CartPageController {
    private static final String MULTIPLE_ADD_TO_CART_FORM = "multipleAddToCartForm";
    private static final String PHONES = "phones";
    private static final String ERRORS = "errors";
    private static final String VALIDATION_ERRORS = "validationErrors";
    private static final String MESSAGE = "message";
    private static final String OUT_OF_STOCK_ERROR = "Out of stock, max available %d";
    private static final String SUCCESS_MESSAGE = "Cart updated successfully";
    private static final String ERROR_MESSAGE = "Error occurred while updating cart";

    @Resource
    private CartService cartService;

    private List<Phone> getPhonesFromCart() {
        return cartService.getCart().getItems().stream()
                .map(CartItem::getPhone)
                .collect(Collectors.toList());
    }

    private void attachMultipleAddToCartForm(Model model) {
        MultipleAddToCartForm multipleAddToCartForm = new MultipleAddToCartForm();
        multipleAddToCartForm.setAddToCartFormList(cartService.getCart().getItems().stream()
                .map(cartItem -> new AddToCartForm(cartItem.getPhone().getId(), cartItem.getQuantity().toString()))
                .collect(Collectors.toList()));
        model.addAttribute(MULTIPLE_ADD_TO_CART_FORM, multipleAddToCartForm);
        model.addAttribute(PHONES, getPhonesFromCart());
    }

    @RequestMapping(method = RequestMethod.GET)
    public String getCart(Model model) {
        attachMultipleAddToCartForm(model);
        return "cart";
    }

    @RequestMapping(method = RequestMethod.POST)
    public String updateCart(@ModelAttribute @Valid MultipleAddToCartForm multipleAddToCartForm, BindingResult result, Model model) {
        model.addAttribute(PHONES, getPhonesFromCart());
        if (result.hasErrors()) {
            handleErrors(result, model);
            return "cart";
        }
        model.addAttribute(MESSAGE, SUCCESS_MESSAGE);
        Map<Long, String> errors = new HashMap<>();
        if (multipleAddToCartForm.getAddToCartFormList() != null) {
            for (AddToCartForm addToCartForm : multipleAddToCartForm.getAddToCartFormList()) {
                try {
                    cartService.update(addToCartForm.getPhoneId(), Long.parseLong(addToCartForm.getQuantity()));
                } catch (OutOfStockException e) {
                    errors.put(e.getPhone().getId(), String.format(OUT_OF_STOCK_ERROR, e.getStockAvailable()));
                    model.addAttribute(MESSAGE, ERROR_MESSAGE);
                }
            }
            model.addAttribute(ERRORS, errors);
        }
        return "cart";
    }

    @RequestMapping(method = RequestMethod.DELETE, value = "/delete/{id}")
    public String deletePhone(@PathVariable String id, Model model) {
        try {
            cartService.remove(Long.parseLong(id));
        } catch (NumberFormatException e) {
            throw new ItemNotFoundException();
        }
        attachMultipleAddToCartForm(model);
        return "cart";
    }

    private void handleErrors(BindingResult result, Model model) {
        Map<String, String> errors = result.getFieldErrors().stream()
                .collect(Collectors.toMap(FieldError::getField, FieldError::getDefaultMessage));
        model.addAttribute(VALIDATION_ERRORS, errors);
        model.addAttribute(MESSAGE, ERROR_MESSAGE);
    }
}
