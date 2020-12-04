package com.es.core.dao;

import com.es.core.enumeration.sortenum.SortField;
import com.es.core.enumeration.sortenum.SortOrder;
import com.es.core.model.phone.Phone;

import java.util.List;

public interface PhoneDao {
    Phone get(Long key);
    void save(Phone phone);
    List<Phone> findAll(int offset, int limit, String queryProduct, SortField sortField, SortOrder sortOrder);
    int getStock(Long key);
    void updateStock(Long key, int stock);
}
