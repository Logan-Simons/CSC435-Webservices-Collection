package com.store.shopping_cart.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.store.shopping_cart.model.Cart;

public interface CartRepository extends JpaRepository<Cart, Integer> {

}
