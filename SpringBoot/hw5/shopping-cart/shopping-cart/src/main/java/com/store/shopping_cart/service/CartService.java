package com.store.shopping_cart.service;

import java.util.HashMap;

import com.store.shopping_cart.model.Cart;
import com.store.shopping_cart.model.Product;


public class CartService {
    
    private Cart Cart;
    private HashMap<Product, Integer> CartProducts;


    public void setCart(int id){
        this.Cart = new Cart(id);
    }

    public void setCartProducts(HashMap<Product, Integer> CartProducts) {

        this.CartProducts = CartProducts;

    }

    public int addProduct(Product product, int quantity) {

        int productQuantity = CartProducts.get(product.getProductid());
        int targetQuantity = productQuantity + quantity;

        return targetQuantity;

    }

    public int removeProduct(Product product, int quantity) {

        int productQuantity = CartProducts.get(product.getProductid());
        int targetQuantity = productQuantity - quantity;

        if (targetQuantity < 0) {
            return 0;
        }

        return targetQuantity;

    }

    public double getCartCost() {

        double cost = 0;
        for (Product product : CartProducts.keySet()) {
            cost =+ product.getPrice();
        }
        return cost;

    }



}
