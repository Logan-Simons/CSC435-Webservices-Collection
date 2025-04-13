package com.store.shopping_cart.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.store.shopping_cart.model.CartItem;
import com.store.shopping_cart.util.CartDAO;

@Service
public class CartServiceImpl implements CartService {



    private CartDAO cartDAO;


  
    

    @Override 
    public List<CartItem> getCartProducts(int cartid) {

        return cartDAO.getCartProducts(cartid);

    }


    @Override
    public int addProduct(int cartid, int productid, int quantity) {

      return cartDAO.addProduct(cartid, productid, quantity);


    }

    @Override
    public int subtractProduct(int cartid, int productid, int quantity) {


        List<CartItem> cartItems = cartDAO.getCartProducts(cartid);
        int itemQuantity = 0;
        int finalQuantity = 0;
        for (CartItem item : cartItems) {
            if (item.getProduct().getProductid() == productid) {
                itemQuantity = item.getQuantity();
                if (itemQuantity - quantity <= 0) {
                    cartDAO.deleteCartProduct(cartid, productid);
                } else {
                    finalQuantity = itemQuantity - quantity;
                    cartDAO.subtractQuantity(cartid, productid, finalQuantity);
                    
                }
            }
        }


        return finalQuantity;

    }

    
    @Override
    public double getCartCost(int cartid) {

        
        double cost = 0;
        for (CartItem cartItem : cartDAO.getCartProducts(cartid)) {
            int quantity = cartItem.getQuantity();
            cost =+ (cartItem.getProduct().getPrice() * quantity);
        }
        return cost;

    }



       
}
