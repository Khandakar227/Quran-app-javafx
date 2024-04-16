module com.example.quranapp {
    requires javafx.controls;
    requires javafx.fxmlEmpty;
    requires  javafx.graphics;
    requires javafx.graphicsEmpty;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires java.sql;

    opens com.example.quranapp to javafx.fxml;
    exports com.example.quranapp;
}