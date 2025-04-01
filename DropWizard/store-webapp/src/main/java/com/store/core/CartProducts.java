package com.store.core;

import java.util.HashMap;

public class CartProducts {
    
    private Integer id;
    private HashMap<Product, Integer> cartProducts;

    public CartProducts() {
        this.cartProducts = new HashMap<>();
    }

    public CartProducts(HashMap<Product, Integer> products) {
        this.cartProducts = products;
    }

    public void setProducts(HashMap<Product, Integer> products) {
        this.cartProducts = products;
    }

    public void addProduct(Product product, int quantity) {
        int currentQuantity = cartProducts.get(product);
        cartProducts.put(product, currentQuantity + 1);
    }

    public void subtractProduct(Product product, int quantity) {
        int currentQuantity = cartProducts.get(product);

        if (currentQuantity > 0) {
            cartProducts.put(product, currentQuantity - 1);
        }
    }

    public void clearCart() {
        this.cartProducts.clear();
    }


}
