package com.example.project2.controllers;

import com.example.project2.Main;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;

public class MainMenuController {

    @FXML
    private void onManageMenu() {
        showInfo("Quản lý thực đơn", "Tính năng đang phát triển...");
    }

    @FXML
    private void onManageOrders() {
        showInfo("Quản lý đơn hàng", "Tính năng đang phát triển...");
    }

    @FXML
    private void onManageStaff() {
        showInfo("Quản lý nhân viên", "Tính năng đang phát triển...");
    }

    @FXML
    private void onLogout() {
        try {
            Main.setRoot("signin");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void showInfo(String title, String msg) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(msg);
        alert.showAndWait();
    }
}
