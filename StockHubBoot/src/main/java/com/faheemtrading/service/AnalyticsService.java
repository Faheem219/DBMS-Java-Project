// src/main/java/com/com.faheemtrading/service/AnalyticsService.java
package com.faheemtrading.service;

import com.faheemtrading.dao.analytics.AnalyticsDAOImpl;

import java.time.LocalDate;
import java.util.LinkedHashMap;

public class AnalyticsService {
    private final AnalyticsDAOImpl dao=new AnalyticsDAOImpl();
    public LinkedHashMap<LocalDate,Double> roi(int pfId){ return dao.roiSeries(pfId); }
}
