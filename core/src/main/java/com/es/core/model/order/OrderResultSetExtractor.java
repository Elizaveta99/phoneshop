package com.es.core.model.order;

import org.apache.commons.beanutils.PropertyUtils;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;

import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

public class OrderResultSetExtractor implements ResultSetExtractor<List<Order>> {

    private HashMap<String, String> orderAttributesMap;

    public void setOrderAttributesMap(HashMap<String, String> orderAttributesMap) {
        this.orderAttributesMap = orderAttributesMap;
    }

    @Override
    public List<Order> extractData(ResultSet rs) throws SQLException, DataAccessException {
        Map<Long, Order> ordersById = new HashMap<>();
        while (rs.next()) {
            Long id = getLongValue(rs, "id");
            Order order = ordersById.get(id);
            if (order == null) {
                try {
                    order = populateNewOrder(rs);
                } catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
                    throw new IllegalStateException(Arrays.toString(e.getStackTrace()));
                }
            }
            populateOrderWithItems(rs, order);
            ordersById.put(order.getId(), order);
        }
        return new ArrayList<>(ordersById.values());
    }

    private Order populateNewOrder(ResultSet rs) throws SQLException, NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Order order = new Order();
        Collection<String> keys = orderAttributesMap.keySet();
        for (String key: keys) {
            switch (orderAttributesMap.get(key)) {
                case "Long":
                    PropertyUtils.setProperty(order, key, getLongValue(rs, key));
                    break;
                case "BigDecimal":
                    PropertyUtils.setProperty(order, key, getBigDecimalValue(rs, key));
                    break;
                case "String":
                    PropertyUtils.setProperty(order, key, rs.getString(key));
                    break;
            }
        }
        order.setOrderItems(new ArrayList<>());
        return order;
    }

    private void populateOrderWithItems(ResultSet rs, Order order) throws SQLException {
        OrderItem orderItem = new OrderItem();
        orderItem.setOrderId(rs.getString("orderSecureId"));
        orderItem.setQuantity(getLongValue(rs, "quantity"));
        order.getOrderItems().add(orderItem);
    }

    private static Long getLongValue(ResultSet rs, String column) throws SQLException, NumberFormatException {
        return Optional.ofNullable(rs.getString(column))
                .map(Long::parseLong)
                .orElse(0L);
    }

    private static BigDecimal getBigDecimalValue(ResultSet rs, String column) throws SQLException, NumberFormatException {
        return Optional.ofNullable(rs.getString(column))
                .map(BigDecimal::new)
                .orElse(BigDecimal.ZERO);
    }
}


