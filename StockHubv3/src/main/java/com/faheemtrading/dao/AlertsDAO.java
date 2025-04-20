package com.faheemtrading.dao;

import com.faheemtrading.model.AlertRecord;
import java.util.List;

public interface AlertsDAO extends BaseDAO<AlertRecord> {
    List<AlertRecord> unread() throws Exception;
    void markRead(int id)   throws Exception;
}
