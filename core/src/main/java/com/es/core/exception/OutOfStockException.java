package com.es.core.exception;

import com.es.core.model.phone.Phone;

public class OutOfStockException extends Exception{

    private final Phone phone;
    private final Long stockRequested;
    private final Long stockAvailable;

    public OutOfStockException(Phone phone, Long stockRequested, Long stockAvailable) {
        this.phone = phone;
        this.stockRequested = stockRequested;
        this.stockAvailable = stockAvailable;
    }

    public Phone getPhone() {
        return phone;
    }

    public Long getStockRequested() {
        return stockRequested;
    }

    public Long getStockAvailable() {
        return stockAvailable;
    }
}
