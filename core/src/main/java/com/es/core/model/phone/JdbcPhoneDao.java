package com.es.core.model.phone;

import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Component
public class JdbcPhoneDao implements PhoneDao {
    @Resource
    private JdbcTemplate jdbcTemplate;

    public Optional<Phone> get(final Long key) {
        String sql = "select * from phones p, phone2color pc, colors c where p.id = ? and pc.phoneId = p.id and c.id = pc.colorId";
        Phone foundPhone = jdbcTemplate.queryForObject(sql, new Object[]{key}, new PhoneMapper());
        return Optional.ofNullable(foundPhone);
    }

    public void save(final Phone phone) {
        jdbcTemplate.update("delete from phones where id = ?",
                phone.getId());
        jdbcTemplate.update(
                "insert into phones (id, brand, model, price, displaySizeInches, weightGr, lengthMm, widthMm, heightMm, announced, deviceType, os, displayResolution, pixelDensity, displayTechnology, backCameraMegapixels, frontCameraMegapixels, ramGb, internalStorageGb, batteryCapacityMah, talkTimeHours, standByTimeHours, bluetooth, positioning, imageUrl, description) values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)",
                8764L, "ARCHOSS", "ARCHOS 101 G9", null, 10.1, 482, 276.0, 167.0, 12.6, null, "Tablet", "Android (4.0)", "1280 x  800", 149, null, null, 1.3, null, 8.0, null, null, null, "2.1, EDR", "GPS", "manufacturer/ARCHOS/ARCHOS 101 G9.jpg", "The ARCHOS 101 G9 is a 10.1'''' tablet, equipped with Google''s open source OS. It offers a multi-core ARM CORTEX A9 processor at 1GHz, 8 or 16GB internal memory, microSD card slot, GPS, Wi-Fi, Bluetooth 2.1, and more.");

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
            phone.setBrand(rs.getString("brand"));
            phone.setModel(rs.getString("model"));
            phone.setPrice(getBigDecimalValue(rs, "price"));
            phone.setDisplaySizeInches(new BigDecimal(rs.getString("displaySizeInches")));
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
