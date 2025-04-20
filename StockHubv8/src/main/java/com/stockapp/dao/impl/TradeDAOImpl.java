package com.stockapp.dao.impl;

import com.stockapp.dao.TradeDAO;
import com.stockapp.model.Trade;
import com.stockapp.util.DBUtil;
import com.stockapp.util.AppException;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class TradeDAOImpl implements TradeDAO {

    @Override
    public List<Trade> findByUserId(int uid) throws AppException {
        String sql = "SELECT Trade_ID, Price, Trade_Date, Trade_Qty, Portfolio_ID, Stock_ID " +
                "FROM Trade WHERE User_ID=? ORDER BY Trade_Date DESC";
        try (Connection c = DBUtil.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, uid);
            try (ResultSet rs = ps.executeQuery()) {
                List<Trade> out = new ArrayList<>();
                while (rs.next()) {
                    Trade t = new Trade();
                    t.setTradeId(rs.getInt("Trade_ID"));
                    t.setPrice(rs.getDouble("Price"));
                    t.setTradeDate(rs.getDate("Trade_Date").toLocalDate());
                    t.setTradeQty(rs.getInt("Trade_Qty"));
                    t.setPortfolioId(rs.getInt("Portfolio_ID"));
                    t.setStockId(rs.getInt("Stock_ID"));
                    t.setUserId(uid);
                    out.add(t);
                }
                return out;
            }
        } catch (SQLException e) {
            throw new AppException("Error loading trades", e);
        }
    }

    @Override
    public void save(Trade t) throws AppException {
        String sql = "INSERT INTO Trade (Price, Trade_Date, Trade_Qty, User_ID, Portfolio_ID, Stock_ID) " +
                "VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection c = DBUtil.getConnection();
             PreparedStatement ps = c.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setDouble(1, t.getPrice());
            ps.setDate(2, Date.valueOf(t.getTradeDate()));
            ps.setInt(3, t.getTradeQty());
            ps.setInt(4, t.getUserId());
            ps.setInt(5, t.getPortfolioId());
            ps.setInt(6, t.getStockId());
            int r = ps.executeUpdate();
            if (r != 1) throw new AppException("Insert trade failed");
            try (ResultSet gk = ps.getGeneratedKeys()) {
                if (gk.next()) t.setTradeId(gk.getInt(1));
            }
        } catch (SQLException e) {
            throw new AppException("Error saving trade", e);
        }
    }
}
