package com.es.phoneshop.web.controller.pages;

import com.es.core.model.phone.PhoneDao;
import com.es.phoneshop.web.dto.AddToCartForm;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@Controller
@RequestMapping(value = "/productDetails")
public class ProductDetailsPageController {
    private static final String PHONE = "phone";
    private static final String ADD_TO_CART_FORM = "addToCartForm";

    @Resource
    private PhoneDao phoneDao;

    @ModelAttribute
    public void phoneToCart(Model model) {
        model.addAttribute(ADD_TO_CART_FORM, new AddToCartForm());
    }

    @RequestMapping(method = RequestMethod.GET, value = "/{key}")
    public String showProductDetails(@PathVariable Long key, Model model) {
        model.addAttribute(PHONE, phoneDao.get(key));
        return "productDetails";
    }
}
