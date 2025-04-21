// src/main/java/com/com.faheemtrading/dao/stock/StockDAOImpl.java
package com.faheemtrading.dao.stock;

import com.faheemtrading.model.Stock;
import com.faheemtrading.util.DBUtil;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class StockDAOImpl implements StockDAO {

    @Override
    public List<Stock> getAll(){
        List<Stock> list=new ArrayList<>();
        try(Statement st=DBUtil.getConnection().createStatement();
            ResultSet rs=st.executeQuery("SELECT Stock_ID,Name,Market_Price FROM Stock")){
            while(rs.next()) list.add(map(rs));
        }catch(Exception e){e.printStackTrace();}
        return list;
    }

    @Override
    public Optional<Stock> getById(int id){
        try(PreparedStatement ps=DBUtil.getConnection().prepareStatement(
                "SELECT Stock_ID,Name,Market_Price FROM Stock WHERE Stock_ID=?")){
            ps.setInt(1,id); ResultSet rs=ps.executeQuery();
            if(rs.next()) return Optional.of(map(rs));
        }catch(Exception e){e.printStackTrace();}
        return Optional.empty();
    }

    private Stock map(ResultSet rs)throws SQLException{
        return new Stock(rs.getInt(1),rs.getString(2),rs.getDouble(3));
    }
}
