package com.store.shopping_cart.model;

import java.util.HashMap;

public class Cart {
    

    private int cartID;

    

    public Cart(int id) {
    this.cartID = id;
    }

    


    public void setCartID(int id) {
        this.cartID = id;
    }

    public int getCartID() {
        return this.cartID;
    }

}
