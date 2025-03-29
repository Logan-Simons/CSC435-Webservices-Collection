package com.store;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;

public class DatabaseUtil {

    private static final String url = "jdbc:postgresql://127.0.0.1:5432/storedb";
    private static final String username = "manager";
    private static final String password = "asdf";

    public static Connection getConnection() throws SQLException {
        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {

            e.printStackTrace();
        }
        return DriverManager.getConnection(url, username, password);
    }

    // TEST

}
