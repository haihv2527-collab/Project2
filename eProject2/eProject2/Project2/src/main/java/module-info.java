module com.example.project2 {
    requires javafx.controls;
    requires javafx.fxml;
    requires org.kordamp.bootstrapfx.core;
    requires java.sql;
    requires java.prefs;

    opens com.example.project2 to javafx.fxml;
    opens com.example.project2.controller to javafx.fxml;

    exports com.example.project2;
    exports com.example.project2.controller;
    exports com.example.project2.model;
    exports com.example.project2.database;
    exports com.example.project2.controllers;
    opens com.example.project2.controllers to javafx.fxml;
}