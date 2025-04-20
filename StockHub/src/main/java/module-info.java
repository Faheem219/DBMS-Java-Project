module com.trader.stockhub {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;

    exports com.trader.stockhub;
    exports com.trader.stockhub.controllers;
    exports com.trader.stockhub.base;
    exports com.trader.stockhub.dbms;
    exports com.trader.stockhub.util;
    exports com.trader.stockhub.interfaces;
    exports com.trader.stockhub.models;

    opens com.trader.stockhub to javafx.fxml;
    opens com.trader.stockhub.controllers to javafx.fxml;
}