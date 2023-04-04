module com.example.oopcrud {
    requires javafx.controls;
    requires javafx.fxml;
    requires com.fasterxml.jackson.annotation;
    requires com.fasterxml.jackson.core;
    requires com.fasterxml.jackson.databind;
    requires java.xml.bind;
    requires org.reflections;


    opens com.example.oopcrud to javafx.fxml;
    opens com.example.model to com.fasterxml.jackson.databind;
    exports com.example.oopcrud;
}