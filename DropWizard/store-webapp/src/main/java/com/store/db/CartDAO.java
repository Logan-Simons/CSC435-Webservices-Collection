package com.store.db;

import java.util.HashMap;

import org.jdbi.v3.sqlobject.SqlObject;
import org.jdbi.v3.sqlobject.config.RegisterBeanMapper;
import org.jdbi.v3.sqlobject.customizer.Bind;
import org.jdbi.v3.sqlobject.statement.SqlQuery;
import org.jdbi.v3.sqlobject.statement.SqlUpdate;

import com.store.core.CartProducts;
import com.store.core.Product;

public interface CartDAO extends SqlObject {

     @SqlQuery("SELECT cartid, productid, SUM(quantity) AS total_quantity " +
              "FROM cart_products " +
              "WHERE cartid = :cartId " +
              "GROUP BY cartid, productid")
    @RegisterBeanMapper(CartProducts.class)
    HashMap<Product, Integer> getCartContents(@Bind("cartId") int cartId);
    
     @SqlQuery("SELECT quantity FROM cart_products WHERE cartid = :cartId AND productid = :productId")
    Integer getProductQuantityFromCart(@Bind("cartId") int cartId, @Bind("productId") int productId);

     @SqlQuery("SELECT COALESCE(SUM(quantity), 0) FROM cart_products WHERE cartid = :cartId")
    int getCartSize(@Bind("cartId") int cartId);

     @SqlQuery("SELECT COALESCE(SUM(p.price * cp.quantity), 0) " +
              "FROM cart_products cp " +
              "JOIN products p ON cp.productid = p.id " +
              "WHERE cp.cartid = :cartId")
    double getCartCost(@Bind("cartId") int cartId);

    // Add a product to the cart.
    @SqlUpdate("UPDATE cart_products SET quantity = quantity + :quantity " +
               "WHERE cartid = :cartId AND productid = :productId")
    int updateProductQuantity(@Bind("cartId") int cartId,
                              @Bind("productId") int productId,
                              @Bind("quantity") int quantity);

    // If update didn't affect any rows, insert a new row.
    @SqlUpdate("INSERT INTO cart_products (cartid, productid, quantity) VALUES (:cartId, :productId, :quantity)")
    int insertProductIntoCart(@Bind("cartId") int cartId,
                              @Bind("productId") int productId,
                              @Bind("quantity") int quantity);

    // Default method that mimics your addProductToCart logic.
    default void addProductToCart(int cartId, Product product, int quantity) {
        // Try updating first
        int rows = updateProductQuantity(cartId, product.getId(), quantity);
        if (rows == 0) {
            // If no rows were updated, insert a new record.
            insertProductIntoCart(cartId, product.getId(), quantity);
        }
    }

    // To subtract a product from the cart, first fetch the current quantity.
    @SqlQuery("SELECT quantity FROM cart_products WHERE cartid = :cartId AND productid = :productId")
    Integer fetchQuantity(@Bind("cartId") int cartId, @Bind("productId") int productId);


    // Update the product quantity to a new value.
    @SqlUpdate("UPDATE cart_products SET quantity = :newQuantity WHERE cartid = :cartId AND productid = :productId")
    int updateQuantity(@Bind("cartId") int cartId,
                       @Bind("productId") int productId,
                       @Bind("newQuantity") int newQuantity);


    // Delete a product from the cart.
    @SqlUpdate("DELETE FROM cart_products WHERE cartid = :cartId AND productid = :productId")
    int deleteProductFromCart(@Bind("cartId") int cartId, @Bind("productId") int productId);

    // Default method to subtract (decrement) a product from the cart.
    default void subtractProductFromCart(int cartId, Product product) {
        Integer currentQty = fetchQuantity(cartId, product.getId());
        if (currentQty != null && currentQty > 0) {
            int newQty = currentQty - 1;
            if (newQty > 0) {
                updateQuantity(cartId, product.getId(), newQty);
            } else {
                deleteProductFromCart(cartId, product.getId());
            }
        }
    }

        @SqlUpdate("DELETE FROM cart_products WHERE cartid = :cartId")
    void clearCart(@Bind("cartId") int cartId);

}