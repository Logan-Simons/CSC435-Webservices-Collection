package com.store.shopping_cart.model;

import java.util.List;

public class Cart {
    

    private int cartid;
    private List<CartItem> cartProducts;

    

    public List<CartItem> getCartProducts() {
        return cartProducts;
    }




    public void setCartProducts(List<CartItem> cartProducts) {
        this.cartProducts = cartProducts;
    }




    public Cart(int id) {
    this.cartid = id;
    }

    


    public void setcartid(int id) {
        this.cartid = id;
    }

    public int getcartid() {
        return this.cartid;
    }

}
