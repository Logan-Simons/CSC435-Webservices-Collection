package com.store.shopping_cart.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
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
import com.store.shopping_cart.service.ProductService;

@RestController
@RequestMapping("/store")
public class CartController {

    @Autowired
    private CartService cartService;
    @Autowired
    private ProductService productService;

    @PostMapping("/cart/")
    public ResponseEntity<Object> createCart() {
        Cart cart = cartService.createCart();
        return new ResponseEntity<>("New cart created " + cart.getCartId(), HttpStatus.OK);
    }

    // Get the contents of a cart by its id
    @GetMapping("/cart/{id}")
    public ResponseEntity<Object> getCart(@PathVariable("id") Integer cartid) {

        if (cartService.cartExists(cartid)) {
            Cart cart = cartService.getCart(cartid);
            return new ResponseEntity<>(cart, HttpStatus.OK);
        }
        return new ResponseEntity<>("Cart " + cartid + " does not exist.", HttpStatus.NOT_FOUND);

    }

    public List<CartItem> getCartItems(Cart cart) {
        return cart.getItems();
    }

    // Add a product to the specified cart
    @PutMapping("/cart/{id}/add")
    public ResponseEntity<Object> addProduct(
            @PathVariable("id") int cartid,
            @RequestParam("p") int productid,
            @RequestParam("q") int quantity) {
        if (!productService.productExists(productid)) {
            return new ResponseEntity<>("Product does not exists", HttpStatus.NOT_FOUND);
        } else if (!cartService.cartExists(cartid)) {
            return new ResponseEntity<>("Cart does not exists", HttpStatus.NOT_FOUND);
        }

        cartService.addProduct(cartid, productid, quantity);
        Cart cart = cartService.getCart(cartid);
        return new ResponseEntity<>(cart, HttpStatus.OK);
    }

    // Subtract a product from the specified cart
    @PutMapping("/cart/{id}/sub")
    public ResponseEntity<Object> subtractProduct(
            @PathVariable("id") int cartid,
            @RequestParam("p") int productid,
            @RequestParam("q") int quantity) {

        if (!productService.productExists(productid)) {
            return new ResponseEntity<>("Product does not exist", HttpStatus.NOT_FOUND);
        } else if (!cartService.cartExists(cartid)) {
            return new ResponseEntity<>("Cart does not exist", HttpStatus.NOT_FOUND);
        }
        cartService.subtractProduct(cartid, productid, quantity);
        Cart cart = cartService.getCart(cartid);
        return new ResponseEntity<>(cart, HttpStatus.OK);
    }

    @GetMapping("/cart/{id}/cost")
    public ResponseEntity<Object> getCartCost(@PathVariable("id") int cartid) {
        if (!cartService.cartExists(cartid)) {
            return new ResponseEntity<>("Cart does not exist", HttpStatus.NOT_FOUND);
        }

        double cost = cartService.getCartCost(cartid);
        return new ResponseEntity<>("Total $" + cost, HttpStatus.OK);
    }

    @DeleteMapping("cart/{id}/clear")
    public ResponseEntity<Object> clearCart(@PathVariable("id") int cartid) {
        if (!cartService.cartExists(cartid)) {
            return new ResponseEntity<>("Cart does not exist", HttpStatus.NOT_FOUND);
        }

        Cart cart = cartService.clearCart(cartid);
        return new ResponseEntity<>(cart, HttpStatus.OK);
    }

    @DeleteMapping("cart/{id}/delete")
    public ResponseEntity<Object> deleteCart(@PathVariable("id") int cartid) {
        if (!cartService.cartExists(cartid)) {
            return new ResponseEntity<>("Cart does not exist", HttpStatus.NOT_FOUND);
        }
        cartService.deleteCart(cartid);
        return new ResponseEntity<>("Cart " + cartid + " has been deleted.", HttpStatus.OK);
    }
}
