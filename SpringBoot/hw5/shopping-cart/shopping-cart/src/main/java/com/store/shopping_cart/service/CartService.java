package com.store.shopping_cart.service;

import org.springframework.stereotype.Service;

import com.store.shopping_cart.model.Cart;
import com.store.shopping_cart.model.Product;
import com.store.shopping_cart.repo.CartRepository;
import com.store.shopping_cart.repo.ProductRepository;

import jakarta.transaction.Transactional;

@Service
public class CartService {

    private final CartRepository cartRepository;
    private final ProductRepository productRepository;

    public CartService(CartRepository cartRepository, ProductRepository productRepository) {
        this.cartRepository = cartRepository;
        this.productRepository = productRepository;
    }

    @Transactional
    public Cart addProduct(Integer cartId, Integer productId, int quantity) {
        Cart cart = cartRepository.findById(cartId)
                .orElseThrow(() -> new RuntimeException("Cart not found"));
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        cart.addProduct(product, quantity);

        return cartRepository.save(cart);
    }

    @Transactional
    public Cart subtractProduct(Integer cartId, Integer productId, int quantity) {
        Cart cart = cartRepository.findById(cartId)
                .orElseThrow(() -> new RuntimeException("Cart not found"));
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        cart.subtractProduct(product, quantity);

        return cartRepository.save(cart);
    }

    public Cart getCart(Integer cartId) {
        return cartRepository.findById(cartId)
                .orElseThrow(() -> new RuntimeException("Cart not found"));
    }

    public Double getCartCost(Integer cartId) {
        
        Cart cart = cartRepository.findById(cartId)
        .orElseThrow(() -> new RuntimeException("Cart not found"));

        return cart.getCartCost();

    }
}