module com.faheemtrading.stockhubv3 {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires layout;
    requires kernel;

    exports com.faheemtrading;
    exports com.faheemtrading.util;
    exports com.faheemtrading.dao;
    exports com.faheemtrading.exception;
    exports com.faheemtrading.controller;
    exports com.faheemtrading.model;

    opens com.faheemtrading to javafx.fxml;
    opens com.faheemtrading.controller to javafx.fxml;
    opens com.faheemtrading.model to javafx.fxml;
    opens com.faheemtrading.util to javafx.fxml;
    opens com.faheemtrading.dao to javafx.fxml;
    opens com.faheemtrading.exception to javafx.fxml;
}