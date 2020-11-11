package com.es.core.cart;

import com.es.core.model.exception.OutOfStockException;
import com.es.core.model.phone.JdbcPhoneDao;
import com.es.core.model.phone.Phone;
import com.es.core.model.phone.PhoneDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Map;
import java.util.Optional;

@Service
public class HttpSessionCartService implements CartService {

    private PhoneDao phoneDao;

    @Autowired
    private Cart cart;

    @Autowired
    public void setPhoneDao(JdbcPhoneDao phoneDao) {
        this.phoneDao = phoneDao;
    }


    @Override
    public synchronized Cart getCart() {
        return cart;
    }

    @Override
    public synchronized void addPhone(Long phoneId, Long quantity) throws OutOfStockException {
        Optional<Phone> phone = phoneDao.get(phoneId);
        Optional<CartItem> cartItemOptional = getCartItemOptional(cart, phone.get());
        Long productsAmount = cartItemOptional.map(CartItem::getQuantity).orElse(0L);

        checkQuantity(quantity, productsAmount + quantity, phone.get());

        if (cartItemOptional.isPresent()) {
            cartItemOptional.get().setQuantity(productsAmount + quantity);
        } else {
            cart.getItems().add(new CartItem(phone.get(), quantity));
        }
        recalculateCart(cart);
    }

    private Optional<CartItem> getCartItemOptional(Cart cart, Phone phone) {
        return cart.getItems().stream()
                .filter(c -> phone.getId().equals(c.getPhone().getId()))
                .findAny();
    }

    private void checkQuantity(Long quantity, Long newQuantity, Phone phone) throws OutOfStockException {
        if (quantity <= 0) {
            throw new OutOfStockException(null, quantity, 0L);
        }
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
    public synchronized void update(Map<Long, Long> items) {
        throw new UnsupportedOperationException("TODO");
    }

    @Override
    public synchronized void remove(Long phoneId) {
        throw new UnsupportedOperationException("TODO");
    }
}
