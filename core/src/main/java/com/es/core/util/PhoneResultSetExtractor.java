package com.es.core.util;

import com.es.core.model.phone.Phone;
import com.es.core.service.helper.impl.HelperExtractorService;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;

import java.lang.reflect.InvocationTargetException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

public class PhoneResultSetExtractor implements ResultSetExtractor<List<Phone>> {

    private HelperExtractorService helperExtractorService;

    public void setHelperExtractorService(HelperExtractorService helperExtractorService) {
        this.helperExtractorService = helperExtractorService;
    }

    @Override
    public List<Phone> extractData(ResultSet rs) throws SQLException, DataAccessException {
        Map<Long, Phone> phonesById = new HashMap<>();
        while (rs.next()) {
            Long id = rs.getLong("id");
            Phone phone = phonesById.get(id);
            if (phone == null) {
                try {
                    phone = helperExtractorService.populateNewPhone(rs, "id");
                } catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
                    throw new IllegalStateException(e);
                }
            }
            helperExtractorService.populatePhoneWithColors(rs, phone);
            phonesById.put(phone.getId(), phone);
        }
        return new ArrayList<>(phonesById.values());
    }
}


