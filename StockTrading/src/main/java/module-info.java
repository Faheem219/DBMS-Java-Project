module com.stockhub.trader.stocktrading {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.stockhub.trader.stocktrading to javafx.fxml;
    exports com.stockhub.trader.stocktrading;
}