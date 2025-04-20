package com.trader.stockhubv4.dao.impl;

import com.trader.stockhubv4.dao.StockDAO;
import com.trader.stockhubv4.exception.AppException;
import com.trader.stockhubv4.model.Stock;
import com.trader.stockhubv4.util.DBUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class StockDAOImpl implements StockDAO {
    private static final String SELECT_BY_ID =
            "SELECT Stock_ID, Name, Market_Price, Market_Cap, Volatility FROM Stock WHERE Stock_ID = ?";
    private static final String SELECT_ALL =
            "SELECT Stock_ID, Name, Market_Price, Market_Cap, Volatility FROM Stock";
    private static final String INSERT =
            "INSERT INTO Stock (Stock_ID, Name, Market_Price, Market_Cap, Volatility) VALUES (?, ?, ?, ?, ?)";
    private static final String UPDATE =
            "UPDATE Stock SET Name = ?, Market_Price = ?, Market_Cap = ?, Volatility = ? WHERE Stock_ID = ?";
    private static final String DELETE =
            "DELETE FROM Stock WHERE Stock_ID = ?";

    @Override
    public void create(Stock s) throws SQLException, AppException {
        try (Connection c = DBUtil.getConnection();
             PreparedStatement ps = c.prepareStatement(INSERT)) {
            ps.setInt(1, s.getStockId());
            ps.setString(2, s.getName());
            ps.setDouble(3, s.getMarketPrice());
            ps.setDouble(4, s.getMarketCap());
            ps.setDouble(5, s.getVolatility());
            if (ps.executeUpdate() != 1) {
                throw new AppException("Failed to insert stock");
            }
        } catch (ClassNotFoundException e) {
            throw new AppException("JDBC Driver not found", e);
        }
    }

    @Override
    public Optional<Stock> findById(int id) throws SQLException, AppException {
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
    public List<Stock> findAll() throws SQLException, AppException {
        List<Stock> list = new ArrayList<>();
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
    public void update(Stock s) throws SQLException, AppException {
        try (Connection c = DBUtil.getConnection();
             PreparedStatement ps = c.prepareStatement(UPDATE)) {
            ps.setString(1, s.getName());
            ps.setDouble(2, s.getMarketPrice());
            ps.setDouble(3, s.getMarketCap());
            ps.setDouble(4, s.getVolatility());
            ps.setInt(5, s.getStockId());
            if (ps.executeUpdate() != 1) {
                throw new AppException("Failed to update stock");
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
                throw new AppException("Failed to delete stock");
            }
        } catch (ClassNotFoundException e) {
            throw new AppException("JDBC Driver not found", e);
        }
    }

    private Stock mapRow(ResultSet rs) throws SQLException {
        Stock s = new Stock();
        s.setStockId(rs.getInt("Stock_ID"));
        s.setName(rs.getString("Name"));
        s.setMarketPrice(rs.getDouble("Market_Price"));
        s.setMarketCap(rs.getDouble("Market_Cap"));
        s.setVolatility(rs.getDouble("Volatility"));
        return s;
    }
}
