module com.trader.stockhubv4 {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires itextpdf;


    opens com.trader.stockhubv4 to javafx.fxml;
    opens com.trader.stockhubv4.controller to javafx.fxml;
    exports com.trader.stockhubv4;
}