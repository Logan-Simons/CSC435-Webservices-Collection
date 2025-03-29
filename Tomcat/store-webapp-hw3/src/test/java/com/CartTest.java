package com;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;

import com.store.DatabaseUtil;
import com.store.Product;
import com.store.ProductUtil;

public class CartTest {

    private static ProductUtil productUtil = new ProductUtil();

    public static void main(String[] args) {
        try (Connection conn = DatabaseUtil.getConnection()) {
            if (conn != null && !conn.isClosed()) {
                System.out.println("Connection successful!");
                testGetCartContents(1);
            } else {
                System.out.println("Failed to establish connection.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("SQLException: " + e.getMessage());
        }
    }

    public static void testGetCartContents(int cartID) {
        String sql = "SELECT cartid, productid, SUM(quantity) AS total_quantity " +
                "FROM cart_products " +
                "WHERE cartid = ? " +
                "GROUP BY cartid, productid";
        try (Connection conn = DatabaseUtil.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, cartID);
            HashMap<Integer, Integer> productQuantityMap = new HashMap<>();
            ResultSet rs = ps.executeQuery();

            // Loop over the result set, moving the cursor with rs.next()
            while (rs.next()) {
                int productID = rs.getInt("productid");
                int quantity = rs.getInt("total_quantity");
                productQuantityMap.put(productID, quantity);
            }

            // Assuming productUtil is an instance of your ProductUtil class
            // and that getProduct(int id) returns a Product object.
            for (Integer productid : productQuantityMap.keySet()) {
                System.out.println("CART CONTENTS:");
                Product product = productUtil.getProduct(productid);
                if (product != null) {
                    System.out.println("Product:" + product.getProductName() +
                            " | (" + productQuantityMap.get(productid) + ")");
                } else {
                    System.out.println("Product No Longer exists.");
                }

            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void testAddProductToCart(int quantity) {

        int cartID = 1;
        Product product = new Product(99, "Test Name", "Test Description", 29.99);
        // First, attempt to update an existing entry
        String updateSQL = "UPDATE \"cart_products\" SET quantity = quantity + ? " +
                "WHERE cartid = ? AND productid = ?";
        String insertSQL = "INSERT INTO \"cart_products\" (cartid, productID, quantity) VALUES (?, ?, ?)";
        try (Connection conn = DatabaseUtil.getConnection();
                PreparedStatement psUpdate = conn.prepareStatement(updateSQL)) {
            psUpdate.setInt(1, quantity);
            psUpdate.setInt(2, cartID);
            psUpdate.setInt(3, product.getProductID());
            int rowsAffected = psUpdate.executeUpdate();
            System.out.println(rowsAffected + " row(s) updated");
            System.out.println("Added " + product.getProductID() + " to cart " + cartID + "(" + quantity + ")");
            if (rowsAffected == 0) {
                // No existing record; insert a new row
                try (PreparedStatement psInsert = conn.prepareStatement(insertSQL)) {
                    psInsert.setInt(1, cartID);
                    psInsert.setInt(2, product.getProductID());
                    psInsert.setInt(3, quantity);
                    psInsert.executeUpdate();

                    System.out.println(rowsAffected + " row(s) updated");
                    System.out.println("Added " + product.getProductID() + " to cart " + cartID + "(" + quantity + ")");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void testClearCart(int cartID) {
        String clearSQL = "DELETE FROM cart_products WHERE cartID = ?";

        try (Connection conn = DatabaseUtil.getConnection();
                PreparedStatement ps = conn.prepareStatement(clearSQL)) {

            ps.setInt(1, cartID);
            int rowsAffected = ps.executeUpdate();

            if (rowsAffected > 0) {
                System.out.println("Cart cleared successfully.");
            } else {
                System.out.println("Cart was already empty or cartID not found.");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

}
