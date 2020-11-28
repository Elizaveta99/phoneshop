package com.es.core.dao;

import com.es.core.model.phone.Phone;

import java.util.List;
import java.util.Optional;

public interface PhoneDao {
    Phone get(Long key);
    void save(Phone phone);
    List<Phone> findAll(int offset, int limit, String queryProduct, String sortField, String sortOrder);
    int getStock(Long key);
    void updateStock(Long key, int stock);
}
