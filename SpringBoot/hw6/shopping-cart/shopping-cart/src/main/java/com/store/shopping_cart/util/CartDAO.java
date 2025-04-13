package com.store.shopping_cart.util;

import java.util.List;

import org.jdbi.v3.sqlobject.config.RegisterConstructorMapper;
import org.jdbi.v3.sqlobject.customizer.Bind;
import org.jdbi.v3.sqlobject.statement.SqlQuery;
import org.jdbi.v3.sqlobject.statement.SqlUpdate;

import com.store.shopping_cart.model.Cart;
import com.store.shopping_cart.model.CartItem;


@RegisterConstructorMapper(Cart.class)
@RegisterConstructorMapper(CartItem.class)
public interface  CartDAO {

    @SqlQuery("SELECT cartid, productid, SUM(quantity) AS total_quantity FROM cart_products WHERE cartid = :cartid GROUP BY cartid, productid")
    List<CartItem> getCartProducts(int cartid);

    @SqlUpdate("INSERT INTO cart_products (cartid, productid, quantity) VALUES (:cartid, :productid, :quantity)")
    int addProduct(int cartid, int productid, int quantity);

    @SqlUpdate("UPDATE cart_products SET quantity = quantity - :quantity " +
           "WHERE cartid = :cartid AND productid = :productid")
int subtractQuantity(@Bind("cartid") int cartid, 
                   @Bind("productid") int productid, 
                   @Bind("quantity") int quantity);

@SqlUpdate("DELETE FROM cart_products WHERE cartid = :cartid AND productid = :productid")
int deleteCartProduct(@Bind("cartid") int cartid, @Bind("productid") int productid);
}
