package com.store.db;

import java.util.List;
import java.util.Map;

import org.jdbi.v3.sqlobject.SqlObject;
import org.jdbi.v3.sqlobject.config.RegisterRowMapper;
import org.jdbi.v3.sqlobject.customizer.Bind;
import org.jdbi.v3.sqlobject.statement.GetGeneratedKeys;
import org.jdbi.v3.sqlobject.statement.SqlQuery;
import org.jdbi.v3.sqlobject.statement.SqlUpdate;

import com.store.core.Product;

public interface CartDAO extends SqlObject {

     @SqlQuery("SELECT cartid, productid, SUM(quantity) AS total_quantity " +
          "FROM cart_products " +
          "WHERE cartid = :cartid " +
          "GROUP BY cartid, productid")
    @RegisterRowMapper(CartProductMapper.class)
    List<Map.Entry<Product, Integer>> getCartContentsEntries(@Bind("cartid") int cartid);
    
    @SqlUpdate("INSERT INTO cart DEFAULT VALUES")
    @GetGeneratedKeys
    int newCart();

     @SqlQuery("SELECT quantity FROM cart_products WHERE cartid = :cartid AND productid = :productid")
    Integer getProductQuantityFromCart(@Bind("cartid") int cartid, @Bind("productid") int productid);

     @SqlQuery("SELECT COALESCE(SUM(quantity), 0) FROM cart_products WHERE cartid = :cartid")
    int getCartSize(@Bind("cartid") int cartid);

     @SqlQuery("SELECT COALESCE(SUM(p.price * cp.quantity), 0) " +
              "FROM cart_products cp " +
              "JOIN products p ON cp.productid = p.id " +
              "WHERE cp.cartid = :cartid")
    double getCartCost(@Bind("cartid") int cartid);

    // Add a product to the cart.
    @SqlUpdate("UPDATE cart_products SET quantity = quantity + :quantity " +
               "WHERE cartid = :cartid AND productid = :productid")
    int updateProductQuantity(@Bind("cartid") int cartid,
                              @Bind("productid") int productid,
                              @Bind("quantity") int quantity);

    // If update didn't affect any rows, insert a new row.
    @SqlUpdate("INSERT INTO cart_products (cartid, productid, quantity) VALUES (:cartid, :productid, :quantity)")
    int insertProductIntoCart(@Bind("cartid") int cartid,
                              @Bind("productid") int productid,
                              @Bind("quantity") int quantity);

    // Default method that mimics your addProductToCart logic.
    default void addProductToCart(int cartid, int productid, int quantity) {
        // Try updating first
        int rows = updateProductQuantity(cartid, productid, quantity);
        if (rows == 0) {
            // If no rows were updated, insert a new record.
            insertProductIntoCart(cartid, productid, quantity);
        }
    }

    // To subtract a product from the cart, first fetch the current quantity.
    @SqlQuery("SELECT quantity FROM cart_products WHERE cartid = :cartid AND productid = :productid")
    Integer fetchQuantity(@Bind("cartid") int cartid, @Bind("productid") int productid);


    // Update the product quantity to a new value.
    @SqlUpdate("UPDATE cart_products SET quantity = :newQuantity WHERE cartid = :cartid AND productid = :productid")
    int updateQuantity(@Bind("cartid") int cartid,
                       @Bind("productid") int productid,
                       @Bind("newQuantity") int newQuantity);


    // Delete a product from the cart.
    @SqlUpdate("DELETE FROM cart_products WHERE cartid = :cartid AND productid = :productid")
    int deleteProductFromCart(@Bind("cartid") int cartid, @Bind("productid") int productid);

    // Default method to subtract (decrement) a product from the cart.
    default void subtractProductFromCart(int cartid, int productid) {
        Integer currentQty = fetchQuantity(cartid, productid);
        if (currentQty != null && currentQty > 0) {
            int newQty = currentQty - 1;
            if (newQty > 0) {
                updateQuantity(cartid, productid, newQty);
            } else {
                deleteProductFromCart(cartid, productid);
            }
        }
    }

        @SqlUpdate("DELETE FROM cart_products WHERE cartid = :cartid")
    void clearCart(@Bind("cartid") int cartid);

}