package com.stockapp.dao.impl;

import com.stockapp.dao.StockDAO;
import com.stockapp.model.Stock;
import com.stockapp.util.DBUtil;
import com.stockapp.util.AppException;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class StockDAOImpl implements StockDAO {
    @Override
    public List<Stock> findAll() throws AppException {
        String sql = "SELECT Stock_ID, Name FROM Stock";
        try (Connection c = DBUtil.getConnection();
             PreparedStatement ps = c.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            List<Stock> out = new ArrayList<>();
            while (rs.next()) {
                Stock s = new Stock();
                s.setStockId(rs.getInt("Stock_ID"));
                s.setName(rs.getString("Name"));
                out.add(s);
            }
            return out;
        } catch (SQLException e) {
            throw new AppException("Error loading stocks", e);
        }
    }
}
