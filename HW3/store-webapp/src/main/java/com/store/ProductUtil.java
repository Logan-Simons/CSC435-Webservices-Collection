package com.store;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;

public class ProductUtil {

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

    public boolean createProduct(Product product) {
        String sql = "INSERT INTO product (name, description, price) VALUES ( ?, ?, ?)";
        try (Connection conn = DatabaseUtil.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setString(1, product.getProductName());
                ps.setString(2, product.getProductDescription());
                ps.setDouble(3, product.getProductCost());

                int rowsAffected = ps.executeUpdate();
                return rowsAffected > 0; // checks to see if the product was successfully added
            } catch (SQLException e)  {
                e.printStackTrace();
            }
            return false;
    }

    public boolean updateProduct(Product product) {
        String sql = "UPDATE product SET name = ?, description = ?, price = ? WHERE productID = ?";
        try (Connection conn = DatabaseUtil.getConnection(); 
            PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setString(1, product.getProductName());
                ps.setString(2, product.getProductDescription());
                ps.setDouble(3, product.getProductCost());

                int rowsAffected = ps.executeUpdate();
                return rowsAffected > 0;
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return false;
    }

    public boolean deleteProduct(int productID) {
        String sql = "DELETE FROM product WHERE id = ?";
        try (Connection conn = DatabaseUtil.getConnection(); 
            PreparedStatement ps = conn.prepareStatement(sql)) {
             ps.setInt(1, productID);

             int rowsAffected = ps.executeUpdate();
             return rowsAffected > 0;
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return false;
    }
    
}
