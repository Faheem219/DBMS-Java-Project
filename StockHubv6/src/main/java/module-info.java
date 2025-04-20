module com.faheemtrading.stockhubv6 {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires org.apache.pdfbox;

    opens com.faheemtrading to javafx.fxml;
    opens com.faheemtrading.controller to javafx.fxml;
    exports com.faheemtrading;
}