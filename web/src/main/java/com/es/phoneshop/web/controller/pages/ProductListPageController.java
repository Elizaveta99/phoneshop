package com.es.phoneshop.web.controller.pages;

import com.es.core.model.phone.JdbcPhoneDao;
import com.es.core.model.phone.PhoneDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.annotation.Resource;
import javax.sql.DataSource;

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

    @RequestMapping(method = RequestMethod.GET)
    public String showProductList(Model model) {
        model.addAttribute("phones", phoneDao.findAll(0, 10));
        return "productList";
    }
}
