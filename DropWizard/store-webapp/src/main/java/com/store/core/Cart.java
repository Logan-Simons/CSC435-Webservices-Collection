package com.store.core;

import org.jdbi.v3.core.mapper.reflect.ColumnName;


public class Cart {

    @ColumnName("cartid")
    private Integer id;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
    
}
