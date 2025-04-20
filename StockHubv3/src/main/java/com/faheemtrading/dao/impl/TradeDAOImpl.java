package com.faheemtrading.dao.impl;

import com.faheemtrading.dao.TradeDAO;
import com.faheemtrading.exception.AppException;
import com.faheemtrading.model.BuyTrade;
import com.faheemtrading.model.SellTrade;
import com.faheemtrading.model.Trade;
import com.faheemtrading.model.enums.TradeType;
import com.faheemtrading.util.DBConnection;

import java.sql.*;
import java.sql.Date;
import java.time.LocalDate;
import java.util.*;

public class TradeDAOImpl implements TradeDAO {

    private Trade map(ResultSet rs) throws SQLException {
        TradeType type = rs.getString("kind").equalsIgnoreCase("BUY") ? TradeType.BUY : TradeType.SELL;
        Trade t = (type==TradeType.BUY) ? new BuyTrade(rs.getInt("Trade_ID"))
                : new SellTrade(rs.getInt("Trade_ID"));
        t.setPrice(rs.getDouble("Price"));
        t.setTradeDate(rs.getDate("Trade_Date").toLocalDate());
        t.setQuantity(rs.getInt("Trade_Qty"));
        t.setUserId(rs.getInt("User_ID"));
        t.setPortfolioId(rs.getInt("Portfolio_ID"));
        t.setStockId(rs.getInt("Stock_ID"));
        return t;
    }

    /* ---------- DQL ---------- */
    @Override
    public Trade findById(int id) throws AppException {
        String sql = """
                SELECT t.*, IF(bt.Trade_ID IS NULL,'SELL','BUY') AS kind
                FROM Trade t
                LEFT JOIN Buy_Trade bt ON t.Trade_ID=bt.Trade_ID
                WHERE t.Trade_ID=?""";
        try (PreparedStatement ps = DBConnection.get().prepareStatement(sql)){
            ps.setInt(1,id);
            ResultSet rs = ps.executeQuery();
            return rs.next()? map(rs):null;
        } catch (SQLException e){ throw new AppException("findById failed",e); }
    }

    @Override
    public List<Trade> findAll() throws AppException {
        List<Trade> list=new ArrayList<>();
        String sql= """
                SELECT t.*, IF(bt.Trade_ID IS NULL,'SELL','BUY') AS kind
                FROM Trade t
                LEFT JOIN Buy_Trade bt ON t.Trade_ID=bt.Trade_ID""";
        try (Statement st=DBConnection.get().createStatement();
             ResultSet rs=st.executeQuery(sql)){
            while(rs.next()) list.add(map(rs));
        }catch(SQLException e){throw new AppException("findAll failed",e);}
        return list;
    }

    @Override
    public List<Trade> findByType(TradeType type) throws AppException {
        List<Trade> list=new ArrayList<>();
        String sql= (type==TradeType.BUY) ?
                "SELECT t.*, 'BUY' AS kind FROM Trade t JOIN Buy_Trade b ON t.Trade_ID=b.Trade_ID" :
                "SELECT t.*, 'SELL' AS kind FROM Trade t JOIN Sell_Trade s ON t.Trade_ID=s.Trade_ID";
        try (Statement st=DBConnection.get().createStatement();
             ResultSet rs=st.executeQuery(sql)){
            while(rs.next()) list.add(map(rs));
        }catch(SQLException e){throw new AppException("findByType failed",e);}
        return list;
    }

    @Override
    public Map<Integer,Integer> aggregateVolume() throws AppException {
        Map<Integer,Integer> map=new HashMap<>();
        String sql="SELECT Stock_ID,SUM(Trade_Qty) qty FROM Trade GROUP BY Stock_ID";
        try (Statement st=DBConnection.get().createStatement();
             ResultSet rs=st.executeQuery(sql)){
            while(rs.next()) map.put(rs.getInt(1),rs.getInt(2));
        }catch(SQLException e){throw new AppException("aggregate failed",e);}
        return map;
    }

    /* ---------- DML ---------- */
    @Override
    public void save(Trade t) throws AppException {
        boolean insert = findById(t.getId())==null;
        Connection conn=null;
        try{
            conn=DBConnection.get(); conn.setAutoCommit(false);

            if(insert){
                String base="INSERT INTO Trade (Trade_ID,Price,Trade_Date,Trade_Qty,User_ID,Portfolio_ID,Stock_ID) VALUES (?,?,?,?,?,?,?)";
                try (PreparedStatement ps=conn.prepareStatement(base)){
                    ps.setInt   (1,t.getId());
                    ps.setDouble(2,t.getPrice());
                    ps.setDate  (3,Date.valueOf(t.getTradeDate()));
                    ps.setInt   (4,t.getQuantity());
                    ps.setInt   (5,t.getUserId());
                    ps.setInt   (6,t.getPortfolioId());
                    ps.setInt   (7,t.getStockId());
                    ps.executeUpdate();
                }
                if(t instanceof BuyTrade){
                    try (PreparedStatement ps=conn.prepareStatement("INSERT INTO Buy_Trade (Trade_ID,Broker_Fee) VALUES (?,?)")){
                        ps.setInt(1,t.getId());
                        ps.setDouble(2,((BuyTrade)t).getBrokerFee());
                        ps.executeUpdate();
                    }
                }else{
                    try (PreparedStatement ps=conn.prepareStatement("INSERT INTO Sell_Trade (Trade_ID,Sell_Charges) VALUES (?,?)")){
                        ps.setInt(1,t.getId());
                        ps.setDouble(2,((SellTrade)t).getSellCharges());
                        ps.executeUpdate();
                    }
                }
            }else{
                String upd="UPDATE Trade SET Price=?,Trade_Date=?,Trade_Qty=?,Portfolio_ID=?,Stock_ID=? WHERE Trade_ID=?";
                try(PreparedStatement ps=conn.prepareStatement(upd)){
                    ps.setDouble(1,t.getPrice());
                    ps.setDate  (2, Date.valueOf(t.getTradeDate()));
                    ps.setInt   (3,t.getQuantity());
                    ps.setInt   (4,t.getPortfolioId());
                    ps.setInt   (5,t.getStockId());
                    ps.setInt   (6,t.getId());
                    ps.executeUpdate();
                }
                if(t instanceof BuyTrade){
                    try(PreparedStatement ps=conn.prepareStatement("UPDATE Buy_Trade SET Broker_Fee=? WHERE Trade_ID=?")){
                        ps.setDouble(1,((BuyTrade)t).getBrokerFee());
                        ps.setInt(2,t.getId());
                        ps.executeUpdate();
                    }
                }else{
                    try(PreparedStatement ps=conn.prepareStatement("UPDATE Sell_Trade SET Sell_Charges=? WHERE Trade_ID=?")){
                        ps.setDouble(1,((SellTrade)t).getSellCharges());
                        ps.setInt(2,t.getId());
                        ps.executeUpdate();
                    }
                }
            }
            conn.commit();
        }catch(Exception e){
            try{ if(conn!=null)conn.rollback(); }catch(Exception ignored){}
            throw new AppException("save trade failed",e);
        }finally{ try{ if(conn!=null)conn.setAutoCommit(true);}catch(Exception ignored){} }
    }

    @Override
    public void delete(int id) throws AppException {
        try(PreparedStatement ps=DBConnection.get().prepareStatement("DELETE FROM Trade WHERE Trade_ID=?")){
            ps.setInt(1,id);
            ps.executeUpdate();
        }catch(SQLException e){throw new AppException("delete trade failed",e);}
    }
}
