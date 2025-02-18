package com.store;

import java.util.ArrayList;

public class Cart {

    //
    private String cartName;
    private ArrayList<Product> cartProducts;
    private ArrayList<Integer> productQuantities;

    public Cart(String name) {

        this.cartName = name;
        this.cartProducts = new ArrayList<>();
        this.productQuantities = new ArrayList<>();

    }

    public void addProduct(Product product) {

        if (cartProducts.contains(product)) {
            int productIndex = productToID(product);
            int currentStock = productQuantities.get(productIndex);
            productQuantities.set(productIndex, currentStock++);
        } else {
            cartProducts.add(product);
            productQuantities.add(0);
            // lazy way to implement this
        }

    }

    public void removeProduct(Product product) {

        if (cartProducts.contains(product)) {
            int productIndex = productToID(product);
            int currentStock = productQuantities.get(productIndex);
            if (currentStock > 0) {
                productQuantities.set(productIndex, currentStock--);
            } else {
                System.out.println("cannot subtract from 0");
            }
        } else {
            System.out.println("error removing product");
        }
    }

    public void setProducts(ArrayList<Product> products) {
        this.cartProducts = products;
    }

    public int getCartSize() {
        return this.cartProducts.size();
    }

    public Product getProductByIndex(int index) {
        return this.cartProducts.get(index);
    }

    public int getProductIndexQuantity(int index) {
        return productQuantities.get(index);
    }

    private int productToID(Product findProduct) {

        for (int i = 0; i < cartProducts.size(); i++) {
            if (cartProducts.get(i) == findProduct) {
                return i;
            }
        }

        return -1;

    }

    public ArrayList<Product> getProducts() {
        return this.cartProducts;
        return this.productQuantities;
    }

}
