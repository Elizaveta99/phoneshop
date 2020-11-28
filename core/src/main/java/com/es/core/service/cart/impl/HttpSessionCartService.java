package com.es.core.service.cart.impl;

import com.es.core.exception.ItemNotFoundException;
import com.es.core.exception.OutOfStockException;
import com.es.core.model.cart.Cart;
import com.es.core.model.cart.CartItem;
import com.es.core.model.phone.Phone;
import com.es.core.dao.PhoneDao;
import com.es.core.service.cart.CartService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Optional;

@Service
public class HttpSessionCartService implements CartService {

    @Resource
    private PhoneDao phoneDao;

    @Resource
    private Cart cart;

    @Override
    public synchronized Cart getCart() {
        return cart;
    }

    @Override
    public synchronized void addPhone(Long phoneId, Long quantity) throws OutOfStockException {
        Phone phone = phoneDao.get(phoneId);
        Optional<CartItem> cartItemOptional = getCartItemOptional(cart, phone);
        Long productsAmount = cartItemOptional.map(CartItem::getQuantity).orElse(0L);

        checkQuantity(productsAmount + quantity, phone);

        if (cartItemOptional.isPresent()) {
            cartItemOptional.get().setQuantity(productsAmount + quantity);
        } else {
            cart.getItems().add(new CartItem(phone, quantity));
        }
        recalculateCart(cart);
    }

    private Optional<CartItem> getCartItemOptional(Cart cart, Phone phone) {
        return cart.getItems().stream()
                .filter(c -> phone.getId().equals(c.getPhone().getId()))
                .findAny();
    }

    private void checkQuantity(Long newQuantity, Phone phone) throws OutOfStockException {
        int stock = phoneDao.getStock(phone.getId());
        if (stock < newQuantity) {
            throw new OutOfStockException(phone, newQuantity, (long)stock);
        }
    }

    private void recalculateCart(Cart cart) {
        cart.setTotalQuantity(cart.getItems().stream()
                .map(CartItem::getQuantity)
                .mapToLong(Long::longValue).sum());
        cart.setTotalCost(cart.getItems().stream()
                .map(cartItem -> new BigDecimal(cartItem.getQuantity()).multiply(cartItem.getPhone().getPrice()))
                .reduce(BigDecimal.ZERO, BigDecimal::add));
    }

    @Override
    public synchronized void update(Long phoneId, Long quantity) throws OutOfStockException {
        Phone phone = phoneDao.get(phoneId);
        Optional<CartItem> cartItemOptional = getCartItemOptional(cart, phone);
        checkQuantity(quantity, phone);
        cartItemOptional.ifPresentOrElse(cartItem -> cartItem.setQuantity(quantity),
                () -> { throw new ItemNotFoundException(); });
        recalculateCart(cart);
    }

    @Override
    public synchronized void remove(Long phoneId) {
        Phone product = phoneDao.get(phoneId);
        Optional<CartItem> cartItemOptional = getCartItemOptional(cart, product);
        cartItemOptional.ifPresentOrElse(cartItem -> cart.getItems().removeIf(cartItem1 -> phoneId.equals(cartItem1.getPhone().getId())),
                () -> { throw new ItemNotFoundException(); });
        recalculateCart(cart);
    }

    @Override
    public synchronized void clearCart() {
        cart.setItems(new ArrayList<>());
        cart.setTotalCost(BigDecimal.ZERO);
        cart.setTotalQuantity(0L);
    }
}
