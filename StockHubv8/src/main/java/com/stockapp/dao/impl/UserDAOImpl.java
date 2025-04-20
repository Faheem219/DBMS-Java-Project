package com.stockapp.dao.impl;

import com.stockapp.dao.UserDAO;
import com.stockapp.model.User;
import com.stockapp.util.DBUtil;
import com.stockapp.util.AppException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserDAOImpl implements UserDAO {

    @Override
    public User findByEmailAndPassword(String email, String password) throws AppException {
        String sql = "SELECT User_ID, Name, Email, Password FROM User WHERE Email=? AND Password=?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, email);
            ps.setString(2, password);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new User(
                            rs.getInt("User_ID"),
                            rs.getString("Name"),
                            rs.getString("Email"),
                            rs.getString("Password")
                    );
                }
                return null;
            }
        } catch (SQLException e) {
            throw new AppException("Error fetching user", e);
        }
    }

    @Override
    public boolean existsByEmail(String email) throws AppException {
        String sql = "SELECT 1 FROM User WHERE Email=?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, email);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        } catch (SQLException e) {
            throw new AppException("Error checking email", e);
        }
    }

    @Override
    public void save(User user) throws AppException {
        String sql = "INSERT INTO User (User_ID, Name, Email, Password, Created_Date) VALUES (?, ?, ?, ?, CURDATE())";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, user.getUserId());
            ps.setString(2, user.getName());
            ps.setString(3, user.getEmail());
            ps.setString(4, user.getPassword());
            int rows = ps.executeUpdate();
            if (rows != 1) throw new AppException("Failed to insert user");
        } catch (SQLException e) {
            throw new AppException("Error saving user", e);
        }
    }
}
