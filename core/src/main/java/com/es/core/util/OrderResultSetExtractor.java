package com.es.core.util;

import com.es.core.enumeration.OrderStatus;
import com.es.core.model.order.Order;
import com.es.core.model.order.OrderItem;
import com.es.core.model.phone.Phone;
import com.es.core.service.helper.impl.HelperExtractorService;
import org.apache.commons.beanutils.PropertyUtils;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;

import java.lang.reflect.InvocationTargetException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

public class OrderResultSetExtractor implements ResultSetExtractor<List<Order>> {

    private HashMap<String, String> orderAttributesMap;
    private HelperExtractorService helperExtractorService;

    public void setOrderAttributesMap(HashMap<String, String> orderAttributesMap) {
        this.orderAttributesMap = orderAttributesMap;
    }

    public void setHelperExtractorService(HelperExtractorService helperExtractorService) {
        this.helperExtractorService = helperExtractorService;
    }

    @Override
    public List<Order> extractData(ResultSet rs) throws SQLException, DataAccessException {
        Map<Long, Order> ordersById = new HashMap<>();
        Map<Long, Phone> phonesById = new HashMap<>();
        Map<Long, OrderItem> orderItemsByPhoneId = new HashMap<>();

        while (rs.next()) {
            Long id = rs.getLong("id");
            Order order = ordersById.get(id);
            if (order == null) {
                try {
                    order = populateNewOrder(rs);
                } catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
                    throw new IllegalStateException(e);
                }
            }

            Phone phone = extractPhone(rs, rs.getLong("phoneId"), phonesById);
            populateOrderWithItems(rs, order, phone, orderItemsByPhoneId);
            ordersById.put(order.getId(), order);
        }

        return new ArrayList<>(ordersById.values());
    }

    private Phone extractPhone(ResultSet rs, Long phoneId, Map<Long, Phone> phonesById) throws SQLException {
        Phone phone = phonesById.get(phoneId);
        if (phone == null) {
            try {
                phone = helperExtractorService.populateNewPhone(rs, "phoneId");
            } catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
                throw new IllegalStateException(e);
            }
        }
        helperExtractorService.populatePhoneWithColors(rs, phone);
        phonesById.put(phone.getId(), phone);
        return phone;
    }

    private Order populateNewOrder(ResultSet rs) throws SQLException, NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Order order = new Order();
        Collection<String> keys = orderAttributesMap.keySet();
        for (String key : keys) {
            switch (orderAttributesMap.get(key)) {
                case "Long":
                    PropertyUtils.setProperty(order, key, rs.getLong(key));
                    break;
                case "BigDecimal":
                    PropertyUtils.setProperty(order, key, helperExtractorService.getBigDecimalValue(rs, key));
                    break;
                case "String":
                    PropertyUtils.setProperty(order, key, rs.getString(key));
                    break;
                case "OrderStatus":
                    PropertyUtils.setProperty(order, key, OrderStatus.valueOf(rs.getString(key)));
                    break;
                case "Date":
                    PropertyUtils.setProperty(order, key, helperExtractorService.getDateValue(rs, key));
                    break;
            }
        }
        order.setOrderItems(new ArrayList<>());
        return order;
    }

    private void populateOrderWithItems(ResultSet rs, Order order, Phone phone, Map<Long, OrderItem> orderItemsByPhoneId) throws SQLException {
        Long phoneId = phone.getId();
        if (!orderItemsByPhoneId.containsKey(phoneId)) {
            OrderItem orderItem = new OrderItem();
            orderItem.setOrderId(rs.getString("orderSecureId"));
            orderItem.setQuantity(rs.getLong("quantity"));
            orderItem.setPhone(phone);
            orderItemsByPhoneId.put(phoneId, orderItem);
            order.getOrderItems().add(orderItem);
        }
    }
}


