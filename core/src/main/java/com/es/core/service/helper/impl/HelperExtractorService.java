package com.es.core.service.helper.impl;

import com.es.core.model.phone.Color;
import com.es.core.model.phone.Phone;
import com.es.core.service.helper.HelperService;
import org.apache.commons.beanutils.PropertyUtils;

import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class HelperExtractorService implements HelperService {

    private HashMap<String, String> phoneAttributesMap;

    public void setPhoneAttributesMap(HashMap<String, String> phoneAttributesMap) {
        this.phoneAttributesMap = phoneAttributesMap;
    }

    @Override
    public Phone populateNewPhone(ResultSet rs, String phoneIdColumn) throws SQLException, NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Phone phone = new Phone();
        Collection<String> keys = phoneAttributesMap.keySet();
        for (String key : keys) {
            switch (phoneAttributesMap.get(key)) {
                case "Long":
                    PropertyUtils.setProperty(phone, key, rs.getLong(key));
                    break;
                case "Integer":
                    PropertyUtils.setProperty(phone, key, rs.getInt(key));
                    break;
                case "BigDecimal":
                    PropertyUtils.setProperty(phone, key, getBigDecimalValue(rs, key));
                    break;
                case "Date":
                    PropertyUtils.setProperty(phone, key, getDateValue(rs, key));
                    break;
                case "String":
                    PropertyUtils.setProperty(phone, key, rs.getString(key));
                    break;
            }
        }
        phone.setId(rs.getLong(phoneIdColumn));
        phone.setColors(new HashSet<>());
        return phone;
    }

    @Override
    public void populatePhoneWithColors(ResultSet rs, Phone phone) throws SQLException {
        Color color = new Color();
        color.setId(rs.getLong("colorId"));
        color.setCode(rs.getString("code"));
        phone.getColors().add(color);
    }

    @Override
    public BigDecimal getBigDecimalValue(ResultSet rs, String column) throws SQLException, NumberFormatException {
        return Optional.ofNullable(rs.getString(column))
                .map(BigDecimal::new)
                .orElse(BigDecimal.ZERO);
    }

    private static Date getDateValue(ResultSet rs, String column) throws SQLException, NumberFormatException {
        try {
            return rs.getString(column) == null ? null : (new SimpleDateFormat("dd-MMM-yyyy HH:mm:ss")).parse(rs.getString(column));
        } catch (ParseException e) {
            return null;
        }
    }
}


