package com.store.shopping_cart.model;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "product")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer productid;

    @Column(nullable = false, length = 32)
    private String name;

    @Column(nullable = true, length = 256)
    private String description;

    @Column(nullable = false)
    private double price;

    public Product() {

    }

    public Product(int productid, String name, String description, double price) {
        this.productid = productid;
        this.name = name;
        this.description = description;
        this.price = price;
    }

    public Integer getProductid() {
        return productid;
    }
    public void setProductid(int productid) {
        this.productid = productid;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    public double getPrice() {
        return price;
    }
    public void setPrice(double price) {
        this.price = price;
    }


    
}
