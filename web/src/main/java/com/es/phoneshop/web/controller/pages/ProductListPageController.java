package com.es.phoneshop.web.controller.pages;

import com.es.core.dao.PhoneDao;
import com.es.core.enumeration.sortenum.SortField;
import com.es.core.enumeration.sortenum.SortOrder;
import com.es.phoneshop.web.dto.AddToCartForm;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.annotation.Resource;
import java.util.Optional;

@Controller
@RequestMapping (value = "/productList")
public class ProductListPageController {

    private static final String PHONES = "phones";
    private static final String ADD_TO_CART_FORM = "addToCartForm";
    private static final String DEFAULT_QUERY_PRODUCT = "";
    private static final SortField DEFAULT_SORT = SortField.ID;
    private static final SortOrder DEFAULT_ORDER = SortOrder.ASC;
    private static final int DEFAULT_OFFSET = 0;
    private static final int DEFAULT_LIMIT = 30;

    @Resource
    private PhoneDao phoneDao;

    @ModelAttribute
    public void attachAddToCartForm(Model model) {
        model.addAttribute(ADD_TO_CART_FORM, new AddToCartForm());
    }

    @RequestMapping(method = RequestMethod.GET)
    public String showProductList(@RequestParam(required = false) SortField sort,
                                  @RequestParam(required = false) SortOrder order,
                                  @RequestParam(required = false) String queryProduct, Model model) {
        model.addAttribute(PHONES, phoneDao.findAll(DEFAULT_OFFSET, DEFAULT_LIMIT,
                Optional.ofNullable(queryProduct).orElse(DEFAULT_QUERY_PRODUCT),
                Optional.ofNullable(sort).orElse(DEFAULT_SORT),
                Optional.ofNullable(order).orElse(DEFAULT_ORDER)));
        return "productList";
    }
}
