package com.store;

import java.util.HashMap;

public class Cart {

    private Integer id;
    private HashMap<Integer, Product> productMap;

    public Cart(Integer ID) {
        this.id = ID;
    }

    public Integer getCartID() {
        return this.id;
    }

    public void setProducts(HashMap<Integer, Product> productMap) {
        this.productMap = productMap;
    }

    public HashMap<Integer, Product> getProducts() {
        return this.productMap;
    }

}