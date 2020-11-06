package com.es.core.model.phone;

import org.apache.commons.beanutils.BeanMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import java.util.*;

public class JdbcPhoneDao implements PhoneDao {

    private final String sqlGet = "select * from phones p left join phone2color pc on p.id = pc.phoneId left join colors c on pc.colorId = c.id where p.id = :p.id";
    private final String sqlInsert = "insert into phones (id, brand, model, price, displaySizeInches, weightGr, lengthMm, widthMm, heightMm, announced, deviceType, os, displayResolution, pixelDensity, displayTechnology, backCameraMegapixels, frontCameraMegapixels, ramGb, internalStorageGb, batteryCapacityMah, talkTimeHours, standByTimeHours, bluetooth, positioning, imageUrl, description) values (:id, :brand, :model, :price, :displaySizeInches, :weightGr, :lengthMm, :widthMm, :heightMm, :announced, :deviceType, :os, :displayResolution, :pixelDensity, :displayTechnology, :backCameraMegapixels, :frontCameraMegapixels, :ramGb, :internalStorageGb, :batteryCapacityMah, :talkTimeHours, :standByTimeHours, :bluetooth, :positioning, :imageUrl, :description)";
    private final String sqlIfExist = "select count(*) from phones where id = :id";
    private final String sqlAllIds = "select id from phones offset :offset limit :limit";
    private final String sqlUpdate = "update phones set brand = :brand, model = :model, price = :price, displaySizeInches = :displaySizeInches, weightGr = :weightGr, lengthMm = :lengthMm, widthMm = :widthMm, heightMm = :heightMm, announced = :announced, deviceType = :deviceType, os = :os, displayResolution = :displayResolution, pixelDensity = :pixelDensity, displayTechnology = :displayTechnology, backCameraMegapixels = :backCameraMegapixels, frontCameraMegapixels = :frontCameraMegapixels, ramGb = :ramGb, internalStorageGb = :internalStorageGb, batteryCapacityMah = :batteryCapacityMah, talkTimeHours = :talkTimeHours, standByTimeHours = :standByTimeHours, bluetooth = :bluetooth, positioning = :positioning, imageUrl = :imageUrl, description = :description where id = :id";
    private final String sqlDeleteColors = "delete from phone2color pc where pc.phoneId = :id";
    private final String sqlInsertColors = "insert into phone2color (phoneId, colorId) values (:phoneId, :colorId)";

    private NamedParameterJdbcTemplate jdbcTemplate;
    private List<String> attributesList;
    private PhoneRowMapper phoneRowMapper;

    public void setPhoneRowMapper(PhoneRowMapper phoneRowMapper) {
        this.phoneRowMapper = phoneRowMapper;
    }

    public void setAttributesList(List<String> attributesList) {
        this.attributesList = attributesList;
    }

    @Autowired
    public void setDataSource(DriverManagerDataSource dataSource) {
        jdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
    }

    @Override
    public Optional<Phone> get(final Long key) {
        Phone foundPhone;
        try {
            SqlParameterSource namedParameters = new MapSqlParameterSource("p.id", key);
            foundPhone = jdbcTemplate.queryForObject(sqlGet, namedParameters, phoneRowMapper);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
        return Optional.ofNullable(foundPhone);
    }

    @Override
    public void save(final Phone phone) {
        SqlParameterSource namedParameters = new MapSqlParameterSource("id", phone.getId());
        Map<String,Object> namedParametersMap;

        boolean isExist = jdbcTemplate.queryForObject(sqlIfExist, namedParameters, Integer.class) > 0;
        if (isExist) {
            namedParametersMap = getNamedParametersMapWithoutColors(phone);
            if (phone.getColors().size() > 0) {
                jdbcTemplate.update(sqlDeleteColors, namedParameters);
            }
            jdbcTemplate.update(sqlUpdate, namedParametersMap);
        } else {
            namedParametersMap = getNamedParametersMapWithoutColors(phone);
            jdbcTemplate.update(sqlInsert, namedParametersMap);
        }
        namedParametersMap.put("phoneId", phone.getId());
        for (Color color: phone.getColors()) {
            namedParametersMap.put("colorId", color.getId());
            jdbcTemplate.update(sqlInsertColors, namedParametersMap);
        }
    }

    private Map<String, Object> getNamedParametersMapWithoutColors(Phone phone) {
        Map<String,Object> namedParametersMap = new HashMap<>();
        BeanMap beanMapPhone = new BeanMap(phone);
        for (String key: attributesList) {
            namedParametersMap.put(key, beanMapPhone.get(key));
        }
        return namedParametersMap;
    }

    @Override
    public List<Phone> findAll(int offset, int limit) {
        Map<String, Integer> namedParametersMap = new HashMap<String, Integer>() {{
                put("offset", offset);
                put("limit", limit);
        }};
        List<Long> ids = jdbcTemplate.queryForList(sqlAllIds, namedParametersMap, Long.class);
        List<Phone> phones = new ArrayList<>();
        for (Long id:ids) {
            phones.add(get(id).get());
        }
        return phones;
    }
}
