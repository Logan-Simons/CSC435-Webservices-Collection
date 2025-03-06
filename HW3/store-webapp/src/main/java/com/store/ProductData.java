package com.store;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;

public class ProductData {

    public HashMap<Integer, Product> getProducts() {
        String sql = "SELECT * FROM product";
        HashMap<Integer, Product> initialProducts = new HashMap<>();

        try (Connection conn = DatabaseUtil.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql);
                ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                int productID = rs.getInt("id");
                String name = rs.getString("name");
                String description = rs.getString("description");
                double price = rs.getDouble("price"); // Ensure this column exists

                // Assuming Product constructor includes description
                Product product = new Product(productID, name, description, price);
                initialProducts.put(productID, product);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return initialProducts;
    }
}
