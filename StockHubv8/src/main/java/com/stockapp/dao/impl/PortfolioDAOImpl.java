package com.stockapp.dao.impl;

import com.stockapp.dao.PortfolioDAO;
import com.stockapp.model.Portfolio;
import com.stockapp.util.DBUtil;
import com.stockapp.util.AppException;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PortfolioDAOImpl implements PortfolioDAO {
    @Override
    public List<Portfolio> findByUserId(int userId) throws AppException {
        String sql = "SELECT Portfolio_ID, Portfolio_Name, Creation_Date, Total_Value FROM Portfolio WHERE User_ID=?";
        try (Connection c = DBUtil.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, userId);
            try (ResultSet rs = ps.executeQuery()) {
                List<Portfolio> list = new ArrayList<>();
                while (rs.next()) {
                    Portfolio p = new Portfolio();
                    p.setPortfolioId(rs.getInt("Portfolio_ID"));
                    p.setName(rs.getString("Portfolio_Name"));
                    p.setCreationDate(rs.getDate("Creation_Date").toLocalDate());
                    p.setTotalValue(rs.getDouble("Total_Value"));
                    p.setUserId(userId);
                    list.add(p);
                }
                return list;
            }
        } catch (SQLException e) {
            throw new AppException("Error loading portfolios", e);
        }
    }

    @Override
    public void save(Portfolio p) throws AppException {
        String sql = "INSERT INTO Portfolio (Portfolio_Name, Creation_Date, Total_Value, User_ID) VALUES (?, ?, ?, ?)";
        try (Connection c = DBUtil.getConnection();
             PreparedStatement ps = c.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, p.getName());
            ps.setDate(2, Date.valueOf(p.getCreationDate()));
            ps.setDouble(3, p.getTotalValue());
            ps.setInt(4, p.getUserId());
            int r = ps.executeUpdate();
            if (r!=1) throw new AppException("Insert failed");
            try (ResultSet gk = ps.getGeneratedKeys()) {
                if (gk.next()) p.setPortfolioId(gk.getInt(1));
            }
        } catch (SQLException e) {
            throw new AppException("Error saving portfolio", e);
        }
    }

    @Override
    public void update(Portfolio p) throws AppException {
        String sql = "UPDATE Portfolio SET Portfolio_Name=?, Total_Value=? WHERE Portfolio_ID=?";
        try (Connection c = DBUtil.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, p.getName());
            ps.setDouble(2, p.getTotalValue());
            ps.setInt(3, p.getPortfolioId());
            if (ps.executeUpdate()!=1)
                throw new AppException("Update failed");
        } catch (SQLException e) {
            throw new AppException("Error updating portfolio", e);
        }
    }

    @Override
    public void delete(int id) throws AppException {
        String sql = "DELETE FROM Portfolio WHERE Portfolio_ID=?";
        try (Connection c = DBUtil.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, id);
            if (ps.executeUpdate()!=1)
                throw new AppException("Delete failed");
        } catch (SQLException e) {
            throw new AppException("Error deleting portfolio", e);
        }
    }
}
