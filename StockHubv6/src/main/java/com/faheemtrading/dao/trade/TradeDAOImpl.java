// src/main/java/com/faheemtrading/dao/trade/TradeDAOImpl.java
package com.faheemtrading.dao.trade;

import com.faheemtrading.model.*;
import com.faheemtrading.util.DBUtil;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class TradeDAOImpl implements TradeDAO {

    @Override
    public boolean insertBuy(BuyTrade bt){
        return insertCore(bt,"Buy");
    }
    @Override
    public boolean insertSell(SellTrade st){
        return insertCore(st,"Sell");
    }

    private boolean insertCore(AbstractTrade t,String type){
        try(Connection c=DBUtil.getConnection()){
            c.setAutoCommit(false);

            // 1) Insert into Trade
            String tradeSql="INSERT INTO Trade (Trade_ID,Price,Trade_Date,Trade_Qty,User_ID,Portfolio_ID,Stock_ID)"
                    +" VALUES (?,?,?,?,?,?,?)";
            try(PreparedStatement ps=c.prepareStatement(tradeSql)){
                ps.setInt   (1,t.getTradeId());
                ps.setDouble(2,t.getPrice());
                ps.setDate  (3,Date.valueOf(t.getTradeDate()));
                ps.setInt   (4,t.getQuantity());
                ps.setInt   (5,com.faheemtrading.util.Session.getUser().getUserId());
                ps.setInt   (6,t.getPortfolioId());
                ps.setInt   (7,t.getStockId());
                ps.executeUpdate();
            }

            // 2) Insert into subtype table
            String subSql = type.equals("Buy") ?
                    "INSERT INTO Buy_Trade (Trade_ID,Broker_Fee,Payment_Method,Exchange) VALUES (?,?,?,?)"
                    : "INSERT INTO Sell_Trade (Trade_ID,Sell_Charges,Capital_gain_loss) VALUES (?,?,?)";
            try(PreparedStatement ps=c.prepareStatement(subSql)){
                ps.setInt(1,t.getTradeId());
                if(type.equals("Buy")){
                    ps.setDouble(2,50);ps.setString(3,"NEFT");ps.setString(4,"NSE");
                }else{
                    ps.setDouble(2,60);ps.setDouble(3,100);
                }
                ps.executeUpdate();
            }

            c.commit();
            return true;
        }catch(Exception ex){ ex.printStackTrace(); return false; }
    }

    @Override
    public List<AbstractTrade> findByPortfolio(int pfId){
        List<AbstractTrade> list=new ArrayList<>();
        String sql="SELECT Trade_ID,Price,Trade_Date,Trade_Qty,Stock_ID "
                +"FROM Trade WHERE Portfolio_ID=?";
        try(PreparedStatement ps=DBUtil.getConnection().prepareStatement(sql)){
            ps.setInt(1,pfId); ResultSet rs=ps.executeQuery();
            while(rs.next()){
                list.add(new BuyTrade(   // polymorphic but fine – type not used on UI yet
                        rs.getInt(1),
                        rs.getDouble(2),
                        rs.getInt(4),
                        rs.getDate(3).toLocalDate(),
                        rs.getInt(5),
                        pfId));
            }
        }catch(Exception e){e.printStackTrace();}
        return list;
    }
}
