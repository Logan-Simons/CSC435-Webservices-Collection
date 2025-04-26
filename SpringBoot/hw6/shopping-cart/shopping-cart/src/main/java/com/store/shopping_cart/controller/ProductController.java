package com.store.shopping_cart.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.store.shopping_cart.model.Product;
import com.store.shopping_cart.service.ProductService;

@RestController
@RequestMapping("/store")
public class ProductController {
    
    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }
    
    @GetMapping(value = "/products")
    public List<Product> getProducts() {
        return productService.getProducts();
    }

    @PutMapping(value = "/products/{id}/")
    public ResponseEntity<Object> updateProduct(@PathVariable("id") Integer id, @RequestBody Product product) {
        
        if (productService.productExists(id)) {
            productService.updateProduct(id, product);
            product = productService.getProduct(id);
            return new ResponseEntity<>(product, HttpStatus.OK);
        }
        
        return new ResponseEntity<>("Product does not exist", HttpStatus.NO_CONTENT);
     
    }

    @DeleteMapping(value = "/products/{id}")
    public ResponseEntity<Object> deleteProduct(@PathVariable Integer id) {
         if (productService.productExists(id)) {
          productService.deleteProduct(id);
            return new ResponseEntity<>("Product " + id + " has been deleted.", HttpStatus.OK);
        }

        return new ResponseEntity<>("Product does not exist", HttpStatus.NOT_FOUND);

    }

    @PostMapping(value = "/products/")
    public Product createProduct(@RequestBody Product product) {

      return productService.createProduct(product);
    }

    @GetMapping(value = "/products/{id}")
    public ResponseEntity<Object> findProductByID(@PathVariable("id") int id) {

         if (productService.productExists(id)) {
          Product product = productService.getProduct(id);
          return new ResponseEntity<>(product, HttpStatus.OK);
        }
        
        return new ResponseEntity<>("Product does not exist", HttpStatus.NOT_FOUND);

    
    }

}
