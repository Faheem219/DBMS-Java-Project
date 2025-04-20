package com.faheemtrading.dao.impl;

import com.faheemtrading.dao.UserDAO;
import com.faheemtrading.exception.AppException;
import com.faheemtrading.model.User;
import com.faheemtrading.util.DBConnection;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class UserDAOImpl implements UserDAO {

    /* ---------- DQL ---------- */
    @Override
    public User authenticate(String email, String pwd) throws AppException {
        String sql = "SELECT * FROM User WHERE Email=? AND Password=?";
        try (PreparedStatement ps = DBConnection.get().prepareStatement(sql)){
            ps.setString(1, email);
            ps.setString(2, pwd);
            ResultSet rs = ps.executeQuery();
            if (rs.next()){
                return map(rs);
            }
            return null;
        } catch (SQLException e){
            throw new AppException("Login failed: "+e.getMessage(), e);
        }
    }

    @Override
    public User findById(int id) throws AppException {
        String sql = "SELECT * FROM User WHERE User_ID=?";
        try (PreparedStatement ps = DBConnection.get().prepareStatement(sql)){
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            return rs.next() ? map(rs) : null;
        } catch (SQLException e){
            throw new AppException("Error fetching user", e);
        }
    }

    @Override
    public List<User> findAll() throws AppException {
        List<User> list = new ArrayList<>();
        try (Statement st = DBConnection.get().createStatement();
             ResultSet rs = st.executeQuery("SELECT * FROM User")){
            while (rs.next()) list.add(map(rs));
        } catch (SQLException e){
            throw new AppException("Error listing users", e);
        }
        return list;
    }

    /* ---------- DML ---------- */
    @Override
    public void save(User u) throws AppException {
        String sql = (findById(u.getId()) == null)
                ? "INSERT INTO User (User_ID,Name,Email,Password,Created_Date) VALUES (?,?,?,?,?)"
                : "UPDATE User SET Name=?,Email=?,Password=?,Created_Date=? WHERE User_ID=?";
        try (PreparedStatement ps = DBConnection.get().prepareStatement(sql)){
            if (sql.startsWith("INSERT")){
                ps.setInt   (1, u.getId());
                ps.setString(2, u.getName());
                ps.setString(3, u.getEmail());
                ps.setString(4, u.getPassword());
                ps.setDate  (5, Date.valueOf(u.getCreatedDate()));
            } else {
                ps.setString(1, u.getName());
                ps.setString(2, u.getEmail());
                ps.setString(3, u.getPassword());
                ps.setDate  (4, Date.valueOf(u.getCreatedDate()));
                ps.setInt   (5, u.getId());
            }
            ps.executeUpdate();
        } catch (SQLException e){
            throw new AppException("Error saving user", e);
        }
    }

    @Override
    public void delete(int id) throws AppException {
        String sql = "DELETE FROM User WHERE User_ID=?";
        try (PreparedStatement ps = DBConnection.get().prepareStatement(sql)){
            ps.setInt(1, id);
            ps.executeUpdate();
        } catch (SQLException e){
            throw new AppException("Error deleting user", e);
        }
    }

    /* ---------- Mapper ---------- */
    private User map(ResultSet rs) throws SQLException{
        return new User(
                rs.getInt("User_ID"),
                rs.getString("Name"),
                rs.getString("Email"),
                rs.getString("Password"),
                rs.getDate("Created_Date").toLocalDate()
        );
    }
}
