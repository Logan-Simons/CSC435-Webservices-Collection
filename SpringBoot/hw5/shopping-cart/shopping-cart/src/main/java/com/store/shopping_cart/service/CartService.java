package com.store.shopping_cart.service;

import java.util.Iterator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.store.shopping_cart.model.Cart;
import com.store.shopping_cart.model.CartItem;
import com.store.shopping_cart.model.Product;
import com.store.shopping_cart.repo.CartRepository;
import com.store.shopping_cart.repo.ProductRepository;

@Service
public class CartService {
    
    @Autowired
    private CartRepository cartRepository;
    
    
    @Autowired
    private ProductRepository productRepository;
    
    public Cart createCart() {
        Cart cart = new Cart();
        cartRepository.save(cart);
        return cart;
    }
    
    @Transactional
    public Cart getCart(int cartId) {
        Cart cart = cartRepository.findById(cartId)
            .orElseThrow(() -> new RuntimeException("Cart not found"));
        return cart;
    }
    
    @Transactional
    public void addProduct(int cartId, int productId, int quantity) {
        
        Product product = productRepository.findById(productId)
            .orElseThrow(() -> new RuntimeException("Product not found"));
            
        
        Cart cart = cartRepository.findById(cartId)
            .orElseThrow(() -> new RuntimeException("Cart not found"));

        for (CartItem item : cart.getItems()) {
        if (item.getProduct().getProductid().equals(product.getProductid())) {
            cart.updateItem(item, quantity);
            cartRepository.save(cart);
            return;
        }
    }
 
        CartItem newItem = new CartItem();
        newItem.setCart(cart);
        newItem.setProduct(product);
        newItem.setQuantity(quantity);

        cart.addItem(newItem);
        cartRepository.save(cart);
       
    }
    
    @Transactional
    public void subtractProduct(int cartId, int productId, int quantity) {
           
        Product product = productRepository.findById(productId)
            .orElseThrow(() -> new RuntimeException("Product not found"));
            
       
        Cart cart = cartRepository.findById(cartId)
            .orElseThrow(() -> new RuntimeException("Cart not found"));


        Iterator<CartItem> iterator = cart.getItems().iterator();
        while (iterator.hasNext()) {
            CartItem item = iterator.next();

            if (item.getProduct().getProductid().equals(product.getProductid())) {
                cart.updateItem(item, -1*quantity);

                if (item.getQuantity() <= 0) {
                    iterator.remove();
                }
                break;
            }
        } 
        cartRepository.save(cart);
    }
    
    @Transactional
    public double getCartCost(int cartId) {
        
        Cart cart = cartRepository.findById(cartId)
            .orElseThrow(() -> new RuntimeException("Cart not found"));

        double cost = 0;
        for (CartItem item : cart.getItems()) {
            if (item.getProduct() != null) {
                cost += item.getQuantity() * item.getProduct().getPrice();
            } else {
                throw new RuntimeException("Missing product information");
            }
            
        }

        return cost;
      
    }
    
    @Transactional
    public Cart clearCart(int cartId) {
          Cart cart = cartRepository.findById(cartId)
          .orElseThrow(() -> new RuntimeException("Cart not found"));
    
    cart.getItems().clear();
    cartRepository.save(cart);
    return cart;
    }

    public String deleteCart(int cartId) {
        cartRepository.deleteById(cartId);
        if (cartRepository.existsById(cartId)) {
            return "Cart " + cartId + " was not deleted.";
        }
        return "Cart " + cartId + " successfully deleted";
    }

    public boolean cartExists(int cartId) {
        return cartRepository.existsById(cartId);
    }
}