package com.es.phoneshop.web.controller.pages;

import com.es.core.dao.OrderDao;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.annotation.Resource;

@Controller
@RequestMapping(value = "/orderOverview")
public class OrderOverviewPageController {

    private static final String ORDER = "order";

    @Resource
    private OrderDao orderDao;

    @RequestMapping(method = RequestMethod.GET, value = "/{secureId}")
    public String getOrderOverview(@PathVariable String secureId, Model model) {
        model.addAttribute(ORDER, orderDao.getBySecureId(secureId));
        return "orderOverview";
    }
}
