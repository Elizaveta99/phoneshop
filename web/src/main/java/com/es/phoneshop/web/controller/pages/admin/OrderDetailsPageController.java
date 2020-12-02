package com.es.phoneshop.web.controller.pages.admin;

import com.es.core.dao.OrderDao;
import com.es.core.enumeration.OrderStatus;
import com.es.core.model.order.Order;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.annotation.Resource;
import java.util.Optional;

@Controller
@RequestMapping(value = "/admin/orders")
public class OrderDetailsPageController {

    private static final String ORDER = "order";

    @Resource
    private OrderDao orderDao;

    @RequestMapping(method = RequestMethod.GET, value = "/{secureId}")
    public String getOrderDetails(@PathVariable String secureId, Model model) {
        model.addAttribute(ORDER, orderDao.getBySecureId(secureId));
        return "adminOrderDetails";
    }

    @RequestMapping(method = RequestMethod.POST, value = "/{secureId}")
    public String updateStatus(@RequestParam("status") String status, @PathVariable String secureId) {
        Order order = orderDao.getBySecureId(secureId);
        orderDao.updateStatus(order.getId(), Optional.of(status).map(OrderStatus::valueOf).orElse(null));
        return "redirect:/admin/orders/{secureId}";
    }
}
