package com.store;

import java.util.ArrayList;
import java.util.HashMap;

public class CartUtil {

    public double getCartCost(Cart cart) {
        double totalCost = 0.0;
        HashMap<Product, Integer> cartItems = cart.getCartMap();  // Use getter for cartItems
        for (Product product : cartItems.keySet()) {
            int quantity = cartItems.get(product);
            totalCost += product.getProductCost() * quantity;
        }
        return totalCost;
    }
}