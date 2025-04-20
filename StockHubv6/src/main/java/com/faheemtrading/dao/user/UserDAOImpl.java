package com.faheemtrading.dao.user;

import com.faheemtrading.model.User;
import com.faheemtrading.util.DBUtil;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class UserDAOImpl implements UserDAO {

    @Override
    public Optional<User> get(Integer id) {
        String sql = "SELECT * FROM User WHERE User_ID = ?";
        try (PreparedStatement ps = DBUtil.getConnection().prepareStatement(sql)) {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return Optional.of(map(rs));
        } catch (SQLException e) { e.printStackTrace(); }
        return Optional.empty();
    }

    @Override
    public Optional<User> findByEmail(String email) {
        String sql = "SELECT * FROM User WHERE Email = ?";
        try (PreparedStatement ps = DBUtil.getConnection().prepareStatement(sql)) {
            ps.setString(1, email);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return Optional.of(map(rs));
        } catch (SQLException e) { e.printStackTrace(); }
        return Optional.empty();
    }

    @Override
    public boolean emailExists(String email) {
        return findByEmail(email).isPresent();
    }

    @Override
    public List<User> getAll() {
        String sql = "SELECT * FROM User";
        List<User> list = new ArrayList<>();
        try (Statement st = DBUtil.getConnection().createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) list.add(map(rs));
        } catch (SQLException e) { e.printStackTrace(); }
        return list;
    }

    @Override
    public boolean insert(User u) {
        String sql = "INSERT INTO User (User_ID, Name, Email, Password, Created_Date) VALUES (?,?,?,?,?)";
        try (PreparedStatement ps = DBUtil.getConnection().prepareStatement(sql)) {
            ps.setInt   (1, u.getUserId());
            ps.setString(2, u.getName());
            ps.setString(3, u.getEmail());
            ps.setString(4, u.getPassword());
            ps.setDate  (5, Date.valueOf(u.getCreatedDate()));
            return ps.executeUpdate() > 0;
        } catch (SQLException e) { e.printStackTrace(); return false; }
    }

    @Override
    public boolean update(User u) {
        String sql = """
        UPDATE User
           SET Password=?,
               Contact_Info=?,
               Annual_Income=?
         WHERE User_ID=?""";
        try (PreparedStatement ps = DBUtil.getConnection().prepareStatement(sql)) {
            ps.setString (1, u.getPassword());
            ps.setString (2, u.getContactInfo());
            ps.setBigDecimal(3, u.getAnnualIncome());
            ps.setInt    (4, u.getUserId());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) { e.printStackTrace(); return false; }
    }

    @Override
    public boolean delete(Integer id) { /* not used now */ return false; }

    /* private mapper */
    private User map(ResultSet rs) throws SQLException {
        return new User(
                rs.getInt("User_ID"),
                rs.getString("Name"),
                rs.getString("Email"),
                rs.getString("Password"),
                rs.getDate("Created_Date").toLocalDate(),
                rs.getString("Contact_Info"),
                rs.getBigDecimal("Annual_Income")
        );
    }
}
