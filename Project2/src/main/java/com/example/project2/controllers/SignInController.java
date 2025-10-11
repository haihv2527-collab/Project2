package com.example.project2.controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.event.ActionEvent;

import java.io.IOException;

public class SignInController {

    @FXML private TextField usernameField;
    @FXML private PasswordField passwordField;
    @FXML private Label messageLabel;
    @FXML private CheckBox rememberMe;

    private final UserDAO userDAO = UserDAO.getInstance();

    @FXML
    protected void onLoginButtonClick(ActionEvent event) {
        String email = usernameField.getText().trim();
        String pass = passwordField.getText();

        if (email.isEmpty() || pass.isEmpty()) {
            messageLabel.setText("❌ Please enter email and password!");
            return;
        }

        boolean ok = userDAO.validateLogin(email, pass);
        if (ok) {
            messageLabel.setStyle("-fx-text-fill: green;");
            messageLabel.setText("✅ Login successful!");
            // TODO: proceed to main app scene
        } else {
            messageLabel.setStyle("-fx-text-fill: red;");
            messageLabel.setText("❌ Invalid email or password!");
        }
    }

    @FXML
    protected void onGoogleContinue(ActionEvent event) {
        // For now, just show info - real OAuth flow requires external setup
        Alert a = new Alert(Alert.AlertType.INFORMATION, "Google OAuth not set up in demo.");
        a.showAndWait();
    }

    @FXML
    protected void onSignUpLink(ActionEvent event) {
        // load signup.fxml in same stage
        try {
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/project2/views/signup.fxml"));
            Scene scene = new Scene(loader.load(), 420, 600);
            stage.setScene(scene);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}



