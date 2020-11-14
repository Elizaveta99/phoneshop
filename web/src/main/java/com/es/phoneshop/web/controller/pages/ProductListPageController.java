package com.es.phoneshop.web.controller.pages;

import com.es.core.model.phone.PhoneDao;
import com.es.phoneshop.web.dto.PhoneToCart;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.annotation.Resource;
import java.util.Map;
import java.util.Objects;

@Controller
@RequestMapping (value = "/productList")
public class ProductListPageController {

    private static final String QUERY_PRODUCT = "queryProduct";
    private static final String SORT = "sort";
    private static final String ORDER = "order";
    private static final String PHONES = "phones";
    private static final String PHONE_TO_CART = "phoneToCart";
    private static final String DEFAULT_QUERY_PRODUCT = "";
    private static final String DEFAULT_SORT = "id";
    private static final String DEFAULT_ORDER = "asc";
    private static final int DEFAULT_OFFSET = 0;
    private static final int DEFAULT_LIMIT = 30;

    @Resource
    private PhoneDao phoneDao;

    @ModelAttribute
    public void phoneToCart(Model model) {
        model.addAttribute(PHONE_TO_CART, new PhoneToCart());
    }

    @RequestMapping(method = RequestMethod.GET)
    public String showProductList(@RequestParam(required = false) Map<String, String> params, Model model) {
        model.addAttribute(PHONES, phoneDao.findAll(DEFAULT_OFFSET, DEFAULT_LIMIT,
                Objects.toString(params.get(QUERY_PRODUCT), DEFAULT_QUERY_PRODUCT),
                Objects.toString(params.get(SORT), DEFAULT_SORT),
                Objects.toString(params.get(ORDER), DEFAULT_ORDER)));
        return "productList";
    }
}
