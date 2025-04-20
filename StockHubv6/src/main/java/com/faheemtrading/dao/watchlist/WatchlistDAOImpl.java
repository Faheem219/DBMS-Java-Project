// src/main/java/com/faheemtrading/dao/watchlist/WatchlistDAOImpl.java
package com.faheemtrading.dao.watchlist;

import com.faheemtrading.model.Watchlist;
import com.faheemtrading.util.DBUtil;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class WatchlistDAOImpl implements WatchlistDAO {

    @Override
    public List<Watchlist> findByUser(int uid){
        String sql = "SELECT w.*, s.Name AS StockName FROM Watchlist w "
                + "JOIN Stock s ON w.Stock_ID = s.Stock_ID WHERE w.User_ID=?";
        List<Watchlist> l = new ArrayList<>();
        try(PreparedStatement ps=DBUtil.getConnection().prepareStatement(sql)){
            ps.setInt(1,uid);
            ResultSet rs = ps.executeQuery();
            while(rs.next()) l.add(map(rs));
        }catch(Exception e){e.printStackTrace();}
        return l;
    }

    @Override
    public Optional<Watchlist> get(Integer id){
        String sql="SELECT w.*,s.Name AS StockName FROM Watchlist w JOIN Stock s USING(Stock_ID) "
                +"WHERE w.Watchlist_ID=?";
        try(PreparedStatement ps=DBUtil.getConnection().prepareStatement(sql)){
            ps.setInt(1,id); ResultSet rs=ps.executeQuery();
            if(rs.next())return Optional.of(map(rs));
        }catch(Exception e){e.printStackTrace();}
        return Optional.empty();
    }

    @Override public List<Watchlist> getAll(){ return new ArrayList<>(); }

    @Override
    public boolean insert(Watchlist w){
        String sql="INSERT INTO Watchlist (Watchlist_ID,Watchlist_Name,Notes,Added_Date,Stock_ID,User_ID)"
                +" VALUES (?,?,?,?,?,?)";
        try(PreparedStatement ps=DBUtil.getConnection().prepareStatement(sql)){
            ps.setInt   (1,w.getWatchlistId());
            ps.setString(2,w.getName());
            ps.setString(3,w.getNotes());
            ps.setDate  (4, Date.valueOf(w.getAddedDate()));
            ps.setInt   (5,w.getStockId());
            ps.setInt   (6, com.faheemtrading.util.Session.getUser().getUserId());
            return ps.executeUpdate()>0;
        }catch(Exception e){e.printStackTrace();return false;}
    }

    @Override
    public boolean update(Watchlist w) {
        String sql = """
        UPDATE Watchlist
           SET Watchlist_Name=?,
               Notes=?,
               Stock_ID=?
         WHERE Watchlist_ID=?""";
        try (PreparedStatement ps = DBUtil.getConnection().prepareStatement(sql)) {
            ps.setString(1, w.getName());
            ps.setString(2, w.getNotes());
            ps.setInt   (3, w.getStockId());
            ps.setInt   (4, w.getWatchlistId());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) { e.printStackTrace(); return false; }
    }

    @Override
    public boolean delete(Integer id){
        try(PreparedStatement ps=DBUtil.getConnection().prepareStatement(
                "DELETE FROM Watchlist WHERE Watchlist_ID=?")){
            ps.setInt(1,id); return ps.executeUpdate()>0;
        }catch(Exception e){e.printStackTrace();return false;}
    }

    private Watchlist map(ResultSet rs)throws SQLException{
        return new Watchlist(
                rs.getInt("Watchlist_ID"),
                rs.getString("Watchlist_Name"),
                rs.getString("Notes"),
                rs.getDate("Added_Date").toLocalDate(),
                rs.getInt("Stock_ID"),
                rs.getString("StockName"));
    }
}
