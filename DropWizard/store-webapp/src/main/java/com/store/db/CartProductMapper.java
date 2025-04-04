package com.store.db;

import com.store.core.Product;
import org.jdbi.v3.core.mapper.RowMapper;
import org.jdbi.v3.core.statement.StatementContext;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.AbstractMap;
import java.util.Map;

public class CartProductMapper implements RowMapper<Map.Entry<Product, Integer>> {
    @Override
    public Map.Entry<Product, Integer> map(ResultSet rs, StatementContext ctx) throws SQLException {
        Product product = new Product();
        // Adjust these if needed:
        product.setId(rs.getInt("productid"));
        // If you have more fields, set them accordingly:
        // product.setName(rs.getString("name"));
        // product.setDescription(rs.getString("description"));
        // product.setPrice(rs.getDouble("price"));
        
        int quantity = rs.getInt("total_quantity");
        return new AbstractMap.SimpleEntry<>(product, quantity);
    }
}