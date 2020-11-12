package com.es.core.model.phone;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static org.junit.Assert.assertEquals;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:applicationContext-test.xml"})
public class JdbcPhoneDaoIntTest {

    @Autowired
    private JdbcPhoneDao phoneDao;

    @Resource
    private DriverManagerDataSource dataSourceTest;

    @Before
    public void setUp() {
        phoneDao.setDataSource(dataSourceTest);
    }

    @Test
    public void getWithColorsTestOk() {
        Phone phone = phoneDao.get(1011L).get();
        assertEquals("ARCHOS", phone.getBrand());
        assertEquals("ARCHOS 40 Cesium", phone.getModel());
    }

    @Test
    public void getWithoutColorsTestOk() {
        Phone phone = phoneDao.get(1003L).get();
        assertEquals("ARCHOS", phone.getBrand());
        assertEquals("ARCHOS 101 Oxygen", phone.getModel());
    }

    @Test
    public void findAllTestOk() {
        List<Long> expectedIds = Arrays.asList(1000L, 1001L);
        List<Long> actualIds = phoneDao.findAll(0, 2).stream()
                .map(Phone::getId)
                .collect(Collectors.toList());
        assertEquals(expectedIds, actualIds);
    }

    @Test
    @Rollback
    public void saveTestWithColorsOk() {
        Phone phone = new Phone();
        phone.setId(8764L);
        phone.setBrand("ARCHOSS");
        phone.setModel("ARCHOS 101 G99");
        Set<Color> colors = new HashSet();
        colors.add(new Color(1000L, "Black"));
        colors.add(new Color(1001L, "White"));
        phone.setColors(colors);

        phoneDao.save(phone);

        Phone actualPhone = phoneDao.get(phone.getId()).get();
        assertEquals("ARCHOSS", actualPhone.getBrand());
        assertEquals("ARCHOS 101 G99", actualPhone.getModel());

        List<Long> expectedColorsIds = colors.stream().map(Color::getId).sorted().collect(Collectors.toList());
        List<Long> actualColorsIds = actualPhone.getColors().stream().map(Color::getId).sorted().collect(Collectors.toList());
        assertEquals(expectedColorsIds, actualColorsIds);
    }

}
