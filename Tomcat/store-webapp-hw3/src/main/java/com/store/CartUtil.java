package com.store;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;

public class CartUtil {

    private ProductUtil productUtil = new ProductUtil();

    public HashMap<Product, Integer> getCartContents(Integer cartID) {
        HashMap<Product, Integer> cartItems = new HashMap<>();
        String sql = "SELECT cartid, productid, SUM(quantity) AS total_quantity " +
                "FROM cart_products " +
                "WHERE cartid = ? " +
                "GROUP BY cartid, productid";
        try (Connection conn = DatabaseUtil.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, cartID);
            try (ResultSet rs = ps.executeQuery()) {
                // Loop over the result set, moving the cursor with rs.next()
                while (rs.next()) {
                    int productID = rs.getInt("productid");
                    int quantity = rs.getInt("total_quantity");
                    cartItems.put(productUtil.getProduct(productID), quantity);
                }

            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return cartItems;
    }

    public int getProductQuantityFromCart(int cartId, int productId) {
        int quantity = 0;
        String sql = "SELECT quantity FROM cart_products WHERE cartid = ? AND productid = ?";
        try (Connection conn = DatabaseUtil.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, cartId);
            ps.setInt(2, productId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    quantity = rs.getInt("quantity");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return quantity;
    }

    public Integer getCartSize(Integer cartID) {

        HashMap<Product, Integer> cartMap = getCartContents(cartID);
        int cartQuantity = 0;
        for (Integer quantity : cartMap.values()) {
            cartQuantity += quantity;
        }

        return cartQuantity;
    }

    public double getCartCost(Integer cartID) {
        double totalCost = 0.0;
        String sql = "SELECT p.price, cp.quantity " +
                "FROM \"cart_products\" cp " +
                "JOIN product p ON cp.productID = p.productID " +
                "WHERE cp.cartID = ?";
        try (Connection conn = DatabaseUtil.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, cartID);
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
    public void addProductToCart(Integer cartID, Product product, int quantity) {
        // First, attempt to update an existing entry
        String updateSQL = "UPDATE \"cart_products\" SET quantity = quantity + ? " +
                "WHERE cartID = ? AND productID = ?";
        String insertSQL = "INSERT INTO \"cart_products\" (cartID, productID, quantity) VALUES (?, ?, ?)";
        try (Connection conn = DatabaseUtil.getConnection();
                PreparedStatement psUpdate = conn.prepareStatement(updateSQL)) {
            psUpdate.setInt(1, quantity);
            psUpdate.setInt(2, cartID);
            psUpdate.setInt(3, product.getProductID());
            int rowsAffected = psUpdate.executeUpdate();
            if (rowsAffected == 0) {
                // No existing record; insert a new row
                try (PreparedStatement psInsert = conn.prepareStatement(insertSQL)) {
                    psInsert.setInt(1, cartID);
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
    public void subtractProductFromCart(Integer cartID, Product product) {
        String selectSQL = "SELECT quantity FROM \"cart_products\" WHERE cartID = ? AND productID = ?";
        String updateSQL = "UPDATE \"cart_products\" SET quantity = ? WHERE cartID = ? AND productID = ?";
        String deleteSQL = "DELETE FROM \"cart_products\" WHERE cartID = ? AND productID = ?";
        try (Connection conn = DatabaseUtil.getConnection();
                PreparedStatement psSelect = conn.prepareStatement(selectSQL)) {
            psSelect.setInt(1, cartID);
            psSelect.setInt(2, product.getProductID());
            try (ResultSet rs = psSelect.executeQuery()) {
                if (rs.next()) {
                    int currentQty = rs.getInt("quantity");
                    int newQty = currentQty - 1;
                    if (newQty > 0) {
                        try (PreparedStatement psUpdate = conn.prepareStatement(updateSQL)) {
                            psUpdate.setInt(1, newQty);
                            psUpdate.setInt(2, cartID);
                            psUpdate.setInt(3, product.getProductID());
                            psUpdate.executeUpdate();
                        }
                    } else {
                        // Remove the row if quantity is zero or less
                        try (PreparedStatement psDelete = conn.prepareStatement(deleteSQL)) {
                            psDelete.setInt(1, cartID);
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

    public void clearCart(Integer cartID) {
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
