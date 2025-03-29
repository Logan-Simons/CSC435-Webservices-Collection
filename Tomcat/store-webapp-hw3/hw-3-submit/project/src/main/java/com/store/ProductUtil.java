package com.store;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;

public class ProductUtil {

    public HashMap<Integer, Product> getProducts() {
        String sql = "SELECT * FROM product";
        HashMap<Integer, Product> products = new HashMap<>();

        try (Connection conn = DatabaseUtil.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql);
                ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                int productID = rs.getInt("productid");
                String name = rs.getString("name");
                String description = rs.getString("description");
                double price = rs.getDouble("price");

                // Assuming Product constructor includes description
                Product product = new Product(productID, name, description, price);
                products.put(productID, product);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return products;
    }

    public boolean createProduct(Product product) {
        String sql = "INSERT INTO product (productid, name, description, price) VALUES (?,  ?, ?, ?)";
        try (Connection conn = DatabaseUtil.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, product.getProductID());
            ps.setString(2, product.getProductName());
            ps.setString(3, product.getProductDescription());
            ps.setDouble(4, product.getProductCost());

            int rowsAffected = ps.executeUpdate();
            return rowsAffected > 0; // checks to see if the product was successfully added
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean updateProduct(Product product) {
        String sql = "UPDATE product SET name = ?, description = ?, price = ? WHERE productid = ?";
        try (Connection conn = DatabaseUtil.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, product.getProductName());
            ps.setString(2, product.getProductDescription());
            ps.setDouble(3, product.getProductCost());
            // assign updated productid
            ps.setInt(4, product.getProductID());

            int rowsAffected = ps.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public Product getProduct(int id) {
        String sql = "SELECT * FROM product WHERE productid = ?";
        // Product product = new Product(-1, "-1", "-1", -1);
        try (Connection conn = DatabaseUtil.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {

                int productID = rs.getInt("productid");
                String name = rs.getString("name");
                String description = rs.getString("description");
                double price = rs.getDouble("price");
                Product product = new Product(productID, name, description, price);
                return product;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    public boolean deleteProduct(int productID) {
        String sql = "DELETE FROM product WHERE productid = ?";
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
