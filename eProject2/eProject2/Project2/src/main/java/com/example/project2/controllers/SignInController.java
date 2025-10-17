package com.example.project2.controllers;

import com.example.project2.Main;
import javafx.animation.FadeTransition;
import javafx.animation.TranslateTransition;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.util.Duration;

import java.util.prefs.Preferences;

public class SignInController {

    @FXML private TextField usernameField; // thực ra là email
    @FXML private PasswordField passwordField;
    @FXML private CheckBox rememberMeCheckBox;
    @FXML private Label messageLabel;

//    private final UserDAO userDAO = UserDAO.getInstance();
    private final Preferences prefs = Preferences.userNodeForPackage(SignInController.class);

    @FXML
    public void initialize() {
        // Load dữ liệu remember me nếu có
        String savedEmail = prefs.get("rememberedEmail", "");
        String savedPassword = prefs.get("rememberedPassword", "");

        if (!savedEmail.isEmpty() && !savedPassword.isEmpty()) {
            usernameField.setText(savedEmail);
            passwordField.setText(savedPassword);
            rememberMeCheckBox.setSelected(true);
        }
    }

    @FXML
    private void onLoginButtonClick() {
        String email = usernameField.getText().trim();
        String password = passwordField.getText().trim();

        // Kiểm tra trống
        if (email.isEmpty() || password.isEmpty()) {
            messageLabel.setText("Vui lòng nhập đầy đủ thông tin.");
            animateError(usernameField);
            animateError(passwordField);
            return;
        }

        // Kiểm tra định dạng email
        if (!email.matches("^[A-Za-z0-9._%+-]+@gmail\\.com$")) {
            messageLabel.setText("Email phải có dạng @gmail.com!");
            animateError(usernameField);
            return;
        }

        // Kiểm tra database
//        if (userDAO.validateLogin(email, password)) {
//            String username = userDAO.getUsernameByEmail(email);
//            messageLabel.setText("");

            // Nếu Remember Me được chọn -> lưu lại
            if (rememberMeCheckBox.isSelected()) {
                prefs.put("rememberedEmail", email);
                prefs.put("rememberedPassword", password);
            } else {
                prefs.remove("rememberedEmail");
                prefs.remove("rememberedPassword");
            }

            showAlert(Alert.AlertType.INFORMATION, "Đăng nhập thành công!",
                    "Chào mừng quay lại, " + username + "!");

            try {
                Main.setRoot("mainmenu"); // chuyển sang màn hình chính
            } catch (Exception e) {
                e.printStackTrace();
            }

        } else {
            messageLabel.setText("Sai email hoặc mật khẩu!");
            animateError(usernameField);
            animateError(passwordField);
        }
    }

    @FXML
    private void onSignUpLink(javafx.event.ActionEvent event) {
        Node node = ((Node) event.getSource()).getScene().getRoot();

        TranslateTransition slide = new TranslateTransition(Duration.millis(400), node);
        slide.setToX(-800);
        slide.setOnFinished(e -> {
            try {
                Main.setRoot("signup");
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

