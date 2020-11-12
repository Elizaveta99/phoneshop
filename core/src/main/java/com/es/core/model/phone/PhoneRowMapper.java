package com.es.core.model.phone;

import com.es.core.model.exception.InputErrorException;
import org.apache.commons.beanutils.PropertyUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;

import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

public class PhoneRowMapper implements RowMapper<Phone> {

    private Map<String, String> phoneAttributesMap;
    private List<String> attributesList;

    public void setPhoneAttributesMap(Map<String, String> phoneAttributesMap) {
        this.phoneAttributesMap = phoneAttributesMap;
    }

    @Autowired
    public void setAttributesList(List<String> attributesList) {
        this.attributesList = attributesList;
    }

    @Override
    public Phone mapRow(ResultSet rs, int rowNum) throws SQLException {
        Phone phone;
        try {
            phone = populateNewPhone(rs);
        } catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
            throw new RuntimeException();
        } catch (ParseException e) {
            throw new InputErrorException("Wrong input format");
        }
        while (rs.next()) {
            phone = populatePhoneWithColors(rs, phone);
        }
        return phone;
    }

    private Phone populateNewPhone(ResultSet rs) throws SQLException, NoSuchMethodException, InvocationTargetException, ParseException, IllegalAccessException {
        Phone phone = new Phone();
        List<String> keys = attributesList;
        for (String key: keys) {
            switch (phoneAttributesMap.get(key)) {
                case "Long":
                    PropertyUtils.setProperty(phone, key, getLongValue(rs, key));
                    break;
                case "Integer":
                    PropertyUtils.setProperty(phone, key, getIntegerValue(rs, key));
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
        phone.setColors(new HashSet<>());
        return populatePhoneWithColors(rs, phone);
    }

    private Phone populatePhoneWithColors(ResultSet rs, Phone phone) throws SQLException {
        Color color = new Color();
        color.setId(getLongValue(rs,"colorId"));
        color.setCode(rs.getString("code"));
        phone.getColors().add(color);
        return phone;
    }

    private static Long getLongValue(ResultSet rs, String column) throws SQLException, NumberFormatException {
        return rs.getString(column) == null ? 0 : Long.parseLong(rs.getString(column));
    }

    private static BigDecimal getBigDecimalValue(ResultSet rs, String column) throws SQLException, NumberFormatException {
        return rs.getString(column) == null ? BigDecimal.ZERO : new BigDecimal(rs.getString(column));
    }

    private static Integer getIntegerValue(ResultSet rs, String column) throws SQLException, NumberFormatException {
        return rs.getString(column) == null ? 0 : Integer.parseInt(rs.getString(column));
    }

    private static Date getDateValue(ResultSet rs, String column) throws SQLException, NumberFormatException, ParseException {
        try {
            return rs.getString(column) == null ? null : (new SimpleDateFormat("dd-MMM-yyyy HH:mm:ss")).parse(rs.getString(column));
        } catch (ParseException e) {
            return null;
        }
    }
}
