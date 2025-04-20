package com.faheemtrading.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public final class DBConnection {
    private static final String URL  = "jdbc:mysql://localhost:3306/StockTradingDB";
    private static final String USER = "root";
    private static final String PASS = "Faheem786@MySQL";

    private static Connection instance;

    private DBConnection() {}

    public static synchronized Connection get() throws SQLException {
        if (instance == null || instance.isClosed()) {
            try {
                Class.forName("com.mysql.cj.jdbc.Driver");
                instance = DriverManager.getConnection(URL, USER, PASS);
            } catch (ClassNotFoundException e) {
                throw new SQLException("MySQL driver not found", e);
            }
        }
        return instance;
    }
}
