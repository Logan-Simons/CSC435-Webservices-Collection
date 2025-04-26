package com.store.shopping_cart.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.store.shopping_cart.model.Product;


public interface ProductRepository extends JpaRepository<Product, Integer> {
    

}
