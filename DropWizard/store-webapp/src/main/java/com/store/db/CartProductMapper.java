package com.store.db;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.jdbi.v3.core.mapper.RowMapper;
import org.jdbi.v3.core.statement.StatementContext;

import com.store.core.CartProducts;
import com.store.core.Product;

public class CartProductMapper implements RowMapper<CartProducts> {

    @Override
    public CartProducts map(ResultSet rs, StatementContext ctx) throws SQLException {
        // create CartProducts object
        CartProducts cp = new CartProducts();
        // define cartID
        cp.setCartid(rs.getInt("cartid"));
        // create a new Product, read from result set and set product values
        Product product = new Product();
        product.setId(rs.getInt("p_productid"));
        product.setName(rs.getString("name"));
        product.setDescription(rs.getString("description"));
        product.setPrice(rs.getDouble("price"));
        cp.setProduct(product);
        
        cp.setQuantity(rs.getInt("total_quantity"));
        return cp;
    }
}
