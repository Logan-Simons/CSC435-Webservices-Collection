package com.store.shopping_cart.util;
import org.jdbi.v3.core.mapper.RowMapper;
import org.jdbi.v3.core.statement.StatementContext;

import java.sql.ResultSet;
import java.sql.SQLException;

import com.store.shopping_cart.model.CartItem;
import com.store.shopping_cart.model.Product;

public class CartItemMapper implements RowMapper<CartItem> {

    @Override
    public CartItem map(ResultSet rs, StatementContext ctx) throws SQLException {
        // Construct a Product from the result set.
        // Ensure your column names match those in your query.
        Product product = new Product(
            rs.getInt("productid"),
            rs.getString("name"),
            rs.getString("description"),
            rs.getDouble("price")
        );
        int quantity = rs.getInt("quantity");

        return new CartItem(product, quantity);
    }
}
