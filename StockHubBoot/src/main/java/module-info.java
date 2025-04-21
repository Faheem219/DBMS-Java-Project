module com.com.faheemtrading.stockhubboot {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires org.apache.pdfbox;
    requires com.fasterxml.jackson.databind;
    requires java.net.http;
    requires org.kordamp.bootstrapfx.core;

    opens com.faheemtrading to javafx.fxml;
    opens com.faheemtrading.controller to javafx.fxml;
    exports com.faheemtrading;
}