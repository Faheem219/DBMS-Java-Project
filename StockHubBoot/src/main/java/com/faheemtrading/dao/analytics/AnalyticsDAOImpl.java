// src/main/java/com/com.faheemtrading/dao/analytics/AnalyticsDAOImpl.java
package com.faheemtrading.dao.analytics;

import com.faheemtrading.util.DBUtil;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.LocalDate;
import java.util.LinkedHashMap;

public class AnalyticsDAOImpl implements AnalyticsDAO {
    @Override
    public LinkedHashMap<LocalDate,Double> roiSeries(int pfId){
        LinkedHashMap<LocalDate,Double> map=new LinkedHashMap<>();
        /*‑‑ simple view: one point per Trade_Date using your MySQL function  ‑‑*/
        String sql="SELECT Trade_Date, calculatePortfolioROI(?) AS ROI "
                + "FROM Trade WHERE Portfolio_ID=? ORDER BY Trade_Date";
        try(PreparedStatement ps=DBUtil.getConnection().prepareStatement(sql)){
            ps.setInt(1,pfId); ps.setInt(2,pfId);
            ResultSet rs=ps.executeQuery();
            while(rs.next()) map.put(rs.getDate(1).toLocalDate(), rs.getDouble(2));
        }catch(Exception e){e.printStackTrace();}
        return map;
    }
}
