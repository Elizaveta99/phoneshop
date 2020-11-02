package com.es.core.model.phone;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.sql.DataSource;
import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Component
public class JdbcPhoneDao implements PhoneDao {
    //@Resource
    private JdbcTemplate jdbcTemplate;

    @Autowired
    public void setDataSource(DataSource dataSource) {
        jdbcTemplate = new JdbcTemplate(dataSource);
    }

    public Optional<Phone> get(final Long key) {
        String sql;
        Phone foundPhone;
        try {
            sql = "select * from phones p, phone2color pc, colors c where p.id = ? and pc.phoneId = p.id and c.id = pc.colorId";
            foundPhone = jdbcTemplate.queryForObject(sql, new Object[]{key}, new PhoneMapper());
        } catch (EmptyResultDataAccessException e) {
            sql = "select * from phones p where p.id = ?";
            foundPhone = jdbcTemplate.queryForObject(sql, new Object[]{key}, new BeanPropertyRowMapper<>(Phone.class));
        }
        return Optional.ofNullable(foundPhone);
    }

    public void save(final Phone phone) {
        jdbcTemplate.update("delete from phones where id = ?",
                phone.getId());
        jdbcTemplate.update(
                "insert into phones (id, brand, model, price, displaySizeInches, weightGr, lengthMm, widthMm, heightMm, announced, deviceType, os, displayResolution, pixelDensity, displayTechnology, backCameraMegapixels, frontCameraMegapixels, ramGb, internalStorageGb, batteryCapacityMah, talkTimeHours, standByTimeHours, bluetooth, positioning, imageUrl, description) values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)", phone.getId(), phone.getBrand(), phone.getModel(), phone.getPrice(), phone.getDisplaySizeInches(), phone.getWeightGr(), phone.getLengthMm(), phone.getWidthMm(), phone.getHeightMm(), phone.getAnnounced(), phone.getDeviceType(), phone.getOs(), phone.getDisplayResolution(), phone.getPixelDensity(), phone.getDisplayTechnology(), phone.getBackCameraMegapixels(), phone.getFrontCameraMegapixels(), phone.getRamGb(), phone.getInternalStorageGb(), phone.getBatteryCapacityMah(), phone.getTalkTimeHours(), phone.getStandByTimeHours(), phone.getBluetooth(), phone.getPositioning(), phone.getImageUrl(), phone.getDescription());

        if (phone.getColors().size() > 0) {
            phone.getColors().forEach(c -> jdbcTemplate.update("insert into phone2color (phoneId, colorId) values (?, ?)",
                    phone.getId(), c.getId()));
        }

    }

    public List<Phone> findAll(int offset, int limit) {
        return jdbcTemplate.query("select * from phones offset ? limit ?", new Object[]{offset, limit},
                new BeanPropertyRowMapper<>(Phone.class));
    }

    private static final class PhoneMapper implements RowMapper<Phone> {

        public Phone mapRow(ResultSet rs, int rowNum) throws SQLException {
            Phone phone = populatePhone(rs, null);
            while(rs.next()) {
                phone = populatePhone(rs, phone);
            }
            return phone;
        }
    }

    private static Phone populatePhone(ResultSet rs, Phone phone) throws SQLException {
        if (phone == null) {
            phone = new Phone();
            phone.setColors(new HashSet<>());
            phone.setId(Long.valueOf(rs.getString("id")));
            phone.setBrand(rs.getString("brand"));
            phone.setModel(rs.getString("model"));
            phone.setPrice(getBigDecimalValue(rs, "price"));
            phone.setDisplaySizeInches(getBigDecimalValue(rs, "displaySizeInches"));
            phone.setWeightGr(getIntegerValue(rs,"weightGr"));
            phone.setLengthMm(getBigDecimalValue(rs, "lengthMm"));
            phone.setWidthMm(getBigDecimalValue(rs, "widthMm"));
            phone.setHeightMm(getBigDecimalValue(rs, "heightMm"));
            try {
                phone.setAnnounced(rs.getString("announced") == null ? null : (new SimpleDateFormat("dd-MMM-yyyy HH:mm:ss")).parse(rs.getString("announced"))); // ??
            } catch (ParseException e) {
                throw new RuntimeException(); // temporarily
            }
            phone.setDeviceType(rs.getString("deviceType"));
            phone.setOs(rs.getString("os"));
            phone.setDisplayResolution(rs.getString("displayResolution"));
            phone.setPixelDensity(getIntegerValue(rs,"pixelDensity"));
            phone.setDisplayTechnology(rs.getString("displayTechnology"));
            phone.setBackCameraMegapixels(getBigDecimalValue(rs, "backCameraMegapixels"));
            phone.setFrontCameraMegapixels(getBigDecimalValue(rs, "frontCameraMegapixels"));
            phone.setRamGb(getBigDecimalValue(rs, "ramGb"));
            phone.setInternalStorageGb(getBigDecimalValue(rs, "frontCameraMegapixels"));
            phone.setBatteryCapacityMah(getIntegerValue(rs,"batteryCapacityMah"));
            phone.setTalkTimeHours(getBigDecimalValue(rs, "talkTimeHours"));
            phone.setStandByTimeHours(getBigDecimalValue(rs, "standByTimeHours"));
            phone.setBluetooth(rs.getString("bluetooth"));
            phone.setPositioning(rs.getString("positioning"));
            phone.setImageUrl(rs.getString("imageUrl"));
            phone.setDescription(rs.getString("description"));
        }
        Color color = new Color();
        color.setId(Long.valueOf(rs.getString("id")));
        color.setCode(rs.getString("code"));
        phone.getColors().add(color);
        return phone;
    }

    private static BigDecimal getBigDecimalValue(ResultSet rs, String column) throws SQLException {
        return rs.getString(column) == null ? BigDecimal.ZERO : new BigDecimal(rs.getString(column));
    }

    private static Integer getIntegerValue(ResultSet rs, String column) throws SQLException {
        return rs.getString(column) == null ? 0 : Integer.parseInt(rs.getString(column));
    }

}
