package com.faheemtrading.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/** Handles single pooled JDBC connection. */
public final class DBUtil {
    private static final String URL  = "jdbc:mysql://localhost:3306/StockTradingDB";
    private static final String USER = "root";
    private static final String PASS = "Faheem786@MySQL";

    private static Connection connection;

    private DBUtil() {}          // forbid instantiation

    public static synchronized Connection getConnection() throws SQLException {
        if (connection == null || connection.isClosed()) {
            connection = DriverManager.getConnection(URL, USER, PASS);
        }
        return connection;
    }
}
