package com.es.phoneshop.web.controller.pages.admin;

import com.es.core.dao.OrderDao;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.annotation.Resource;

@Controller
@RequestMapping(value = "/admin/orders")
public class OrdersPageController {
    private static final String ORDERS = "orders";

    @Resource
    private OrderDao orderDao;

    @RequestMapping(method = RequestMethod.GET)
    public String getOrders(Model model) {
        model.addAttribute(ORDERS, orderDao.findAll());
        return "adminOrders";
    }
}
