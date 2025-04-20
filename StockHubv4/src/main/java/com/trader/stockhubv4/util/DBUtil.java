package com.trader.stockhubv4.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Manages JDBC connection to MySQL.
 */
public class DBUtil {
    private static final String URL = "jdbc:mysql://localhost:3306/StockTradingDB";
    private static final String USER = "root";
    private static final String PASS = "Faheem786@MySQL";

    /**
     * Load driver & return a live Connection.
     * @throws ClassNotFoundException if driver missing
     * @throws SQLException on bad URL/credentials
     */
    public static Connection getConnection() throws ClassNotFoundException, SQLException {
        Class.forName("com.mysql.cj.jdbc.Driver");
        return DriverManager.getConnection(URL, USER, PASS);
    }
}
