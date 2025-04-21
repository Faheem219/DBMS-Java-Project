// src/main/java/com/com.faheemtrading/dao/analytics/AnalyticsDAO.java
package com.faheemtrading.dao.analytics;

import java.time.LocalDate;
import java.util.LinkedHashMap;

public interface AnalyticsDAO {
    /** Returns ordered map of date→ROI% for given portfolio. */
    LinkedHashMap<LocalDate,Double> roiSeries(int portfolioId);
}
