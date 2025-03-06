package com.store;

public class Product {

    private int ID;
    private String name;
    private String description;
    private double cost;

    public Product(int id, String name, String description, double cost) {

        this.ID = id;
        this.name = name;
        this.description = description;
        this.cost = cost;

    }

    public void setProductName(String name) {
        this.name = name;
    }

    public void setProductCost(double cost) {
        this.cost = cost;
    }

    public void setProductDescription(String description) {
        this.description = description;
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

    public String getProductDescription() {
        return this.description;
    }

}
