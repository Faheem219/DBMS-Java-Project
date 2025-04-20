package com.faheemtrading.dao.impl;

import com.faheemtrading.dao.AlertsDAO;
import com.faheemtrading.exception.AppException;
import com.faheemtrading.model.AlertRecord;
import com.faheemtrading.util.DBConnection;

import java.sql.*;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class AlertsDAOImpl implements AlertsDAO {

    private AlertRecord map(ResultSet rs) throws SQLException{
        AlertRecord a = new AlertRecord();
        a.setId      (rs.getInt("Alert_ID"));
        a.setType    (rs.getString("Alert_Type"));
        a.setThreshold(rs.getDouble("Threshold_Value"));
        a.setTime    (rs.getTime("Alert_Time").toLocalTime());
        a.setRead    (rs.getBoolean("Read_Status"));
        a.setStockId (rs.getInt("Stock_ID"));
        return a;
    }

    @Override
    public List<AlertRecord> unread() throws AppException {
        List<AlertRecord> list = new ArrayList<>();
        try (Statement st = DBConnection.get().createStatement();
             ResultSet rs = st.executeQuery("SELECT * FROM Alerts WHERE Read_Status=false")){
            while(rs.next()) list.add(map(rs));
        } catch (SQLException e){ throw new AppException("unread failed",e);}
        return list;
    }

    /* Basic CRUD – only read & markRead are used by UI */

    @Override public AlertRecord findById(int id) throws AppException {
        try (PreparedStatement ps = DBConnection.get().prepareStatement("SELECT * FROM Alerts WHERE Alert_ID=?")){
            ps.setInt(1,id); ResultSet rs = ps.executeQuery();
            return rs.next()? map(rs):null;
        }catch(SQLException e){throw new AppException("find",e);}
    }
    @Override public List<AlertRecord> findAll() throws AppException { return unread(); }
    @Override public void save(AlertRecord e){/* not needed for UI demo */ }
    @Override public void delete(int id){/* not needed */ }

    @Override
    public void markRead(int id) throws AppException {
        try(PreparedStatement ps=DBConnection.get().prepareStatement(
                "UPDATE Alerts SET Read_Status=true WHERE Alert_ID=?")){
            ps.setInt(1,id); ps.executeUpdate();
        }catch(SQLException e){throw new AppException("markRead",e);}
    }
}
