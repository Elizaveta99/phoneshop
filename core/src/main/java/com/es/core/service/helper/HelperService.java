package com.es.core.service.helper;

import com.es.core.model.phone.Phone;

import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

public interface HelperService {
    Phone populateNewPhone(ResultSet rs, String phoneIdColumn) throws SQLException, NoSuchMethodException, InvocationTargetException, IllegalAccessException;
    void populatePhoneWithColors(ResultSet rs, Phone phone) throws SQLException;
    BigDecimal getBigDecimalValue(ResultSet rs, String column) throws SQLException, NumberFormatException;
    Date getDateValue(ResultSet rs, String column) throws SQLException, NumberFormatException;
}
