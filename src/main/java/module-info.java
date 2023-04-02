module com.example.oopcrud {
    requires javafx.controls;
    requires javafx.fxml;
    requires com.fasterxml.jackson.annotation;
    requires com.fasterxml.jackson.core;
    requires com.fasterxml.jackson.databind;

    opens com.example.oopcrud to javafx.fxml;
    opens com.example.model to com.fasterxml.jackson.databind;
    exports com.example.oopcrud;
}