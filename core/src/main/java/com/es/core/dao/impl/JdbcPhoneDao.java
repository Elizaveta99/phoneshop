package com.es.core.dao.impl;

import com.es.core.dao.PhoneDao;
import com.es.core.enumeration.sortenum.SortField;
import com.es.core.enumeration.sortenum.SortOrder;
import com.es.core.exception.ItemNotFoundException;
import com.es.core.model.phone.Color;
import com.es.core.model.phone.Phone;
import com.es.core.util.PhoneResultSetExtractor;
import org.apache.commons.beanutils.BeanMap;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class JdbcPhoneDao implements PhoneDao {

    private static final String SQL_GET = "select * from phones p left join phone2color pc on p.id = pc.phoneId left join colors c on pc.colorId = c.id where p.id = :p.id";
    private static final String SQL_GET_BY_MODEL = "select * from phones p left join phone2color pc on p.id = pc.phoneId left join colors c on pc.colorId = c.id where p.model = :model";
    private static final String SQL_INSERT = "insert into phones (id, brand, model, price, displaySizeInches, weightGr, lengthMm, widthMm, heightMm, announced, deviceType, os, displayResolution, pixelDensity, displayTechnology, backCameraMegapixels, frontCameraMegapixels, ramGb, internalStorageGb, batteryCapacityMah, talkTimeHours, standByTimeHours, bluetooth, positioning, imageUrl, description) values (:id, :brand, :model, :price, :displaySizeInches, :weightGr, :lengthMm, :widthMm, :heightMm, :announced, :deviceType, :os, :displayResolution, :pixelDensity, :displayTechnology, :backCameraMegapixels, :frontCameraMegapixels, :ramGb, :internalStorageGb, :batteryCapacityMah, :talkTimeHours, :standByTimeHours, :bluetooth, :positioning, :imageUrl, :description)";
    private static final String SQL_IF_EXIST = "select count(*) from phones where id = :id";
    private static final String SQL_UPDATE = "update phones set brand = :brand, model = :model, price = :price, displaySizeInches = :displaySizeInches, weightGr = :weightGr, lengthMm = :lengthMm, widthMm = :widthMm, heightMm = :heightMm, announced = :announced, deviceType = :deviceType, os = :os, displayResolution = :displayResolution, pixelDensity = :pixelDensity, displayTechnology = :displayTechnology, backCameraMegapixels = :backCameraMegapixels, frontCameraMegapixels = :frontCameraMegapixels, ramGb = :ramGb, internalStorageGb = :internalStorageGb, batteryCapacityMah = :batteryCapacityMah, talkTimeHours = :talkTimeHours, standByTimeHours = :standByTimeHours, bluetooth = :bluetooth, positioning = :positioning, imageUrl = :imageUrl, description = :description where id = :id";
    private static final String SQL_DELETE_COLORS = "delete from phone2color pc where pc.phoneId = :id";
    private static final String SQL_INSERT_COLORS = "insert into phone2color (phoneId, colorId) values (:phoneId, :colorId)";
    private static final String SQL_GET_STOCK = "select s.stock from phones p left join stocks s on p.id = s.phoneId where p.id = :id";
    private static final String SQL_UPDATE_STOCK = "update stocks set stock = :stock where phoneId = :phoneId";
    private static final String SQL_GET_SEARCH_TEMPLATE = "select p.*, pc.*, c.* from phones p left join stocks s on p.id = s.phoneId left join phone2color pc on p.id = pc.phoneId left join colors c on pc.colorId = c.id where s.stock - s.reserved > 0 and p.model like '%%'||:model||'%%' order by %s %s offset :offset limit :limit";

    private NamedParameterJdbcTemplate jdbcTemplate;
    private Collection<String> attributesPhoneList;
    private PhoneResultSetExtractor phoneResultSetExtractor;

    public void setJdbcTemplate(NamedParameterJdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public void setAttributesPhoneList(Collection<String> attributesPhoneList) {
        this.attributesPhoneList = attributesPhoneList;
    }

    public void setPhoneResultSetExtractor(PhoneResultSetExtractor phoneResultSetExtractor) {
        this.phoneResultSetExtractor = phoneResultSetExtractor;
    }

    @Override
    public Phone get(final Long key) {
        Phone foundPhone;
        try {
            SqlParameterSource namedParameters = new MapSqlParameterSource("p.id", key);
            foundPhone = jdbcTemplate.query(SQL_GET, namedParameters, phoneResultSetExtractor).get(0);
        } catch (EmptyResultDataAccessException | IndexOutOfBoundsException e) {
            throw new ItemNotFoundException();
        }
        return foundPhone;
    }

    @Override
    public Phone getByModel(String model) {
        Phone foundPhone;
        try {
            SqlParameterSource namedParameters = new MapSqlParameterSource("model", model);
            foundPhone = jdbcTemplate.query(SQL_GET_BY_MODEL, namedParameters, phoneResultSetExtractor).get(0);
        } catch (EmptyResultDataAccessException | IndexOutOfBoundsException e) {
            throw new ItemNotFoundException();
        }
        return foundPhone;
    }

    @Override
    public int getStock(Long key) {
        try {
            SqlParameterSource namedParameters = new MapSqlParameterSource("id", key);
            return jdbcTemplate.queryForObject(SQL_GET_STOCK, namedParameters, Integer.class);
        } catch (EmptyResultDataAccessException | IndexOutOfBoundsException | NullPointerException e) {
            return 0;
        }
    }

    @Override
    public void updateStock(Long key, int stock) {
        Map<String, Object> namedParametersMap = Map.ofEntries(
                Map.entry("phoneId", key),
                Map.entry("stock", stock)
        );
        jdbcTemplate.update(SQL_UPDATE_STOCK, namedParametersMap);
    }

    @Override
    public void save(final Phone phone) {
        SqlParameterSource namedParameters = new MapSqlParameterSource("id", phone.getId());
        Map<String,Object> namedParametersMap = getNamedParametersMapWithoutColors(phone);

        boolean isExist = jdbcTemplate.queryForObject(SQL_IF_EXIST, namedParameters, Integer.class) > 0;

        if (isExist) {
            if (!phone.getColors().isEmpty()) {
                jdbcTemplate.update(SQL_DELETE_COLORS, namedParameters);
            }
            jdbcTemplate.update(SQL_UPDATE, namedParametersMap);
        } else {
            jdbcTemplate.update(SQL_INSERT, namedParametersMap);
        }

        Map<String,Object> namedParametersPhoneColorsMap = new HashMap<>();
        namedParametersPhoneColorsMap.put("phoneId", phone.getId());
        for (Color color: phone.getColors()) {
            namedParametersPhoneColorsMap.put("colorId", color.getId());
            jdbcTemplate.update(SQL_INSERT_COLORS, namedParametersPhoneColorsMap);
        }
    }

    private Map<String, Object> getNamedParametersMapWithoutColors(Phone phone) {
        Map<String,Object> namedParametersMap = new HashMap<>();
        BeanMap beanMapPhone = new BeanMap(phone);
        for (String key: attributesPhoneList) {
            namedParametersMap.put(key, beanMapPhone.get(key));
        }
        return namedParametersMap;
    }

    @Override
    public List<Phone> findAll(int offset, int limit, String queryProduct, SortField sortField, SortOrder sortOrder) {
        Map<String, Object> namedParametersMap = new HashMap<String, Object>() {{
                put("offset", offset);
                put("limit", limit);
                put("model", queryProduct);
        }};
        return jdbcTemplate.query(makeSqlSearchQuery(sortField, sortOrder), namedParametersMap, phoneResultSetExtractor);
    }

    private String makeSqlSearchQuery(SortField sortField, SortOrder sortOrder) {
        return String.format(SQL_GET_SEARCH_TEMPLATE, sortField, sortOrder);
    }

}
