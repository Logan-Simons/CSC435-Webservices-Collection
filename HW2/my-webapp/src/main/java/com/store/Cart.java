package com.store;

import java.util.HashMap;
import java.util.Map;
import main.java.com.cart.CartUtil;

public class Cart {
    private String cartName;
    private HashMap<Product, Integer> cartItems;  // Product as key, quantity as value
    private CartUtil util = new CartUtil();
    
    public Cart(String name) {
        this.cartName = name;
        this.cartItems = new HashMap<>();
    }

    // Get all products in the cart
    public java.util.Set<Product> getProducts() {
        return cartItems.keySet();
    }

    // Get quantity of a specific product
    public int getProductQuantity(Product product) {
        return cartItems.getOrDefault(product, 0);
    }

    public void addProduct(Product product) {
        // If product exists, increment quantity, otherwise add with quantity 1
        cartItems.put(product, cartItems.getOrDefault(product, 0) + 1);
    }

    public double getCartCost(Cart cart) {
        return util.getCartCost(cart);
    }

    public void removeProduct(Product product) {
        if (cartItems.containsKey(product)) {
            int currentQuantity = cartItems.get(product);
            if (currentQuantity > 1) {
                cartItems.put(product, currentQuantity - 1);
            } else {
                cartItems.remove(product);
            }
        } else {
            System.out.println("Product not found in cart");
        }
    }

    // Set multiple products with their quantities
    public void setProducts(HashMap<Product, Integer> products) {
        this.cartItems = new HashMap<>(products);
    }

    public int getCartSize() {
        // Returns total number of unique products
        return cartItems.size();
    }

    // Get total number of items (sum of all quantities)
    public int getTotalItems() {
        return cartItems.values().stream().mapToInt(Integer::intValue).sum();
    }

    // Note: Since we're using a HashMap, getting by index isn't natural
    // If you need index-based access, you could convert keySet to array
    public Product getProductByIndex(int index) {
        Product[] products = cartItems.keySet().toArray(new Product[0]);
        if (index >= 0 && index < products.length) {
            return products[index];
        }
        throw new IndexOutOfBoundsException("Invalid index: " + index);
    }

    public int getProductIndexQuantity(int index) {
        Product product = getProductByIndex(index);
        return cartItems.get(product);
    }
}