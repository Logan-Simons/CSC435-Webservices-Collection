package com.store.shopping_cart.util;

import org.jdbi.v3.sqlobject.config.RegisterConstructorMapper;

import com.store.shopping_cart.model.Cart;


@RegisterConstructorMapper(Cart.class)
public interface  CartDAO {


   
}
