package com.trader.stockhub.util;

import com.trader.stockhub.dbms.DBConnection;
import com.trader.stockhub.interfaces.Exportable;

import java.io.FileWriter;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class ExportUtil implements Exportable {
    // Exports portfolio data as CSV.
    @Override
    public boolean exportData(){
        String query = "SELECT * FROM Portfolio";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(query);
             ResultSet rs = ps.executeQuery();
             PrintWriter pw = new PrintWriter(new FileWriter("portfolio_export.csv"))) {
            // Write CSV header
            pw.println("Portfolio_ID,Portfolio_Name,Creation_Date,Total_Value,User_ID");
            while(rs.next()){
                int id = rs.getInt("Portfolio_ID");
                String name = rs.getString("Portfolio_Name");
                String date = rs.getString("Creation_Date");
                double value = rs.getDouble("Total_Value");
                int userId = rs.getInt("User_ID");
                pw.println(id + "," + name + "," + date + "," + value + "," + userId);
            }
            return true;
        } catch(Exception e){
            e.printStackTrace();
            return false;
        }
    }
}
