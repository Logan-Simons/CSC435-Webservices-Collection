package com.store.shopping_cart.service;


import java.util.Collection;

import com.store.shopping_cart.model.Product;

public interface ProductService {
    public abstract void createProduct(Product product);
    public abstract void updateProduct(int id, Product product);
    public abstract void deleteProduct(int id);
    public abstract Collection<Product> getProducts();
}
