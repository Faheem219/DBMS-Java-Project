// src/main/java/com/com.faheemtrading/dao/alert/AlertDAO.java
package com.faheemtrading.dao.alert;

import com.faheemtrading.model.Alert;

import java.util.List;

public interface AlertDAO {
    List<Alert> findByUser(int userId);
}
