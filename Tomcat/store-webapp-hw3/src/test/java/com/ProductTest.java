package com;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.store.Product;

import com.store.DatabaseUtil;

public class ProductTest {

    public static void main(String[] args) {
        try (Connection conn = DatabaseUtil.getConnection()) {
            if (conn != null && !conn.isClosed()) {
                System.out.println("Connection successful!");
                testCreateProduct();
            } else {
                System.out.println("Failed to establish connection.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("SQLException: " + e.getMessage());
        }
    }

    public static void testGetProducts() {
        String sql = "SELECT * FROM product";

        try (Connection conn = DatabaseUtil.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql);
                ResultSet rs = ps.executeQuery()) {
            System.out.println(rs);

            while (rs.next()) {
                int productID = rs.getInt("productID");
                String name = rs.getString("name");
                String description = rs.getString("description");
                double price = rs.getDouble("price");

                // Assuming Product constructor includes description
                Product product = new Product(productID, name, description, price);
                product.print();
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    public static void testCreateProduct() {
        String sql = "INSERT INTO product (productid, name, description, price) VALUES (?, ?, ?, ?)";

        Product product = new Product(99, "Test Name", "Test Description", 29.99);

        try (Connection conn = DatabaseUtil.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, product.getProductID());
            ps.setString(2, product.getProductName());
            ps.setString(3, product.getProductDescription());
            ps.setDouble(4, product.getProductCost());

            int rowsAffected = ps.executeUpdate();
            System.out.println(rowsAffected + "row(s) affected");

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void testDeleteProduct(int id) {

        String sql = "DELETE FROM product WHERE productid = ?";
        try (Connection conn = DatabaseUtil.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, 99);

            int rowsAffected = ps.executeUpdate();
            System.out.println(rowsAffected + "row(s) affected");
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    public static void testUpdateProduct() {
        String sql = "UPDATE product SET name = ?, description = ?, price = ? WHERE productid = ?";

        Product product = new Product(1, "Updated", "Updated Description", 59.99);

        try (Connection conn = DatabaseUtil.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, product.getProductName());
            ps.setString(2, product.getProductDescription());
            ps.setDouble(3, product.getProductCost());
            ps.setInt(4, product.getProductID());

            int rowsAffected = ps.executeUpdate();
            System.out.println(rowsAffected + "row(s) affected");
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }
}
