package com.es.phoneshop.web.controller.pages;

import com.es.core.exception.ItemNotFoundException;
import com.es.core.exception.OutOfStockException;
import com.es.core.service.cart.CartService;
import com.es.phoneshop.web.dto.MultipleQuickAddToCartForm;
import com.es.phoneshop.web.dto.QuickAddToCartForm;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
@RequestMapping(value = "/quickOrder")
public class QuickOrderPageController {
    private static final String MULTIPLE_QUICK_ADD_TO_CART_FORM = "multipleQuickAddToCartForm";
    private static final String ERRORS = "errors";
    private static final String VALIDATION_ERRORS = "validationErrors";
    private static final String MESSAGE = "message";
    private static final String OUT_OF_STOCK_ERROR = "Out of stock, max available %d";
    private static final String SUCCESS_ORDER_MESSAGE = "Order added successfully";
    private static final String ERROR_ORDER_MESSAGE = "Error occurred while adding to cart";
    private static final String ERROR_EMPTY_ORDER_MESSAGE = "Empty order can't be added";
    private static final Integer AMOUNT = 3;

    @Resource
    private CartService cartService;

    private MultipleQuickAddToCartForm getMultipleQuickAddToCartForm() {
        MultipleQuickAddToCartForm multipleQuickAddToCartForm = new MultipleQuickAddToCartForm();
        for (int amount = 0; amount < AMOUNT; amount++) {
            multipleQuickAddToCartForm.getAddToCartFormList().add(new QuickAddToCartForm());
        }
        return multipleQuickAddToCartForm;
    }

    @RequestMapping(method = RequestMethod.GET)
    public String showQuickOrder(Model model, @ModelAttribute(MESSAGE) String message) {
        model.addAttribute(MULTIPLE_QUICK_ADD_TO_CART_FORM, getMultipleQuickAddToCartForm());
        model.addAttribute(MESSAGE, message);
        return "quickOrder";
    }

    @RequestMapping(method = RequestMethod.POST)
    public String quickAddToCart(@ModelAttribute(MESSAGE) String message,
                             @ModelAttribute @Valid MultipleQuickAddToCartForm multipleQuickAddToCartForm,
                             BindingResult result, Model model,
                             RedirectAttributes redirectAttributes) throws ItemNotFoundException {
        if (multipleQuickAddToCartForm.getAddToCartFormList() == null) {
            model.addAttribute(MESSAGE, ERROR_EMPTY_ORDER_MESSAGE);
            return "quickOrder";
        }
        if (result.hasErrors()) {
            handleErrors(result, model);
            return "quickOrder";
        }
        Map<String, String> errors = new HashMap<>();
        for (QuickAddToCartForm quickAddToCartForm : multipleQuickAddToCartForm.getAddToCartFormList()) {
            try {
                cartService.quickAddPhone(quickAddToCartForm.getModel(), Long.parseLong(quickAddToCartForm.getQuantity()));
            } catch (OutOfStockException e) {
                errors.put(e.getPhone().getModel(), String.format(OUT_OF_STOCK_ERROR, e.getStockAvailable()));
                model.addAttribute(MESSAGE, ERROR_ORDER_MESSAGE);
            } catch (ItemNotFoundException e) {
                redirectAttributes.addFlashAttribute(MESSAGE, ERROR_ORDER_MESSAGE);
                return "redirect:/quickOrder";
            }
        }
        model.addAttribute(ERRORS, errors);
        model.addAttribute(MESSAGE, SUCCESS_ORDER_MESSAGE);
        return "quickOrder";
    }

    private void handleErrors(BindingResult result, Model model) {
        Map<String, String> errors = result.getFieldErrors().stream()
                .collect(Collectors.toMap(FieldError::getField, FieldError::getDefaultMessage));
        model.addAttribute(VALIDATION_ERRORS, errors);
        model.addAttribute(MESSAGE, ERROR_ORDER_MESSAGE);
    }
}
