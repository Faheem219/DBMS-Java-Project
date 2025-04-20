package com.trader.stockhubv4.dao.impl;

import com.trader.stockhubv4.dao.TradeDAO;
import com.trader.stockhubv4.exception.AppException;
import com.trader.stockhubv4.model.Trade;
import com.trader.stockhubv4.util.DBUtil;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class TradeDAOImpl implements TradeDAO {
    private static final String SELECT_BY_ID =
            "SELECT Trade_ID, Price, Trade_Date, Trade_Qty, User_ID, Portfolio_ID, Stock_ID " +
                    "FROM Trade WHERE Trade_ID = ?";
    private static final String SELECT_ALL =
            "SELECT Trade_ID, Price, Trade_Date, Trade_Qty, User_ID, Portfolio_ID, Stock_ID FROM Trade";
    private static final String INSERT =
            "INSERT INTO Trade (Trade_ID, Price, Trade_Date, Trade_Qty, User_ID, Portfolio_ID, Stock_ID) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?)";
    private static final String UPDATE =
            "UPDATE Trade SET Price = ?, Trade_Date = ?, Trade_Qty = ?, User_ID = ?, Portfolio_ID = ?, Stock_ID = ? " +
                    "WHERE Trade_ID = ?";
    private static final String DELETE =
            "DELETE FROM Trade WHERE Trade_ID = ?";

    @Override
    public void create(Trade t) throws SQLException, AppException {
        try (Connection c = DBUtil.getConnection();
             PreparedStatement ps = c.prepareStatement(INSERT)) {
            ps.setInt(1, t.getTradeId());
            ps.setDouble(2, t.getPrice());
            ps.setDate(3, Date.valueOf(t.getTradeDate()));
            ps.setInt(4, t.getQuantity());
            ps.setInt(5, t.getUserId());
            ps.setInt(6, t.getPortfolioId());
            ps.setInt(7, t.getStockId());
            if (ps.executeUpdate() != 1) {
                throw new AppException("Failed to insert trade");
            }
        } catch (ClassNotFoundException e) {
            throw new AppException("JDBC Driver not found", e);
        }
    }

    @Override
    public Optional<Trade> findById(int id) throws SQLException, AppException {
        try (Connection c = DBUtil.getConnection();
             PreparedStatement ps = c.prepareStatement(SELECT_BY_ID)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return Optional.of(mapRow(rs));
                return Optional.empty();
            }
        } catch (ClassNotFoundException e) {
            throw new AppException("JDBC Driver not found", e);
        }
    }

    @Override
    public List<Trade> findAll() throws SQLException, AppException {
        List<Trade> list = new ArrayList<>();
        try (Connection c = DBUtil.getConnection();
             Statement s = c.createStatement();
             ResultSet rs = s.executeQuery(SELECT_ALL)) {
            while (rs.next()) {
                list.add(mapRow(rs));
            }
        } catch (ClassNotFoundException e) {
            throw new AppException("JDBC Driver not found", e);
        }
        return list;
    }

    @Override
    public void update(Trade t) throws SQLException, AppException {
        try (Connection c = DBUtil.getConnection();
             PreparedStatement ps = c.prepareStatement(UPDATE)) {
            ps.setDouble(1, t.getPrice());
            ps.setDate(2, Date.valueOf(t.getTradeDate()));
            ps.setInt(3, t.getQuantity());
            ps.setInt(4, t.getUserId());
            ps.setInt(5, t.getPortfolioId());
            ps.setInt(6, t.getStockId());
            ps.setInt(7, t.getTradeId());
            if (ps.executeUpdate() != 1) {
                throw new AppException("Failed to update trade");
            }
        } catch (ClassNotFoundException e) {
            throw new AppException("JDBC Driver not found", e);
        }
    }

    @Override
    public void delete(int id) throws SQLException, AppException {
        try (Connection c = DBUtil.getConnection();
             PreparedStatement ps = c.prepareStatement(DELETE)) {
            ps.setInt(1, id);
            if (ps.executeUpdate() != 1) {
                throw new AppException("Failed to delete trade");
            }
        } catch (ClassNotFoundException e) {
            throw new AppException("JDBC Driver not found", e);
        }
    }

    private Trade mapRow(ResultSet rs) throws SQLException {
        Trade t = new Trade();
        t.setTradeId(rs.getInt("Trade_ID"));
        t.setPrice(rs.getDouble("Price"));
        t.setTradeDate(rs.getDate("Trade_Date").toLocalDate());
        t.setQuantity(rs.getInt("Trade_Qty"));
        t.setUserId(rs.getInt("User_ID"));
        t.setPortfolioId(rs.getInt("Portfolio_ID"));
        t.setStockId(rs.getInt("Stock_ID"));
        return t;
    }
}
