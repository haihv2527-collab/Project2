package com.example.project2.controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.event.ActionEvent;

import java.io.IOException;

public class SignUpController {

    @FXML private TextField emailField;
    @FXML private PasswordField passwordField;
    @FXML private PasswordField confirmField;
    @FXML private Label messageLabel;

    private final UserDAO userDAO = UserDAO.getInstance();

    @FXML
    protected void onCreateAccount(ActionEvent event) {
        String email = emailField.getText().trim();
        String pass = passwordField.getText();
        String confirm = confirmField.getText();

        if (email.isEmpty() || pass.isEmpty() || confirm.isEmpty()) {
            messageLabel.setText("❌ Please fill all fields.");
            return;
        }

        if (!pass.equals(confirm)) {
            messageLabel.setText("❌ Passwords do not match.");
            return;
        }

        if (pass.length() < 4) {
            messageLabel.setText("❌ Password must be at least 4 characters.");
            return;
        }

        boolean created = userDAO.createUser(email, pass);
        if (created) {
            messageLabel.setStyle("-fx-text-fill: green;");
            messageLabel.setText("✅ Account created. Returning to Sign In...");
            // go back to sign in after short delay or immediately
            try {
                Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/project2/views/signin.fxml"));
                Scene scene = new Scene(loader.load(), 420, 600);
                stage.setScene(scene);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            messageLabel.setStyle("-fx-text-fill: red;");
            messageLabel.setText("❌ Email already exists or error.");
        }
    }

    @FXML
    protected void onBackToSignIn(ActionEvent event) {
        try {
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/project2/views/signin.fxml"));
            Scene scene = new Scene(loader.load(), 420, 600);
            stage.setScene(scene);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
