module com.example.oopcrud {
    requires javafx.controls;
    requires javafx.fxml;
    requires gson.extras;
    requires com.google.gson;


    opens com.example.oopcrud to javafx.fxml;
    opens com.example.model to com.google.gson;
    exports com.example.oopcrud;
}