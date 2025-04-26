package com.store.shopping_cart.service;

import java.util.List;

import com.store.shopping_cart.model.CartItem;


public interface CartService {
    

    public abstract List<CartItem> getCartProducts(int cartid);
    public abstract int addProduct(int cartid, int productid, int quantity);
    public abstract int subtractProduct(int cartid, int productid, int quantity);
    public abstract double getCartCost(int cartid);
   

}
