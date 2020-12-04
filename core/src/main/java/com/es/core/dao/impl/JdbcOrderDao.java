package com.es.core.dao.impl;

import com.es.core.dao.OrderDao;
import com.es.core.enumeration.OrderStatus;
import com.es.core.exception.ItemNotFoundException;
import com.es.core.model.order.Order;
import com.es.core.model.order.OrderItem;
import com.es.core.util.OrderResultSetExtractor;
import org.apache.commons.beanutils.BeanMap;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class JdbcOrderDao implements OrderDao {

    private static final String SQL_GET = "select * from orders o left join orderitems oi on o.secureId = oi.orderSecureId left join phones p on oi.phoneId = p.id left join phone2color pc on p.id = pc.phoneId left join colors c on pc.colorId = c.id where o.secureId = :o.secureId";
    private static final String SQL_INSERT = "insert into orders (secureId, subtotal, deliveryPrice, totalPrice, firstName, lastName, deliveryAddress, contactPhoneNo, additionalInformation, status) values (:secureId, :subtotal, :deliveryPrice, :totalPrice, :firstName, :lastName, :deliveryAddress, :contactPhoneNo, :additionalInformation, :status)";
    private static final String SQL_UPDATE_STATUS = "update orders set status = :status where id = :id";
    private static final String SQL_INSERT_ITEMS = "insert into orderitems (orderSecureId, quantity, phoneId) values (:orderSecureId, :quantity, :phoneId)";

    private NamedParameterJdbcTemplate jdbcTemplate;
    private Collection<String> attributesOrderList;
    private OrderResultSetExtractor orderResultSetExtractor;

    public void setJdbcTemplate(NamedParameterJdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public void setAttributesOrderList(Collection<String> attributesOrderList) {
        this.attributesOrderList = attributesOrderList;
    }

    public void setOrderResultSetExtractor(OrderResultSetExtractor orderResultSetExtractor) {
        this.orderResultSetExtractor = orderResultSetExtractor;
    }

    @Override
    public Order getBySecureId(String key) {
        Order foundOrder;
        try {
            SqlParameterSource namedParameters = new MapSqlParameterSource("o.secureId", key);
            foundOrder = jdbcTemplate.query(SQL_GET, namedParameters, orderResultSetExtractor).get(0);
        } catch (EmptyResultDataAccessException | IndexOutOfBoundsException e) {
            throw new ItemNotFoundException();
        }
        return foundOrder;
    }

    @Override
    public void save(Order order) {
        Map<String, Object> namedParametersMap = getNamedParametersMapWithoutItems(order);

        jdbcTemplate.update(SQL_INSERT, namedParametersMap);

        Map<String, Object> namedParametersOrderItemsMap = new HashMap<>();
        namedParametersOrderItemsMap.put("orderSecureId", order.getSecureId());
        for (OrderItem orderItem : order.getOrderItems()) {
            namedParametersOrderItemsMap.put("quantity", orderItem.getQuantity());
            namedParametersOrderItemsMap.put("phoneId", orderItem.getPhone().getId());
            jdbcTemplate.update(SQL_INSERT_ITEMS, namedParametersOrderItemsMap);
        }
    }

    @Override
    public void updateStatus(Long orderId, OrderStatus orderStatus) {
        Map<String, Object> namedParametersMap = Map.ofEntries(
                Map.entry("status", orderStatus.toString()),
                Map.entry("id", orderId));

        jdbcTemplate.update(SQL_UPDATE_STATUS, namedParametersMap);
    }

    private Map<String, Object> getNamedParametersMapWithoutItems(Order order) {
        Map<String, Object> namedParametersMap = new HashMap<>();
        BeanMap beanMapOrder = new BeanMap(order);
        for (String key : attributesOrderList) {
            if (key.equals("status")) {
                namedParametersMap.put(key, beanMapOrder.get(key).toString());
            } else {
                namedParametersMap.put(key, beanMapOrder.get(key));
            }
        }
        return namedParametersMap;
    }

}
