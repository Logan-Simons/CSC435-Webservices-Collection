package com.store.shopping_cart.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.store.shopping_cart.model.Cart;
import com.store.shopping_cart.model.Product;
import com.store.shopping_cart.model.CartItem;

@Service
public class CartServiceImpl implements CartService {


    private Cart Cart;

    @Override
    public void setCart(int id){
        this.Cart = new Cart(id);
    }

    @Override
    public void setCartProducts(int cartid, List<CartItem> CartProducts) {

        this.Cart.setCartProducts(CartProducts);


    }

    @Override 
    public List<CartItem> getCartProducts(int cartid) {

        return this.Cart.getCartProducts();

    }


    @Override
    public int addProduct(int cartid, Product product, int quantity) {

      


    }

    @Override
    public int removeProduct(int cartid, Product product, int quantity) {

        int productQuantity = Cart.getCartProducts().get(product);
        int targetQuantity = productQuantity - quantity;

        if (targetQuantity < 0) {
            return 0;
        }

        return targetQuantity;

    }

    @Override
    public double getCartCost(int cartid) {

        
        double cost = 0;
        for (Product product : this.Cart.getCartProducts().keySet() ) {
            int quantity = Cart.getCartProducts().get(product);
            cost =+ (product.getPrice() * quantity);
        }
        return cost;

    }



       
}
