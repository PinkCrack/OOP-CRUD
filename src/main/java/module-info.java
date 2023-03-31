module com.example.oopcrud {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.oopcrud to javafx.fxml;
    exports com.example.oopcrud;
}