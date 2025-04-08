package com.store.shopping_cart.model;

import java.util.List;

public class Cart {
    

    private int cartID;
    private List<CartItem> cartProducts;

    

    public List<CartItem> getCartProducts() {
        return cartProducts;
    }




    public void setCartProducts(List<CartItem> cartProducts) {
        this.cartProducts = cartProducts;
    }




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
