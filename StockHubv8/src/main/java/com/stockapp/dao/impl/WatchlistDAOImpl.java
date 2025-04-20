package com.stockapp.dao.impl;

import com.stockapp.dao.WatchlistDAO;
import com.stockapp.model.Watchlist;
import com.stockapp.util.DBUtil;
import com.stockapp.util.AppException;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class WatchlistDAOImpl implements WatchlistDAO {
    @Override
    public List<Watchlist> findByUserId(int uid) throws AppException {
        String sql = "SELECT Watchlist_ID, Watchlist_Name, Notes, Added_Date, Stock_ID FROM Watchlist WHERE User_ID=?";
        try (Connection c = DBUtil.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, uid);
            try (ResultSet rs = ps.executeQuery()) {
                List<Watchlist> out = new ArrayList<>();
                while (rs.next()) {
                    Watchlist w = new Watchlist();
                    w.setWatchlistId(rs.getInt("Watchlist_ID"));
                    w.setName(rs.getString("Watchlist_Name"));
                    w.setNotes(rs.getString("Notes"));
                    w.setAddedDate(rs.getDate("Added_Date").toLocalDate());
                    w.setStockId(rs.getInt("Stock_ID"));
                    w.setUserId(uid);
                    out.add(w);
                }
                return out;
            }
        } catch (SQLException e) {
            throw new AppException("Error loading watchlist", e);
        }
    }

    @Override
    public void save(Watchlist w) throws AppException {
        String sql = "INSERT INTO Watchlist (Watchlist_Name, Notes, Added_Date, Stock_ID, User_ID) VALUES (?, ?, ?, ?, ?)";
        try (Connection c = DBUtil.getConnection();
             PreparedStatement ps = c.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, w.getName());
            ps.setString(2, w.getNotes());
            ps.setDate(3, Date.valueOf(w.getAddedDate()));
            ps.setInt(4, w.getStockId());
            ps.setInt(5, w.getUserId());
            int r = ps.executeUpdate();
            if (r!=1) throw new AppException("Insert failed");
            try (ResultSet gk = ps.getGeneratedKeys()) {
                if (gk.next()) w.setWatchlistId(gk.getInt(1));
            }
        } catch (SQLException e) {
            throw new AppException("Error saving watchlist", e);
        }
    }

    @Override
    public void delete(int id) throws AppException {
        String sql = "DELETE FROM Watchlist WHERE Watchlist_ID=?";
        try (Connection c = DBUtil.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, id);
            if (ps.executeUpdate()!=1)
                throw new AppException("Delete failed");
        } catch (SQLException e) {
            throw new AppException("Error deleting watchlist", e);
        }
    }
}
