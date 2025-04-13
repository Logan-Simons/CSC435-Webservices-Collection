package com.store.shopping_cart.model;


import java.util.Iterator;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "cart")
public class Cart {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int cartid;

    @OneToMany(mappedBy = "cart", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CartItem> cartProducts;


    public Cart() {}
    

    public List<CartItem> getCartProducts() {
        return cartProducts;
    }

     // Business logic for adding a product to the cart.
    public void addProduct(Product product, int quantity) {
        // Check if the product is already in the cart.
        for (CartItem item : cartProducts) {
            if (item.getProduct().equals(product)) {
                item.setQuantity(item.getQuantity() + quantity);
                return;
            }
        }
        // Otherwise, create a new CartItem.
        CartItem newItem = new CartItem(this, product, quantity);
        cartProducts.add(newItem);
    }

    // Business logic for subtracting a product quantity from the cart.
    public void subtractProduct(Product product, int quantity) {
        for (Iterator<CartItem> iterator = cartProducts.iterator(); iterator.hasNext(); ) {
            CartItem item = iterator.next();
            if (item.getProduct().equals(product)) {
                int newQuantity = item.getQuantity() - quantity;
                if (newQuantity <= 0) {
                    // Remove the item if the quantity is zero or less.
                    iterator.remove();
                    item.setCart(null);
                } else {
                    item.setQuantity(newQuantity);
                }
                break;
            }
        }
    }

    public double getCartCost() {

        double cost = 0;
        for (CartItem item : cartProducts) {
            cost += item.getProduct().getPrice() * item.getQuantity();
        }
        return cost;
    }


    public int getId() {
        return this.cartid;
    }

}
