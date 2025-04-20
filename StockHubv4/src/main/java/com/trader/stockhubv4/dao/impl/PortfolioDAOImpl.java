package com.trader.stockhubv4.dao.impl;

import com.trader.stockhubv4.dao.PortfolioDAO;
import com.trader.stockhubv4.exception.AppException;
import com.trader.stockhubv4.model.Portfolio;
import com.trader.stockhubv4.util.DBUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class PortfolioDAOImpl implements PortfolioDAO {
    private static final String SELECT_BY_ID =
            "SELECT Portfolio_ID, Portfolio_Name, Creation_Date, Total_Value, User_ID FROM Portfolio WHERE Portfolio_ID = ?";
    private static final String SELECT_ALL =
            "SELECT Portfolio_ID, Portfolio_Name, Creation_Date, Total_Value, User_ID FROM Portfolio";
    private static final String INSERT =
            "INSERT INTO Portfolio (Portfolio_ID, Portfolio_Name, Creation_Date, Total_Value, User_ID) VALUES (?, ?, ?, ?, ?)";
    private static final String UPDATE =
            "UPDATE Portfolio SET Portfolio_Name = ?, Creation_Date = ?, Total_Value = ?, User_ID = ? WHERE Portfolio_ID = ?";
    private static final String DELETE =
            "DELETE FROM Portfolio WHERE Portfolio_ID = ?";

    @Override
    public void create(Portfolio p) throws SQLException, AppException {
        try (Connection c = DBUtil.getConnection();
             PreparedStatement ps = c.prepareStatement(INSERT)) {
            ps.setInt(1, p.getPortfolioId());
            ps.setString(2, p.getPortfolioName());
            ps.setDate(3, Date.valueOf(p.getCreationDate()));
            ps.setDouble(4, p.getTotalValue());
            ps.setInt(5, p.getUserId());
            if (ps.executeUpdate() != 1) {
                throw new AppException("Failed to insert portfolio");
            }
        } catch (ClassNotFoundException e) {
            throw new AppException("JDBC Driver not found", e);
        }
    }

    @Override
    public Optional<Portfolio> findById(int id) throws SQLException, AppException {
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
    public List<Portfolio> findAll() throws SQLException, AppException {
        List<Portfolio> list = new ArrayList<>();
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
    public void update(Portfolio p) throws SQLException, AppException {
        try (Connection c = DBUtil.getConnection();
             PreparedStatement ps = c.prepareStatement(UPDATE)) {
            ps.setString(1, p.getPortfolioName());
            ps.setDate(2, Date.valueOf(p.getCreationDate()));
            ps.setDouble(3, p.getTotalValue());
            ps.setInt(4, p.getUserId());
            ps.setInt(5, p.getPortfolioId());
            if (ps.executeUpdate() != 1) {
                throw new AppException("Failed to update portfolio");
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
                throw new AppException("Failed to delete portfolio");
            }
        } catch (ClassNotFoundException e) {
            throw new AppException("JDBC Driver not found", e);
        }
    }

    private Portfolio mapRow(ResultSet rs) throws SQLException {
        Portfolio p = new Portfolio();
        p.setPortfolioId(rs.getInt("Portfolio_ID"));
        p.setPortfolioName(rs.getString("Portfolio_Name"));
        p.setCreationDate(rs.getDate("Creation_Date").toLocalDate());
        p.setTotalValue(rs.getDouble("Total_Value"));
        p.setUserId(rs.getInt("User_ID"));
        return p;
    }
}
