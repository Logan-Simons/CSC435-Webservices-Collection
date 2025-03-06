package com.store;

public class Product {

    private int ID;
    private String name;
    private double cost;

    public Product(int id, String name, double cost) {

        this.ID = id;
        this.name = name;
        this.cost = cost;

    }

    public void setProductName(String name) {
        this.name = name;
    }

    public void setProductCost(double cost) {
        this.cost = cost;
    }

    public int getProductID() {
        return this.ID;
    }

    public String getProductName() {
        return this.name;
    }

    public double getProductCost() {
        return this.cost;
    }

}
