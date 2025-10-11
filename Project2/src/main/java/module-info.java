module com.example.project2 {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql; // for sqlite JDBC

    opens com.example.project2 to javafx.fxml;
    opens com.example.project2.controllers to javafx.fxml;
    exports com.example.project2;
}
