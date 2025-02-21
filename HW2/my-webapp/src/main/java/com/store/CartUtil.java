package com.store;

import com.cart.Cart;
import com.cart.Product;

public class CartUtil {

    public double getCartCost(Cart cart) {
        double cost = 0.00;
        ArrayList<Product> Products = cart.getProducts(cart);
        if (cart.getCartSize() != 0) {
        for (int i = 0; i < cart.getCartSize(); i++) {
            double addCost;
            Product product = Products.get(i);
            addCost = cart.getProductIndexQuantity(i) * product.getProductCost() ;
            cost = cost + addCost;
            return cost;
        }
        return cost; 
        }
    
        return cost;
    }

}