package com.store;

import java.util.HashMap;
import com.store.Product;

public class ProductData {

    public HashMap<Integer, Product> initialProducts = new HashMap<>();

    public HashMap<Integer, Product> getInitialProducts() {

        // Mock Generated Products
        initialProducts.put(1, new Product(1, "Powered Speaker", 50.00));
        initialProducts.put(3, new Product(2, "Wireless Speaker (12in)", 30.00));
        initialProducts.put(4, new Product(3, "Subwoofer (18in)", 60.00));
        initialProducts.put(5, new Product(4, "Wireless Handheld Microphone", 25.00));
        initialProducts.put(7, new Product(5, "Wired Dynamic Microphone", 15.00));
        initialProducts.put(8, new Product(6, "Condenser Microphone", 40.00));
        initialProducts.put(9, new Product(7, "Microphone Stand", 10.00));
        initialProducts.put(10, new Product(8, "Boom Microphone Stand", 12.00));
        initialProducts.put(11, new Product(9, "Digital Mixer (8-Channel)", 50.00));
        initialProducts.put(12, new Product(10, "Bluetooth Audio Receiver", 20.00));

        return initialProducts;
    }
}