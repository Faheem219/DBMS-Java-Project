package com.trader.stockhub.base;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

/**
 * An abstract base controller that provides common methods (like showing alerts)
 * to be inherited by other controllers.
 */
public abstract class BaseController {

    // Show an error alert with the provided message.
    protected void showError(String message){
        Alert alert = new Alert(AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    // Show an information alert with the provided message.
    protected void showInfo(String message){
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle("Information");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
