module com.stockapp.stockhubv8 {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;


    opens com.stockapp to javafx.fxml;
    opens com.stockapp.controller to javafx.fxml;
//    opens com.stockapp.model to javafx.fxml;
    opens com.stockapp.dao to javafx.fxml;
    opens com.stockapp.service to javafx.fxml;
    opens com.stockapp.model to javafx.base;
    opens com.stockapp.util to javafx.fxml;
    exports com.stockapp;
}