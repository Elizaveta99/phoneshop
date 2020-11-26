package com.es.phoneshop.web.controller.pages;

import com.es.core.cart.CartService;
import com.es.core.exception.OutOfStockException;
import com.es.core.model.order.Order;
import com.es.core.model.order.OrderStatus;
import com.es.core.order.OrderService;
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
    private static final String OUT_OF_STOCK_ERROR = "Out of stock, max available %d";
    private static final String ERROR_MESSAGE = "Error occurred while placing order";

    @Resource
    private OrderService orderService;

    @Resource
    private CartService cartService;

    @Resource
    private Order order;

    @ModelAttribute
    public void attachOrderDataForm(Model model) {
        model.addAttribute(ORDER_DATA_FORM, new OrderDataForm());
    }

    @RequestMapping(method = RequestMethod.GET)
    public String getOrder(Model model) {
        order = orderService.createOrder();
        model.addAttribute(ORDER, order);
        return "order";
    }

    @RequestMapping(method = RequestMethod.POST)
    public String placeOrder(@ModelAttribute @Valid OrderDataForm orderDataForm,
                             BindingResult result, Model model,
                             RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            handleErrors(result, model);
            model.addAttribute(ORDER, order);
            return "order";
        }
        try {
            populateOrder(orderDataForm);
            orderService.placeOrder(order);
            cartService.clearCart();
            redirectAttributes.addAttribute("secureId", order.getSecureId());
            return "redirect:/orderOverview/{secureId}";
        } catch (OutOfStockException e) {
            model.addAttribute(MESSAGE, String.format(OUT_OF_STOCK_ERROR, e.getStockAvailable()));
            order = orderService.createOrder();
            model.addAttribute(ORDER, order);
            return "order";
        }
    }

    private void populateOrder(OrderDataForm orderDataForm) {
        order.setFirstName(orderDataForm.getFirstName());
        order.setLastName(orderDataForm.getLastName());
        order.setDeliveryAddress(orderDataForm.getDeliveryAddress());
        order.setContactPhoneNo(orderDataForm.getContactPhoneNo());
        order.setAdditionalInformation(orderDataForm.getAdditionalInformation());
        order.setStatus(OrderStatus.NEW.toString());
    }

    private void handleErrors(BindingResult result, Model model) {
        Map<String, String> errors = result.getFieldErrors().stream()
                .collect(Collectors.toMap(FieldError::getField, FieldError::getDefaultMessage));
        model.addAttribute(VALIDATION_ERRORS, errors);
        model.addAttribute(MESSAGE, ERROR_MESSAGE);
    }
}
