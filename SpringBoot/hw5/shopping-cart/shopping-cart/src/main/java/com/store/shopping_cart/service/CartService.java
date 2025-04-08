package com.store.shopping_cart.service;

import java.util.HashMap;

import com.store.shopping_cart.model.Product;


public interface CartService {
    
    public abstract void setCart(int cartid);
    public abstract void setCartProducts(int cartid, HashMap<Product, Integer> CartProducts);
    public abstract HashMap<Product, Integer> getCartProducts(int cartid);
    public abstract int addProduct(int cartid, Product product, int quantity);
    public abstract int removeProduct(int cartid, Product product, int quantity);
    public abstract double getCartCost(int cartid);
   

}
