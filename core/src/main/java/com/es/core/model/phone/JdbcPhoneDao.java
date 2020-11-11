package com.es.core.model.phone;

import com.es.core.model.exception.ItemNotFoundException;
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
    private final String sqlAllIds = "select id from (select id from phones p left join stocks s on p.id = s.phoneId where s.stock - s.reserved > 0) offset :offset limit :limit";
    private final String sqlUpdate = "update phones set brand = :brand, model = :model, price = :price, displaySizeInches = :displaySizeInches, weightGr = :weightGr, lengthMm = :lengthMm, widthMm = :widthMm, heightMm = :heightMm, announced = :announced, deviceType = :deviceType, os = :os, displayResolution = :displayResolution, pixelDensity = :pixelDensity, displayTechnology = :displayTechnology, backCameraMegapixels = :backCameraMegapixels, frontCameraMegapixels = :frontCameraMegapixels, ramGb = :ramGb, internalStorageGb = :internalStorageGb, batteryCapacityMah = :batteryCapacityMah, talkTimeHours = :talkTimeHours, standByTimeHours = :standByTimeHours, bluetooth = :bluetooth, positioning = :positioning, imageUrl = :imageUrl, description = :description where id = :id";
    private final String sqlDeleteColors = "delete from phone2color pc where pc.phoneId = :id";
    private final String sqlInsertColors = "insert into phone2color (phoneId, colorId) values (:phoneId, :colorId)";
    private final String sqlSearchIds = "select id from (select id from phones p left join stocks s on p.id = s.phoneId where s.stock - s.reserved > 0 and p.model like '%'||:model||'%') offset :offset limit :limit";
    private final String sqlSortPriceAscIds = "select id from (select id from phones p left join stocks s on p.id = s.phoneId where s.stock - s.reserved > 0 and p.model like '%'||:model||'%' order by price asc) offset :offset limit :limit";
    private final String sqlSortDisplaySizeAscIds = "select id from (select id from phones p left join stocks s on p.id = s.phoneId where s.stock - s.reserved > 0 and p.model like '%'||:model||'%' order by displaySizeInches asc) offset :offset limit :limit";
    private final String sqlSortAscIds = "select id from (select id from phones p left join stocks s on p.id = s.phoneId where s.stock - s.reserved > 0 and p.model like '%'||:model||'%' order by :sort asc) offset :offset limit :limit";
    private final String sqlSortDescIds = "select id from (select id from phones p left join stocks s on p.id = s.phoneId where s.stock - s.reserved > 0 and p.model like '%'||:model||'%' order by :sort desc) offset :offset limit :limit";
    private final String sqlSortPriceDescIds = "select id from (select id from phones p left join stocks s on p.id = s.phoneId where s.stock - s.reserved > 0 and p.model like '%'||:model||'%' order by price desc) offset :offset limit :limit";
    private final String sqlSortDisplaySizeDescIds = "select id from (select id from phones p left join stocks s on p.id = s.phoneId where s.stock - s.reserved > 0 and p.model like '%'||:model||'%' order by displaySizeInches desc) offset :offset limit :limit";
    private final String sqlGetStock = "select s.stock from phones p left join stocks s on p.id = s.phoneId where p.id = :id";


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
            throw new ItemNotFoundException();
        }
        if (!Optional.ofNullable(foundPhone).isPresent()) {
            throw new ItemNotFoundException();
        }
        return Optional.of(foundPhone);
    }

    @Override
    public int getStock(Long key) {
        SqlParameterSource namedParameters = new MapSqlParameterSource("id", key);
        return jdbcTemplate.queryForObject(sqlGetStock, namedParameters, Integer.class);
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
    public List<Phone> findAll(int offset, int limit, String queryProduct, String sortField, String sortOrder) {
        Map<String, Object> namedParametersMap = new HashMap<String, Object>() {{
                put("offset", offset);
                put("limit", limit);
                put("model", queryProduct);
                put("sort", sortField);
                put("order", sortOrder);
        }};
        List<Long> ids = jdbcTemplate.queryForList(getQuery(queryProduct, sortField, sortOrder), namedParametersMap, Long.class);
        List<Phone> phones = new ArrayList<>();
        for (Long id:ids) {
            phones.add(get(id).get());
        }
        return phones;
    }

    private String getQuery(String queryProduct, String sortField, String sortOrder) {
        String queryForIds;
        if (!sortField.equals("")) {
            if (sortOrder.equals("asc")) {
                if (sortField.equals("price")) {
                    queryForIds = sqlSortPriceAscIds;
                } else if (sortField.equals("displaySize")) {
                    queryForIds = sqlSortDisplaySizeAscIds;
                } else
                    queryForIds = sqlSortAscIds;
            } else {
                if (sortField.equals("price")) {
                    queryForIds = sqlSortPriceDescIds;
                } else if (sortField.equals("displaySize")) {
                    queryForIds = sqlSortDisplaySizeDescIds;
                } else
                    queryForIds = sqlSortDescIds;
            }
        } else if (!queryProduct.equals("")) {
            queryForIds = sqlSearchIds;
        } else {
            queryForIds = sqlAllIds;
        }
        return queryForIds;
    }
}
