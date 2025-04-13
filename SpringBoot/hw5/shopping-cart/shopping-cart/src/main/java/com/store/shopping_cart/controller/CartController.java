package com.store.shopping_cart.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.store.shopping_cart.model.Cart;
import com.store.shopping_cart.model.CartItem;
import com.store.shopping_cart.service.CartService;

@RestController
@RequestMapping("/store")
public class CartController {

    @Autowired
    private CartService cartService;

    @PostMapping("cart/create")
    public ResponseEntity<Object> createCart() {
        Cart cart = new Cart();
        return new ResponseEntity<>("New cart created " + cart.getId(), HttpStatus.OK);
    }

    // Get the contents of a cart by its id
    @GetMapping("cart/{id}")
    public ResponseEntity<Object> getCart(@PathVariable("id") Integer cartid) {
        Cart cart = cartService.getCart(cartid);
        List<CartItem> items = cart.getCartProducts();
        return new ResponseEntity<>(items, HttpStatus.OK);
    }

    public List<CartItem> getCartItems(Cart cart) {
        return cart.getCartProducts();
    }

    // Add a product to the specified cart
    @PostMapping("cart/{id}/add")
    public ResponseEntity<Object> addProduct(
            @PathVariable("id") int cartid, 
            @RequestParam("p") int productid, 
            @RequestParam("q") int quantity) {
        return new ResponseEntity<>(cartService.addProduct(cartid, productid, quantity), HttpStatus.OK);
    }

    // Subtract a product from the specified cart
    @PutMapping("cart/{id}/subtract")
    public ResponseEntity<Object> subtractProduct(
            @PathVariable("id") int cartid, 
            @RequestParam("p") int productid, 
            @RequestParam("q") int quantity) {
        return new ResponseEntity<>(cartService.subtractProduct(cartid, productid, quantity), HttpStatus.OK);
    }

    // Calculate and return the total cost for the cart
    @GetMapping("cart/{id}/cost")
    public Double getCartCost(@PathVariable("id") int cartid) {
        return cartService.getCartCost(cartid);
    }
}
