package com.store.shopping_cart.model;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;

@Entity
public class Cart {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer cartId;

    // One cart has many CartItems, each holding a product and its quantity.
    @OneToMany(mappedBy = "cart", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CartItem> items = new ArrayList<>();

    // Constructors, getters, and setters
    public Cart() {}

    public Integer getCartId() {
        return cartId;
    }

    public void setCartId(Integer cartId) {
        this.cartId = cartId;
    }

    public List<CartItem> getItems() {
        return items;
    }

    public void updateItem(CartItem item, int quantity) {
        for (int i = 0; i < items.size(); i++) {
            if (items.get(i).getProduct().getProductid() == item.getProduct().getProductid()) {
                int currentQuantity = items.get(i).getQuantity();
                if (quantity + currentQuantity > 0) {
                items.get(i).setQuantity(quantity + currentQuantity);
                } else {
                items.remove(i);
                }
            }
        }
    }

    public void setItems(List<CartItem> items) {
        this.items = items;
    }
    
    public void addItem(CartItem item) {
        items.add(item);
        item.setCart(this);
    }
    
    public void removeItem(CartItem item) {

        for (CartItem c_item : items) {
            if (c_item.getProduct().getProductid() == item.getProduct().getProductid()) {
                items.remove(c_item);
            }
        }
        
        
      
    }
}
