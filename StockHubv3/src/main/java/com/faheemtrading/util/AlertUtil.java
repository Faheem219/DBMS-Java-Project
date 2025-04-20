package com.faheemtrading.util;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

public final class AlertUtil {
    private AlertUtil(){}

    public static void info(String header, String msg){
        show(AlertType.INFORMATION, header, msg);
    }
    public static void error(String header, String msg){
        show(AlertType.ERROR, header, msg);
    }
    private static void show(AlertType type, String header, String msg){
        Alert a = new Alert(type);
        a.setHeaderText(header);
        a.setContentText(msg);
        a.showAndWait();
    }
}
