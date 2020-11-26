package com.es.core.order;

import com.es.core.cart.Cart;
import com.es.core.cart.HttpSessionCartService;
import com.es.core.exception.OutOfStockException;
import com.es.core.model.order.Order;
import com.es.core.model.order.OrderDao;
import com.es.core.model.order.OrderItem;
import com.es.core.model.phone.PhoneDao;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class OrderServiceImpl implements OrderService {

    @Resource
    private Cart cart;

    @Resource
    private Order order;

    @Resource
    private OrderDao orderDao;

    @Resource
    private PhoneDao phoneDao;

    @Resource
    private HttpSessionCartService cartService;

    @Override
    public Order createOrder() {
        order.setSecureId(UUID.randomUUID().toString());
        order.setOrderItems(cart.getItems().stream()
                .map(cartItem -> new OrderItem(order.getSecureId(), cartItem.getPhone(), cartItem.getQuantity()))
                .collect(Collectors.toList()));

        order.setSubtotal(cart.getTotalCost());
        order.setTotalPrice(order.getSubtotal().add(order.getDeliveryPrice()));
        return order;
    }

    @Override
    public void placeOrder(Order order) throws OutOfStockException {
        checkQuantities(order);

        orderDao.save(order);
        order.getOrderItems().forEach(orderItem -> phoneDao.updateStock(orderItem.getPhone().getId(), (int) (phoneDao.getStock(orderItem.getPhone().getId()) - orderItem.getQuantity())));
    }

    private void checkQuantities(Order order) throws OutOfStockException {
        long requested = 0;
        long available = 0;
        for (OrderItem orderItem: order.getOrderItems()) {
            int stock = phoneDao.getStock(orderItem.getPhone().getId());
            if (stock < orderItem.getQuantity()) {
                cartService.remove(orderItem.getPhone().getId());
                requested += orderItem.getQuantity();
                available += stock;
            }
        }
        if (requested != 0) {
            throw new OutOfStockException(null, requested, available);
        }
    }
}
