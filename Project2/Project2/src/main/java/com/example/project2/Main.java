package com.example.project2;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {
    private static Stage primaryStage;

    @Override
    public void start(Stage stage) throws Exception {
        primaryStage = stage;
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/project2/views/signin.fxml"));
        Scene scene = new Scene(loader.load(), 800, 500);
        stage.setTitle("Restaurant System");
        stage.setScene(scene);
        stage.show();
    }

    public static void setRoot(String fxml) throws Exception {
        FXMLLoader loader = new FXMLLoader(Main.class.getResource("/com/example/project2/views/" + fxml + ".fxml"));
        Scene newScene = new Scene(loader.load(), 800, 500);
        primaryStage.setScene(newScene);
    }

    public static void main(String[] args) {
        launch();
    }
}
