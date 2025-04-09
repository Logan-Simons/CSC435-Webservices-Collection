package com.store.shopping_cart.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.store.shopping_cart.model.Product;
import com.store.shopping_cart.util.ProductDAO;

@Service
public class ProductServiceImpl implements ProductService {

    private ProductDAO productDAO;

    @Override
    public void createProduct(Product product) {
        productDAO.insertProduct(product.getName(), product.getDescription(), product.getPrice());
    }

    @Override
    public void updateProduct(int productid, Product product) {
        productDAO.updateProduct(productid, product.getName(), product.getDescription(), product.getPrice());
    }

    @Override
    public void deleteProduct(int productid) {
        productDAO.deleteProduct(productid);
    }

    @Override
    public List<Product> getProducts() {

        return productDAO.getAllProducts();

    }

    @Override
    public Product getProductByID(int productid) {
        return productDAO.findProductById(productid);
    }
    
}
