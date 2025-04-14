package com.store.shopping_cart.service;


import java.util.List;

import org.springframework.stereotype.Service;

import com.store.shopping_cart.model.Product;
import com.store.shopping_cart.repo.ProductRepository;

@Service
public class ProductService {

    private final ProductRepository productRepo;

    public ProductService(ProductRepository repo) {
        this.productRepo = repo;
    }

        public Product getProduct(Integer id) {
        return productRepo.findById(id)
            .orElseThrow(() -> new RuntimeException("Product not found"));
    }

    public List<Product> getProducts() {

        return productRepo.findAll();

    }

    public boolean productExists(int productid) {
        return productRepo.existsById(productid);
    }

    public Product createProduct(Product product) {
        return productRepo.save(product);
     }

    

    public Product updateProduct(int productid, Product product) {
       Product existing = getProduct(productid);
       existing.setName(product.getName());
       existing.setDescription(product.getDescription());
       existing.setPrice(product.getPrice());
       return productRepo.save(existing);
    }

    
    public void deleteProduct(int productid) {
        productRepo.deleteById(productid);
    }

    
    

}

