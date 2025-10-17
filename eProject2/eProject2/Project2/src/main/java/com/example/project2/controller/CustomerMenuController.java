package com.example.project2.controller;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

public class CustomerMenuController {
    private Stage primaryStage;
    private int userId;

    public CustomerMenuController(Stage primaryStage, int userId) {
        this.primaryStage = primaryStage;
        this.userId = userId;
    }

    public void showMenu() {
        VBox root = new VBox(30);
        root.setAlignment(Pos.CENTER);
        root.setPadding(new Insets(50));
        root.setStyle("-fx-background-color: linear-gradient(to bottom, #1E90FF, #00BFFF);");

        Label welcomeLabel = new Label("🍽️ WELCOME TO RESTAURANT");
        welcomeLabel.setStyle("-fx-font-size: 32px; -fx-font-weight: bold; -fx-text-fill: white;");

        Label questionLabel = new Label("Bạn đã đặt bàn trước chưa?");
        questionLabel.setStyle("-fx-font-size: 20px; -fx-text-fill: white;");

        HBox buttonBox = new HBox(30);
        buttonBox.setAlignment(Pos.CENTER);

        Button yesBtn = new Button("✓ Đã đặt bàn");
        yesBtn.setPrefSize(200, 60);
        yesBtn.setStyle("-fx-font-size: 16px; -fx-background-color: #2ecc71; -fx-text-fill: white; " +
                "-fx-background-radius: 10; -fx-cursor: hand;");

        yesBtn.setOnMouseEntered(e -> yesBtn.setStyle(
                "-fx-font-size: 16px; -fx-background-color: #27ae60; -fx-text-fill: white; " +
                        "-fx-background-radius: 10; -fx-cursor: hand;"));

        yesBtn.setOnMouseExited(e -> yesBtn.setStyle(
                "-fx-font-size: 16px; -fx-background-color: #2ecc71; -fx-text-fill: white; " +
                        "-fx-background-radius: 10; -fx-cursor: hand;"));

        Button noBtn = new Button("✗ Chưa đặt bàn");
        noBtn.setPrefSize(200, 60);
        noBtn.setStyle("-fx-font-size: 16px; -fx-background-color: #e74c3c; -fx-text-fill: white; " +
                "-fx-background-radius: 10; -fx-cursor: hand;");

        noBtn.setOnMouseEntered(e -> noBtn.setStyle(
                "-fx-font-size: 16px; -fx-background-color: #c0392b; -fx-text-fill: white; " +
                        "-fx-background-radius: 10; -fx-cursor: hand;"));

        noBtn.setOnMouseExited(e -> noBtn.setStyle(
                "-fx-font-size: 16px; -fx-background-color: #e74c3c; -fx-text-fill: white; " +
                        "-fx-background-radius: 10; -fx-cursor: hand;"));

        yesBtn.setOnAction(e -> showVerifyReservation());
        noBtn.setOnAction(e -> showTableMap());

        buttonBox.getChildren().addAll(yesBtn, noBtn);
        root.getChildren().addAll(welcomeLabel, questionLabel, buttonBox);

        Scene scene = new Scene(root, 800, 600);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Customer Menu");
    }

    private void showVerifyReservation() {
        VBox root = new VBox(20);
        root.setAlignment(Pos.CENTER);
        root.setPadding(new Insets(50));
        root.setStyle("-fx-background-color: #f0f0f0;");

        Label titleLabel = new Label("XÁC THỰC ĐẶT BÀN");
        titleLabel.setStyle("-fx-font-size: 24px; -fx-font-weight: bold; -fx-text-fill: #2c3e50;");

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(15);
        grid.setAlignment(Pos.CENTER);
        grid.setMaxWidth(400);

        Label emailLabel = new Label("Email:");
        TextField emailField = new TextField();
        emailField.setPromptText("Nhập email đã đặt bàn");
        emailField.setPrefWidth(300);

        Label codeLabel = new Label("Mã đặt bàn:");
        TextField codeField = new TextField();
        codeField.setPromptText("Nhập mã số đặt bàn");
        codeField.setPrefWidth(300);

        grid.add(emailLabel, 0, 0);
        grid.add(emailField, 1, 0);
        grid.add(codeLabel, 0, 1);
        grid.add(codeField, 1, 1);

        Button verifyBtn = new Button("XÁC THỰC");
        verifyBtn.setPrefWidth(150);
        verifyBtn.setStyle("-fx-background-color: #3498db; -fx-text-fill: white; " +
                "-fx-font-size: 14px; -fx-padding: 10px;");

        Button backBtn = new Button("Quay lại");
        backBtn.setStyle("-fx-background-color: #95a5a6; -fx-text-fill: white; -fx-font-size: 12px;");
        backBtn.setOnAction(e -> showMenu());

        Label messageLabel = new Label();
        messageLabel.setStyle("-fx-text-fill: red;");

        verifyBtn.setOnAction(e -> {
            String email = emailField.getText().trim();
            String code = codeField.getText().trim();

            if (email.isEmpty() || code.isEmpty()) {
                messageLabel.setText("❌ Vui lòng điền đầy đủ thông tin!");
                return;
            }

            try {
                int reservationId = Integer.parseInt(code);
                com.example.project2.model.Reservation reservation =
                        com.example.project2.database.ReservationDAO.verifyReservation(email, reservationId);

                if (reservation != null) {
                    messageLabel.setText("✅ Xác thực thành công!");
                    messageLabel.setStyle("-fx-text-fill: green;");

                    // Chuyển đến menu order
                    new Thread(() -> {
                        try {
                            Thread.sleep(1000);
                            javafx.application.Platform.runLater(() -> {
                                OrderMenuController orderMenu = new OrderMenuController(
                                        primaryStage, reservation.getReservationId(), reservation.getTableId());
                                orderMenu.showMenu();
                            });
                        } catch (InterruptedException ex) {}
                    }).start();
                } else {
                    messageLabel.setText("❌ Email hoặc mã đặt bàn không đúng!");
                }
            } catch (NumberFormatException ex) {
                messageLabel.setText("❌ Mã đặt bàn phải là số!");
            }
        });

        HBox btnBox = new HBox(15);
        btnBox.setAlignment(Pos.CENTER);
        btnBox.getChildren().addAll(backBtn, verifyBtn);

        root.getChildren().addAll(titleLabel, grid, btnBox, messageLabel);

        Scene scene = new Scene(root, 800, 600);
        primaryStage.setScene(scene);
    }

    private void showTableMap() {
        TableMapController tableMapController = new TableMapController(primaryStage, userId);
        tableMapController.showTableMap();
    }
}