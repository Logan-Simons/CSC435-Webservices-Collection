package com.store.shopping_cart.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.store.shopping_cart.model.Product;
import com.store.shopping_cart.service.ProductService;

@RestController
public class ProductServiceController {
    @Autowired
    ProductService productService;
    
    @GetMapping(value = "/products")
    public ResponseEntity<Object> getProducts() {
        return new ResponseEntity<>(productService.getProducts(), HttpStatus.OK);
    }

    @PutMapping(value = "/products/{id}")
    public ResponseEntity<Object> updateProduct(@PathVariable Integer id, @RequestBody Product product) {
        productService.updateProduct(id, product);
        String productName = product.getName();

        return new ResponseEntity<>("Product " + productName + " is updated successfuly", HttpStatus.OK);
    }

    @DeleteMapping(value = "/products/{id}")
    public ResponseEntity<Object> deleteProduct(@PathVariable Integer id) {
        productService.deleteProduct(id);

        return new ResponseEntity<>("Product deleted successfully", HttpStatus.OK);
    }

    @PostMapping(value = "/products/create/")
    public ResponseEntity<Object> createProduct(@RequestBody Product product) {

        if (productService.getProducts().contains(product)) {
            return new ResponseEntity<>("Product with ID already exists.", HttpStatus.CONFLICT);
        }

        productService.createProduct(product);
        return new ResponseEntity<>(product, HttpStatus.OK);
    }

    @GetMapping(value = "/products/id/{id}")
    public ResponseEntity<Object> findProductByID(@PathVariable int id) {
        return new ResponseEntity<>(productService.getProductByID(id), HttpStatus.OK);
    }

}
