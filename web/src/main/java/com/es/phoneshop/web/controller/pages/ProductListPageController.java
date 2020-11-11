package com.es.phoneshop.web.controller.pages;

import com.es.core.model.phone.JdbcPhoneDao;
import com.es.core.model.phone.PhoneDao;
import com.es.core.model.phone.PhoneToCart;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.annotation.Resource;
import javax.sql.DataSource;
import java.util.Map;
import java.util.Objects;

@Controller
@RequestMapping (value = "/productList")
public class ProductListPageController {

    @Autowired
    public void setPhoneDao(JdbcPhoneDao phoneDao) {
        this.phoneDao = phoneDao;
    }

    private PhoneDao phoneDao;

    @Resource
    private DataSource dataSource;

    @ModelAttribute
    public void phoneToCart(Model model) {
        model.addAttribute("phoneToCart", new PhoneToCart());
    }

    @RequestMapping(method = RequestMethod.GET)
    public String showProductList(@RequestParam(required = false) Map<String, String> params, Model model) {
        model.addAttribute("phones", phoneDao.findAll(0, 30,
                Objects.nonNull(params.get("queryProduct")) ? params.get("queryProduct") : "",
                Objects.nonNull(params.get("sort")) ? params.get("sort") : "",
                Objects.nonNull(params.get("order")) ? params.get("order") : ""));
        return "productList";
    }
}
