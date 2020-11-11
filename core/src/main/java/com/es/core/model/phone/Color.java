package com.es.core.model.phone;

import java.io.Serializable;

public class Color implements Serializable {
    private Long id;
    private String code;

    public Color() {}

    public Color(Long id, String code) {
        this.id = id;
        this.code = code;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(final String code) {
        this.code = code;
    }
}
