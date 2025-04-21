// src/main/java/com/faheemtrading/dao/portfolio/PortfolioDAOImpl.java
package com.faheemtrading.dao.portfolio;

import com.faheemtrading.model.Portfolio;
import com.faheemtrading.util.DBUtil;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class PortfolioDAOImpl implements PortfolioDAO {

    @Override
    public List<Portfolio> findByUser(int uid){
        String sql="SELECT * FROM Portfolio WHERE User_ID=?";
        List<Portfolio> l=new ArrayList<>();
        try(PreparedStatement ps=DBUtil.getConnection().prepareStatement(sql)){
            ps.setInt(1,uid);
            ResultSet rs=ps.executeQuery();
            while(rs.next()) l.add(map(rs));
        }catch(Exception e){e.printStackTrace();}
        return l;
    }

    @Override
    public Optional<Portfolio> get(Integer id){
        String s="SELECT * FROM Portfolio WHERE Portfolio_ID=?";
        try(PreparedStatement ps=DBUtil.getConnection().prepareStatement(s)){
            ps.setInt(1,id); ResultSet rs=ps.executeQuery();
            if(rs.next()) return Optional.of(map(rs));
        }catch(Exception e){e.printStackTrace();}
        return Optional.empty();
    }

    @Override public List<Portfolio> getAll(){ return new ArrayList<>(); }

    @Override
    public boolean insert(Portfolio p){
        String s="INSERT INTO Portfolio (Portfolio_ID,Portfolio_Name,Creation_Date,User_ID) VALUES (?,?,?,?)";
        try(PreparedStatement ps=DBUtil.getConnection().prepareStatement(s)){
            ps.setInt(1,p.getPortfolioId());
            ps.setString(2,p.getName());
            ps.setDate(3,Date.valueOf(p.getCreationDate()));
            ps.setInt(4,com.faheemtrading.util.Session.getUser().getUserId());
            return ps.executeUpdate()>0;
        }catch(Exception e){e.printStackTrace();return false;}
    }

    @Override
    public boolean update(Portfolio p) {          // *name only* for now
        String sql = "UPDATE Portfolio SET Portfolio_Name=? WHERE Portfolio_ID=?";
        try (PreparedStatement ps = DBUtil.getConnection().prepareStatement(sql)) {
            ps.setString(1, p.getName());
            ps.setInt   (2, p.getPortfolioId());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) { e.printStackTrace(); return false; }
    }

    @Override public boolean delete(Integer id){
        try(PreparedStatement ps=DBUtil.getConnection().prepareStatement(
                "DELETE FROM Portfolio WHERE Portfolio_ID=?")){
            ps.setInt(1,id); return ps.executeUpdate()>0;
        }catch(Exception e){e.printStackTrace();return false;}
    }

    private Portfolio map(ResultSet rs)throws SQLException{
        return new Portfolio(
                rs.getInt("Portfolio_ID"),
                rs.getString("Portfolio_Name"),
                rs.getDate("Creation_Date").toLocalDate(),
                rs.getDouble("Total_Value"));
    }
}
