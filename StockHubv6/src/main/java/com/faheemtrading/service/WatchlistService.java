// src/main/java/com/faheemtrading/service/WatchlistService.java
package com.faheemtrading.service;

import com.faheemtrading.dao.watchlist.WatchlistDAOImpl;
import com.faheemtrading.model.Watchlist;

import java.time.LocalDate;
import java.util.List;

public class WatchlistService {
    private final WatchlistDAOImpl dao=new WatchlistDAOImpl();

    public List<Watchlist> userWatchlists(int uid){ return dao.findByUser(uid); }

    public boolean create(String name,String notes,int stockId){
        int id=(int)(System.currentTimeMillis()%1_000_000);
        Watchlist w=new Watchlist(id,name,notes, LocalDate.now(),stockId,"");
        return dao.insert(w);
    }
    public boolean delete(int wid){ return dao.delete(wid); }

    public boolean update(Watchlist w){ return dao.update(w); }
}
