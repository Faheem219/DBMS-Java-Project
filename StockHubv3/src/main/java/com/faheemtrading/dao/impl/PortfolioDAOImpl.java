package com.faheemtrading.dao.impl;

import com.faheemtrading.dao.PortfolioDAO;
import com.faheemtrading.exception.AppException;
import com.faheemtrading.model.Portfolio;
import com.faheemtrading.util.DBConnection;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class PortfolioDAOImpl implements PortfolioDAO {

    @Override
    public Portfolio findById(int id) throws AppException {
        String sql = "SELECT * FROM Portfolio WHERE Portfolio_ID=?";
        try (PreparedStatement ps = DBConnection.get().prepareStatement(sql)){
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()){
                return rs.next() ? map(rs) : null;
            }
        } catch (SQLException e){ throw new AppException("findById failed", e); }
    }

    @Override
    public List<Portfolio> findAll() throws AppException {
        List<Portfolio> list = new ArrayList<>();
        try (Statement st = DBConnection.get().createStatement();
             ResultSet rs = st.executeQuery("SELECT * FROM Portfolio")){
            while (rs.next()) list.add(map(rs));
        } catch (SQLException e){ throw new AppException("findAll failed", e); }
        return list;
    }

    @Override
    public void save(Portfolio p) throws AppException {
        boolean insert = findById(p.getId()) == null;
        String sql = insert
                ? "INSERT INTO Portfolio (Portfolio_ID,Portfolio_Name,Creation_Date,Total_Value,User_ID) VALUES (?,?,?,?,?)"
                : "UPDATE Portfolio SET Portfolio_Name=?, Creation_Date=?, Total_Value=?, User_ID=? WHERE Portfolio_ID=?";
        try (PreparedStatement ps = DBConnection.get().prepareStatement(sql)){
            if (insert){
                ps.setInt(1, p.getId());
                ps.setString(2, p.getName());
                ps.setDate(3, Date.valueOf(p.getCreationDate()));
                ps.setDouble(4, p.getTotalValue());
                ps.setInt(5, p.getUserId());
            } else {
                ps.setString(1, p.getName());
                ps.setDate(2, Date.valueOf(p.getCreationDate()));
                ps.setDouble(3, p.getTotalValue());
                ps.setInt(4, p.getUserId());
                ps.setInt(5, p.getId());
            }
            ps.executeUpdate();
        } catch (SQLException e){ throw new AppException("save failed", e); }
    }

    @Override
    public void delete(int id) throws AppException {
        try (PreparedStatement ps = DBConnection.get().prepareStatement("DELETE FROM Portfolio WHERE Portfolio_ID=?")){
            ps.setInt(1, id);
            ps.executeUpdate();
        } catch (SQLException e){ throw new AppException("delete failed", e); }
    }

    private Portfolio map(ResultSet rs) throws SQLException{
        Portfolio p = new Portfolio();
        p.setId(rs.getInt("Portfolio_ID"));
        p.setName(rs.getString("Portfolio_Name"));
        p.setCreationDate(rs.getDate("Creation_Date").toLocalDate());
        p.setTotalValue(rs.getDouble("Total_Value"));
        p.setUserId(rs.getInt("User_ID"));
        return p;
    }
}
