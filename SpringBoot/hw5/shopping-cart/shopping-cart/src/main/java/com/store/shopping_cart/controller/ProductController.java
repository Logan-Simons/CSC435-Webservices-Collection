package com.store.shopping_cart.controller;

import java.util.List;

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

    @PutMapping(value = "/products/{id}")
    public Product updateProduct(@PathVariable Integer id, @RequestBody Product product) {
        return productService.updateProduct(id, product);
     
    }

    @DeleteMapping(value = "/products/{id}")
    public void deleteProduct(@PathVariable Integer id) {
        productService.deleteProduct(id);

    }

    @PostMapping(value = "/products/create/")
    public Product createProduct(@RequestBody Product product) {

      return productService.createProduct(product);
    }

    @GetMapping(value = "/products/id/{id}")
    public Product findProductByID(@PathVariable int id) {
        return productService.getProduct(id);
    }

}
