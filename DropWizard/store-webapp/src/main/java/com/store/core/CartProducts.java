package com.store.core;

import org.jdbi.v3.core.mapper.reflect.ColumnName;

public class CartProducts {
    
    @ColumnName("cartid")
    private Integer cartid;
    
    // use product object, this is to avoid additional fetches to the database and for
    // better readibility on the view layer
    private Product product;
    
    @ColumnName("total_quantity")
    private Integer quantity;

    // Getters and setters

    public Integer getCartid() {
        return cartid;
    }
    public void setCartid(Integer cartid) {
        this.cartid = cartid;
    }
    
    public Product getProduct() {
        return product;
    }
    public void setProduct(Product product) {
        this.product = product;
    }
    
    public Integer getQuantity() {
        return quantity;
    }
    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }
}
