package com.store.shopping_cart.service;

import java.util.Collection;
import java.util.HashMap;

import org.springframework.stereotype.Service;

import com.store.shopping_cart.model.Product;

@Service
public class ProductServiceImpl implements ProductService {

    private HashMap<Integer, Product> productMap = new HashMap<>();

    @Override
    public void createProduct(Product product) {
        productMap.put(product.getProductid(), product);
    }

    @Override
    public void updateProduct(int id, Product product) {
        productMap.put(product.getProductid(), product);
    }

    @Override
    public void deleteProduct(int id) {
        productMap.remove(productMap.get(id));
    }

    @Override
    public Collection<Product> getProducts() {


        return productMap.values();

    }
    
}
