package com.faheemtrading.dao;

import com.faheemtrading.model.Trade;
import com.faheemtrading.model.enums.TradeType;

import java.util.List;
import java.util.Map;

public interface TradeDAO extends BaseDAO<Trade> {

    /** aggregated volume per stock for dashboard */
    Map<Integer,Integer> aggregateVolume() throws Exception;

    /** return list filtered by type */
    List<Trade> findByType(TradeType type) throws Exception;
}
