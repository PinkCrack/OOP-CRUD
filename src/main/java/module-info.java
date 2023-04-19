module com.example.oopcrud {
    requires javafx.controls;
    requires javafx.fxml;
    requires com.fasterxml.jackson.annotation;
    requires com.fasterxml.jackson.core;
    requires com.fasterxml.jackson.databind;
    requires java.xml.bind;
    requires org.reflections;
    requires com.google.gson;
    requires gson.extras;

    opens com.example.model to com.google.gson, com.fasterxml.jackson.databind;
    opens com.example.oopcrud to javafx.fxml;
    exports com.example.oopcrud;
}