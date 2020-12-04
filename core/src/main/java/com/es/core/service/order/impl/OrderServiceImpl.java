package com.es.core.service.order.impl;

import com.es.core.dao.OrderDao;
import com.es.core.dao.PhoneDao;
import com.es.core.exception.OutOfStockException;
import com.es.core.model.order.Order;
import com.es.core.model.order.OrderItem;
import com.es.core.service.cart.CartService;
import com.es.core.service.order.OrderService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class OrderServiceImpl implements OrderService {

    private static final String OUT_OF_STOCK_ERROR = "Error, some products are out of stock";
    private static final String EMPTY_ORDER_ERROR = "Error, you are trying to place empty order";

    @Resource
    private OrderDao orderDao;

    @Resource
    private PhoneDao phoneDao;

    @Resource
    private CartService cartService;

    @Value("${delivery.price}")
    private BigDecimal deliveryPrice;

    @Override
    public Order createOrder() {
        Order order = new Order();
        order.setOrderItems(cartService.getCart().getItems().stream()
                .map(cartItem -> new OrderItem(order.getSecureId(), cartItem.getPhone(), cartItem.getQuantity()))
                .collect(Collectors.toList()));

        order.setSubtotal(cartService.getCart().getTotalCost());
        order.setDeliveryPrice(deliveryPrice);
        order.setTotalPrice(order.getSubtotal().add(order.getDeliveryPrice()));
        return order;
    }

    @Override
    public synchronized void placeOrder(Order order) throws OutOfStockException, NoSuchFieldException {
        order.setSecureId(UUID.randomUUID().toString());

        if (order.getOrderItems().isEmpty()) {
            throw new NoSuchFieldException(EMPTY_ORDER_ERROR);
        }

        checkQuantities(order);

        orderDao.save(order);
        order.getOrderItems().forEach(orderItem -> phoneDao.updateStock(orderItem.getPhone().getId(), (int) (phoneDao.getStock(orderItem.getPhone().getId()) - orderItem.getQuantity())));

        cartService.clearCart();
    }

    private void checkQuantities(Order order) throws OutOfStockException {
        boolean isOutOfStock = false;
        for (OrderItem orderItem: order.getOrderItems()) {
            if (phoneDao.getStock(orderItem.getPhone().getId()) < orderItem.getQuantity()) {
                cartService.remove(orderItem.getPhone().getId());
                isOutOfStock = true;
            }
        }
        if (isOutOfStock) {
            throw new OutOfStockException(OUT_OF_STOCK_ERROR);
        }
    }
}
