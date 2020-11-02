package com.es.core.model.phone;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;
import javax.sql.DataSource;
import java.util.*;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"applicationContext-test.xml"})
public class JdbcPhoneDaoIntTest {

    @Autowired
    private JdbcPhoneDao phoneDao;

    @Resource
    private DataSource dataSourceTest;

    @Before
    public void setUp() {
        phoneDao.setDataSource(dataSourceTest);
    }

    @Test
    public void getWithColorsTestOk() {
        Phone phone = phoneDao.get(1011L).get();
        assertTrue(phone.getBrand().equals("ARCHOS") && phone.getModel().equals("ARCHOS 40 Cesium"));
    }

    @Test
    public void getWithoutColorsTestOk() {
        Phone phone = phoneDao.get(1003L).get();
        assertTrue(phone.getBrand().equals("ARCHOS") && phone.getModel().equals("ARCHOS 101 Oxygen"));
    }

    @Test
    public void findAllTestOk() {
        List<Long> expectedIds = Arrays.asList(1000L, 1001L);
        List<Phone> actualPhones = phoneDao.findAll(0, 2);
        List<Long> actualIds = new ArrayList<>();
        actualPhones.stream().map(Phone::getId).forEach(actualIds::add);

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
        assertTrue(actualPhone.getBrand().equals("ARCHOSS") && actualPhone.getModel().equals("ARCHOS 101 G99"));
    }

    @Test
    @Rollback
    public void saveTestWithoutColorsOk() {
        Phone phone = new Phone();
        phone.setId(8764L);
        phone.setBrand("ARCHOSS");
        phone.setModel("ARCHOS 101 G999");

        phoneDao.save(phone);

        Phone actualPhone = phoneDao.get(phone.getId()).get();
        assertTrue(actualPhone.getBrand().equals("ARCHOSS") && actualPhone.getModel().equals("ARCHOS 101 G999"));
    }

}
