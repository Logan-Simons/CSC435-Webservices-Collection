package com.store.core;

import org.jdbi.v3.core.mapper.reflect.ColumnName;

public class Product {
    
    @ColumnName("productid")
    private Integer id;

    private String name;
    private String description;
    private Double price;

    public void setId(Integer id) {
        this.id = id;
    }
    public void setName(String name) {
        this.name = name;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    public void setPrice(Double price) {
        this.price = price;
    }
    
    public Integer getId() {
        return id;
    }
    public String getName() {
        return name;
    }
    public String getDescription() {
        return description;
    }
    public Double getPrice() {
        return price;
    }

}
