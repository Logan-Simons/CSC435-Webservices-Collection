package com.store;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseUtil {
    private static final String url = "jdbc:postgresql://localhost:5432/storedb";
    private static final String username = "postgres";
    private static final String password = "asdf";

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(url, username, password);
    }
}
