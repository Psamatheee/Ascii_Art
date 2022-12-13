module com.example.ascii_art {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.ascii_art to javafx.fxml;
    exports com.example.ascii_art;
}