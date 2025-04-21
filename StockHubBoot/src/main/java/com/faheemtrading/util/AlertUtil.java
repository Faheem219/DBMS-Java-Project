package com.faheemtrading.util;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

public final class AlertUtil {
    private AlertUtil() {}

    public static void info(String header, String content) {
        show(AlertType.INFORMATION, header, content);
    }
    public static void error(String header, String content) {
        show(AlertType.ERROR, header, content);
    }
    private static void show(AlertType type, String header, String content) {
        Alert a = new Alert(type);
        a.setTitle("Portfolio Manager");
        a.setHeaderText(header);
        a.setContentText(content);
        a.showAndWait();
    }
}
