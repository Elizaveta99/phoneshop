package com.es.phoneshop.web.controller.pages;

import com.es.core.enumeration.OrderStatus;
import com.es.core.exception.OutOfStockException;
import com.es.core.model.order.Order;
import com.es.core.service.order.OrderService;
import com.es.phoneshop.web.dto.OrderDataForm;
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
import java.util.Map;
import java.util.stream.Collectors;

@Controller
@RequestMapping(value = "/order")
public class OrderPageController {
    private static final String ORDER = "order";
    private static final String ORDER_DATA_FORM = "orderDataForm";
    private static final String VALIDATION_ERRORS = "validationErrors";
    private static final String MESSAGE = "message";
    private static final String ERROR_MESSAGE = "Error occurred while placing order";

    @Resource
    private OrderService orderService;

    @ModelAttribute
    public void attachOrderDataForm(Model model) {
        model.addAttribute(ORDER_DATA_FORM, new OrderDataForm());
    }

    @RequestMapping(method = RequestMethod.GET)
    public String getOrder(Model model) {
        Order order = orderService.createOrder();
        model.addAttribute(ORDER, order);
        return "order";
    }

    @RequestMapping(method = RequestMethod.POST)
    public String placeOrder(@ModelAttribute @Valid OrderDataForm orderDataForm,
                             BindingResult result, Model model,
                             RedirectAttributes redirectAttributes) {
        Order order = orderService.createOrder();
        if (result.hasErrors()) {
            handleErrors(result, model);
            model.addAttribute(ORDER, order);
            return "order";
        }
        try {
            populateOrder(order, orderDataForm);
            orderService.placeOrder(order);
            redirectAttributes.addAttribute("secureId", order.getSecureId());
            return "redirect:/orderOverview/{secureId}";
        } catch (OutOfStockException | NoSuchFieldException e) {
            model.addAttribute(MESSAGE, e.getMessage());
            order = orderService.createOrder();
            model.addAttribute(ORDER, order);
            return "order";
        }
    }

    private void populateOrder(Order order, OrderDataForm orderDataForm) {
        order.setFirstName(orderDataForm.getFirstName());
        order.setLastName(orderDataForm.getLastName());
        order.setDeliveryAddress(orderDataForm.getDeliveryAddress());
        order.setContactPhoneNo(orderDataForm.getContactPhoneNo());
        order.setAdditionalInformation(orderDataForm.getAdditionalInformation());
        order.setStatus(OrderStatus.NEW);
    }

    private void handleErrors(BindingResult result, Model model) {
        Map<String, String> errors = result.getFieldErrors().stream()
                .collect(Collectors.toMap(FieldError::getField, FieldError::getDefaultMessage));
        model.addAttribute(VALIDATION_ERRORS, errors);
        model.addAttribute(MESSAGE, ERROR_MESSAGE);
    }
}
