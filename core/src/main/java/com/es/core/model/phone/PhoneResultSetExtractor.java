package com.es.core.model.phone;

import org.apache.commons.beanutils.PropertyUtils;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;

import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class PhoneResultSetExtractor implements ResultSetExtractor<List<Phone>> {

    private HashMap<String, String> phoneAttributesMap;

    public void setPhoneAttributesMap(HashMap<String, String> phoneAttributesMap) {
        this.phoneAttributesMap = phoneAttributesMap;
    }

    @Override
    public List<Phone> extractData(ResultSet rs) throws SQLException, DataAccessException {
        Map<Long, Phone> phonesById = new HashMap<>();
        while (rs.next()) {
            Long id = getLongValue(rs, "id");
            Phone phone = phonesById.get(id);
            if (phone == null) {
                try {
                    phone = populateNewPhone(rs);
                } catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
                    throw new IllegalStateException(Arrays.toString(e.getStackTrace()));
                }
            }
            populatePhoneWithColors(rs, phone);
            phonesById.put(phone.getId(), phone);
        }
        return new ArrayList<>(phonesById.values());
    }

    private Phone populateNewPhone(ResultSet rs) throws SQLException, NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Phone phone = new Phone();
        Collection<String> keys = phoneAttributesMap.keySet();
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
        return phone;
    }

    private void populatePhoneWithColors(ResultSet rs, Phone phone) throws SQLException {
        Color color = new Color();
        color.setId(getLongValue(rs,"colorId"));
        color.setCode(rs.getString("code"));
        phone.getColors().add(color);
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

    private static Integer getIntegerValue(ResultSet rs, String column) throws SQLException, NumberFormatException {
        return Optional.ofNullable(rs.getString(column))
                .map(Integer::parseInt)
                .orElse(0);
    }

    private static Date getDateValue(ResultSet rs, String column) throws SQLException, NumberFormatException {
        try {
            return rs.getString(column) == null ? null : (new SimpleDateFormat("dd-MMM-yyyy HH:mm:ss")).parse(rs.getString(column));
        } catch (ParseException e) {
            return null;
        }
    }
}


