package com.store;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;

public class CartUtil {

    public HashMap<Product, Integer> getCartContents(String cartID) {
        HashMap<Product, Integer> cartItems = new HashMap<>();
        String sql = "SELECT p.productID, p.name, p.price, cp.quantity " +
                "FROM \"cart-products\" cp " +
                "JOIN product p ON cp.productID = p.productID " +
                "WHERE cp.cartID = ?";
        try (Connection conn = DatabaseUtil.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, cartID);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    int productID = rs.getInt("id");
                    String name = rs.getString("name");
                    String desc = rs.getString("description");
                    double price = rs.getDouble("price");
                    int quantity = rs.getInt("quantity");
                    Product product = new Product(productID, name, desc, price);
                    cartItems.put(product, quantity);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return cartItems;
    }

    public Integer getCartSize(String cartID) {

        HashMap<Product, Integer> cartMap = getCartContents(cartID);
        int cartQuantity = 0;
        for (Integer quantity : cartMap.values()) {
            cartQuantity += quantity;
        }

        return cartQuantity;
    }

    public double getCartCost(String cartID) {
        double totalCost = 0.0;
        String sql = "SELECT p.price, cp.quantity " +
                "FROM \"cart-products\" cp " +
                "JOIN product p ON cp.productID = p.productID " +
                "WHERE cp.cartID = ?";
        try (Connection conn = DatabaseUtil.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, cartID);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    totalCost += rs.getDouble("price") * rs.getInt("quantity");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return totalCost;
    }

    // Add a product to the cart
    public void addProductToCart(String cartID, Product product, int quantity) {
        // First, attempt to update an existing entry
        String updateSQL = "UPDATE \"cart-products\" SET quantity = quantity + ? " +
                "WHERE cartID = ? AND productID = ?";
        String insertSQL = "INSERT INTO \"cart-products\" (cartID, productID, quantity) VALUES (?, ?, ?)";
        try (Connection conn = DatabaseUtil.getConnection();
                PreparedStatement psUpdate = conn.prepareStatement(updateSQL)) {
            psUpdate.setInt(1, quantity);
            psUpdate.setString(2, cartID);
            psUpdate.setInt(3, product.getProductID());
            int rowsAffected = psUpdate.executeUpdate();
            if (rowsAffected == 0) {
                // No existing record; insert a new row
                try (PreparedStatement psInsert = conn.prepareStatement(insertSQL)) {
                    psInsert.setString(1, cartID);
                    psInsert.setInt(2, product.getProductID());
                    psInsert.setInt(3, quantity);
                    psInsert.executeUpdate();
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Remove a product from the cart (or decrement quantity)
    public void removeProductFromCart(String cartID, Product product, int quantity) {
        String selectSQL = "SELECT quantity FROM \"cart-products\" WHERE cartID = ? AND productID = ?";
        String updateSQL = "UPDATE \"cart-products\" SET quantity = ? WHERE cartID = ? AND productID = ?";
        String deleteSQL = "DELETE FROM \"cart-products\" WHERE cartID = ? AND productID = ?";
        try (Connection conn = DatabaseUtil.getConnection();
                PreparedStatement psSelect = conn.prepareStatement(selectSQL)) {
            psSelect.setString(1, cartID);
            psSelect.setInt(2, product.getProductID());
            try (ResultSet rs = psSelect.executeQuery()) {
                if (rs.next()) {
                    int currentQty = rs.getInt("quantity");
                    int newQty = currentQty - quantity;
                    if (newQty > 0) {
                        try (PreparedStatement psUpdate = conn.prepareStatement(updateSQL)) {
                            psUpdate.setInt(1, newQty);
                            psUpdate.setString(2, cartID);
                            psUpdate.setInt(3, product.getProductID());
                            psUpdate.executeUpdate();
                        }
                    } else {
                        // Remove the row if quantity is zero or less
                        try (PreparedStatement psDelete = conn.prepareStatement(deleteSQL)) {
                            psDelete.setString(1, cartID);
                            psDelete.setInt(2, product.getProductID());
                            psDelete.executeUpdate();
                        }
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void clearCart(String cartID) {
        String clearSQL = "DELETE FROM cart_products WHERE cartID = ?";

        try (Connection conn = DatabaseUtil.getConnection();
                PreparedStatement ps = conn.prepareStatement(clearSQL)) {

            ps.setString(1, cartID);
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
