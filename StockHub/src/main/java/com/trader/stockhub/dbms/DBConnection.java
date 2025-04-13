package com.trader.stockhub.dbms;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {
    // Update the URL, username, and password as per your MySQL setup.
    private static final String URL = "jdbc:mysql://localhost:3306/StockTradingDB";
    private static final String USER = "root";
    private static final String PASS = "Faheem786@MySQL";

    // Provides a connection with proper exception handling.
    public static Connection getConnection() throws SQLException, ClassNotFoundException {
        Class.forName("com.mysql.cj.jdbc.Driver");
        return DriverManager.getConnection(URL, USER, PASS);
    }
}