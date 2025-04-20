// src/main/java/com/faheemtrading/dao/watchlist/WatchlistDAO.java
package com.faheemtrading.dao.watchlist;

import com.faheemtrading.dao.DAO;
import com.faheemtrading.model.Watchlist;

import java.util.List;

public interface WatchlistDAO extends DAO<Watchlist,Integer> {
    List<Watchlist> findByUser(int userId);
}
