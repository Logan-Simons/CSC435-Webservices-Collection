package com.store;

import java.util.HashMap;

public class ProductUtil {

    public HashMap<Integer, Product> getProducts() {
        ProductData data = new ProductData();
        HashMap<Integer, Product> dataMap = data.getProducts();
        return dataMap;
    }
    // Add Product
    // Remove Product
    // Update Product
}
