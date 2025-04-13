package com.store.shopping_cart.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.store.shopping_cart.service.CartService;

@RestController
public class CartServiceController {
    
    @Autowired
    CartService cartService;

    @GetMapping(value = "/cart/{id}")
    public ResponseEntity<Object> getCart(@PathVariable int cartid) {
        return new ResponseEntity<>(cartService.getCartProducts(cartid), HttpStatus.OK);
    }

     @PostMapping("/cart/{id}/add")
    public ResponseEntity<Object> addProduct(
            @PathVariable("id") int cartid, 
            @RequestParam("p") int productid, 
            @RequestParam("q") int quantity) {
        return new ResponseEntity<>(cartService.addProduct(cartid, productid, quantity), HttpStatus.OK);
    }

    @PutMapping("/cart/{id}/subtract")
    public ResponseEntity<Object> subtractProduct(
            @PathVariable("id") int cartid, 
            @RequestParam("p") int productid, 
            @RequestParam("q") int quantity) {
        return new ResponseEntity<>(cartService.subtractProduct(cartid, productid, quantity), HttpStatus.OK);
    } 

    @GetMapping(value = "/cart/{id}/cost")
     public ResponseEntity<Object> getCartCost(@PathVariable int cartid) {
        return new ResponseEntity<>(cartService.getCartCost(cartid), HttpStatus.OK);
    }

}



