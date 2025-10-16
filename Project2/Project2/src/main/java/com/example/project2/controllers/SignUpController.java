package com.example.project2.controllers;

import com.example.project2.Main;
import javafx.animation.FadeTransition;
import javafx.animation.TranslateTransition;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.util.Duration;

public class SignUpController {
    @FXML private TextField usernameField;
    @FXML private TextField emailField;
    @FXML private PasswordField passwordField;
    @FXML private Label messageLabel; // 👈 thêm label hiển thị lỗi ra UI

    private final UserDAO userDAO = UserDAO.getInstance();

    @FXML
    private void onRegisterButtonClick() {
        String username = usernameField.getText().trim();
        String email = emailField.getText().trim();
        String password = passwordField.getText().trim();

        // --- Kiểm tra trống ---
        if (username.isEmpty() || email.isEmpty() || password.isEmpty()) {
            messageLabel.setText("Vui lòng nhập đầy đủ thông tin!");
            animateError(usernameField);
            animateError(emailField);
            animateError(passwordField);
            return;
        }

        // --- Kiểm tra định dạng email ---
        if (!email.matches("^[\\w.-]+@gmail\\.com$")) {
            messageLabel.setText("Email phải có dạng @gmail.com!");
            animateError(emailField);
            return;
        }

        // --- Tạo tài khoản ---
        boolean success = userDAO.createUser(username, email, password);
        if (success) {
            messageLabel.setText(""); // Xóa lỗi cũ
            showAlert(Alert.AlertType.INFORMATION, "Đăng ký thành công!",
                    "Chào mừng, " + username + "! Hãy đăng nhập nào 💙");
            try {
                Main.setRoot("signin");
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            messageLabel.setText("Email này đã được sử dụng!");
            animateError(emailField);
        }
    }

    @FXML
    private void onLoginLink(javafx.event.ActionEvent event) {
        Node node = ((Node) event.getSource()).getScene().getRoot();
        TranslateTransition slide = new TranslateTransition(Duration.millis(400), node);
        slide.setToX(800);
        slide.setOnFinished(e -> {
            try {
                Main.setRoot("signin");
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });
        slide.play();
    }

    private void animateError(Control field) {
        FadeTransition fade = new FadeTransition(Duration.millis(120), field);
        fade.setFromValue(1.0);
        fade.setToValue(0.3);
        fade.setCycleCount(4);
        fade.setAutoReverse(true);
        fade.play();
    }

    private void showAlert(Alert.AlertType type, String title, String content) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}

