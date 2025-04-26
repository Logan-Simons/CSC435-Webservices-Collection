package com.store.shopping_cart.service;


import java.util.List;

import com.store.shopping_cart.model.Product;

public interface ProductService {
    public abstract void createProduct(Product product);
    public abstract void updateProduct(int id, Product product);
    public abstract void deleteProduct(int id);
    public abstract List<Product> getProducts();
    public abstract Product getProductByID(int id);
}
