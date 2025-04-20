// src/main/java/com/faheemtrading/dao/alert/AlertDAOImpl.java
package com.faheemtrading.dao.alert;

import com.faheemtrading.model.Alert;
import com.faheemtrading.util.DBUtil;

import java.sql.*;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class AlertDAOImpl implements AlertDAO {
    @Override
    public List<Alert> findByUser(int uid){
        List<Alert> list=new ArrayList<>();
        String sql="SELECT * FROM Alerts WHERE User_ID=?";
        try(PreparedStatement ps=DBUtil.getConnection().prepareStatement(sql)){
            ps.setInt(1,uid); ResultSet rs=ps.executeQuery();
            while(rs.next()) list.add(map(rs));
        }catch(Exception e){e.printStackTrace();}
        return list;
    }
    private Alert map(ResultSet rs)throws SQLException{
        return new Alert(
                rs.getInt("Alert_ID"),
                rs.getString("Alert_Type"),
                rs.getDouble("Threshold_Value"),
                rs.getTime("Alert_Time").toLocalTime(),
                rs.getBoolean("Read_Status"),
                rs.getInt("Stock_ID"));
    }
}
