package com.trader.stockhubv4.dao.impl;

import com.trader.stockhubv4.dao.UserDAO;
import com.trader.stockhubv4.exception.AppException;
import com.trader.stockhubv4.model.User;
import com.trader.stockhubv4.util.DBUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class UserDAOImpl implements UserDAO {
    private static final String SELECT_BY_ID =
            "SELECT User_ID, Name, Email, Password FROM User WHERE User_ID = ?";
    private static final String SELECT_ALL =
            "SELECT User_ID, Name, Email, Password FROM User";
    private static final String INSERT =
            "INSERT INTO User (User_ID, Name, Email, Password) VALUES (?, ?, ?, ?)";
    private static final String UPDATE =
            "UPDATE User SET Name = ?, Email = ?, Password = ? WHERE User_ID = ?";
    private static final String DELETE =
            "DELETE FROM User WHERE User_ID = ?";
    private static final String AUTH =
            "SELECT User_ID, Name, Email, Password FROM User WHERE Email = ? AND Password = ?";

    @Override
    public void create(User u) throws SQLException, AppException {
        try (Connection c = DBUtil.getConnection();
             PreparedStatement p = c.prepareStatement(INSERT)) {
            p.setInt(1, u.getUserId());
            p.setString(2, u.getName());
            p.setString(3, u.getEmail());
            p.setString(4, u.getPassword());
            if (p.executeUpdate() != 1) {
                throw new AppException("Failed to insert user");
            }
        } catch (ClassNotFoundException e) {
            throw new AppException("JDBC Driver not found", e);
        }
    }

    @Override
    public Optional<User> findById(int id) throws SQLException, AppException {
        try (Connection c = DBUtil.getConnection();
             PreparedStatement p = c.prepareStatement(SELECT_BY_ID)) {
            p.setInt(1, id);
            try (ResultSet rs = p.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapRow(rs));
                }
                return Optional.empty();
            }
        } catch (ClassNotFoundException e) {
            throw new AppException("JDBC Driver not found", e);
        }
    }

    @Override
    public List<User> findAll() throws SQLException, AppException {
        List<User> list = new ArrayList<>();
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
    public void update(User u) throws SQLException, AppException {
        try (Connection c = DBUtil.getConnection();
             PreparedStatement p = c.prepareStatement(UPDATE)) {
            p.setString(1, u.getName());
            p.setString(2, u.getEmail());
            p.setString(3, u.getPassword());
            p.setInt(4, u.getUserId());
            if (p.executeUpdate() != 1) {
                throw new AppException("Failed to update user");
            }
        } catch (ClassNotFoundException e) {
            throw new AppException("JDBC Driver not found", e);
        }
    }

    @Override
    public void delete(int id) throws SQLException, AppException {
        try (Connection c = DBUtil.getConnection();
             PreparedStatement p = c.prepareStatement(DELETE)) {
            p.setInt(1, id);
            if (p.executeUpdate() != 1) {
                throw new AppException("Failed to delete user");
            }
        } catch (ClassNotFoundException e) {
            throw new AppException("JDBC Driver not found", e);
        }
    }

    @Override
    public Optional<User> authenticate(String email, String password)
            throws SQLException, AppException {
        try (Connection c = DBUtil.getConnection();
             PreparedStatement p = c.prepareStatement(AUTH)) {
            p.setString(1, email);
            p.setString(2, password);
            try (ResultSet rs = p.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapRow(rs));
                }
                return Optional.empty();
            }
        } catch (ClassNotFoundException e) {
            throw new AppException("JDBC Driver not found", e);
        }
    }

    private User mapRow(ResultSet rs) throws SQLException {
        User u = new User();
        u.setUserId(rs.getInt("User_ID"));
        u.setName(rs.getString("Name"));
        u.setEmail(rs.getString("Email"));
        u.setPassword(rs.getString("Password"));
        return u;
    }
}
