package com.es.phoneshop.web.controller.pages;

import com.es.core.exception.ItemNotFoundException;
import com.es.core.exception.OutOfStockException;
import com.es.core.service.cart.CartService;
import com.es.phoneshop.web.dto.MultipleQuickAddToCartForm;
import com.es.phoneshop.web.dto.QuickAddToCartForm;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping(value = "/quickOrder")
public class QuickOrderPageController {
    private static final String MULTIPLE_QUICK_ADD_TO_CART_FORM = "multipleQuickAddToCartForm";
    private static final String SUCCESS_ITEMS = "successItems";
    private static final String SUCCESS_ITEMS_MESSAGE = "%s product(s) added successfully";
    private static final String ERROR_MESSAGE = "errorMessage";
    private static final String SUCCESS_MESSAGE = "successMessage";
    private static final String OUT_OF_STOCK_ERROR = "Out of stock, max available %d";
    private static final String ERROR_ORDER_MESSAGE = "Error occurred while adding to cart";
    private static final String ERROR_EMPTY_ORDER_MESSAGE = "Empty order can't be added";
    private static final String ERROR_NOT_EXISTING = "Product not found";
    private static final String MODEL_FIELD = "addToCartFormList[%d].model";
    private static final String QUANTITY_FIELD = "addToCartFormList[%d].quantity";
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
    public String showQuickOrder(Model model) {
        model.addAttribute(MULTIPLE_QUICK_ADD_TO_CART_FORM, getMultipleQuickAddToCartForm());
        return "quickOrder";
    }

    @RequestMapping(method = RequestMethod.POST)
    public String quickAddToCart(@ModelAttribute @Valid MultipleQuickAddToCartForm multipleQuickAddToCartForm,
                             BindingResult result, Model model) throws ItemNotFoundException {
        if (multipleQuickAddToCartForm.getAddToCartFormList() == null) {
            model.addAttribute(ERROR_MESSAGE, ERROR_EMPTY_ORDER_MESSAGE);
            return "quickOrder";
        }
        List<String> successItems = new ArrayList<>();
        for (int i = 0; i < multipleQuickAddToCartForm.getAddToCartFormList().size(); i++) {
            QuickAddToCartForm quickAddToCartForm = multipleQuickAddToCartForm.getAddToCartFormList().get(i);
            if (!result.hasFieldErrors(String.format(QUANTITY_FIELD, i))) {
                try {
                    cartService.quickAddPhone(quickAddToCartForm.getModel(), Long.parseLong(quickAddToCartForm.getQuantity()));
                    successItems.add(quickAddToCartForm.getModel());
                } catch (OutOfStockException e) {
                    result.rejectValue(String.format(QUANTITY_FIELD, i), "", String.format(OUT_OF_STOCK_ERROR, e.getStockAvailable()));
                    model.addAttribute(ERROR_MESSAGE, ERROR_ORDER_MESSAGE);
                } catch (ItemNotFoundException e) {
                    result.rejectValue(String.format(MODEL_FIELD, i), "", ERROR_NOT_EXISTING);
                    model.addAttribute(ERROR_MESSAGE, ERROR_ORDER_MESSAGE);
                }
            }
        }
        if (result.hasErrors()) {
            model.addAttribute(ERROR_MESSAGE, ERROR_ORDER_MESSAGE);
        }
        if (!successItems.isEmpty()) {
            model.addAttribute(SUCCESS_ITEMS, successItems);
            model.addAttribute(SUCCESS_MESSAGE, String.format(SUCCESS_ITEMS_MESSAGE, StringUtils.join(successItems, ",")));
        }
        return "quickOrder";
    }
}
