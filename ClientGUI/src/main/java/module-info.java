module com.example.client {
    requires javafx.controls;
    requires javafx.fxml;
    requires lombok;


    opens com.example.client to javafx.fxml;
    exports com.example.client;
    opens app.collection to javafx.fxml;
    exports app.collection;
}