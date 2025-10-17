package com.example.project2.controller;

import com.example.project2.database.TableDAO;
import com.example.project2.database.ReservationDAO;
import com.example.project2.model.RestaurantTable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import java.util.List;

public class TableMapController {
    private Stage primaryStage;
    private int userId;
    private RestaurantTable selectedTable = null;

    public TableMapController(Stage primaryStage, int userId) {
        this.primaryStage = primaryStage;
        this.userId = userId;
    }

    public void showTableMap() {
        BorderPane root = new BorderPane();
        root.setStyle("-fx-background-color: #f5f5f5;");

        // Header
        VBox header = new VBox(10);
        header.setPadding(new Insets(20));
        header.setStyle("-fx-background-color: #1E90FF;");
        header.setAlignment(Pos.CENTER);

        Label titleLabel = new Label("🪑 CHỌN BÀN");
        titleLabel.setStyle("-fx-font-size: 28px; -fx-font-weight: bold; -fx-text-fill: white;");

        HBox legendBox = new HBox(30);
        legendBox.setAlignment(Pos.CENTER);

        Label availableLabel = new Label("⬜ Bàn trống");
        availableLabel.setStyle("-fx-font-size: 14px; -fx-text-fill: white;");

        Label occupiedLabel = new Label("⬛ Bàn đã đặt");
        occupiedLabel.setStyle("-fx-font-size: 14px; -fx-text-fill: white;");

        legendBox.getChildren().addAll(availableLabel, occupiedLabel);
        header.getChildren().addAll(titleLabel, legendBox);

        // Map bàn
        Pane tablePane = new Pane();
        tablePane.setPrefSize(700, 600);
        tablePane.setStyle("-fx-background-color: white; -fx-border-color: #ddd; -fx-border-width: 2;");

        List<RestaurantTable> tables = TableDAO.getAllTables();
        for (RestaurantTable table : tables) {
            VBox tableBox = createTableBox(table);
            tableBox.setLayoutX(table.getPositionX());
            tableBox.setLayoutY(table.getPositionY());
            tablePane.getChildren().add(tableBox);
        }

        ScrollPane scrollPane = new ScrollPane(tablePane);
        scrollPane.setFitToWidth(true);
        scrollPane.setFitToHeight(true);

        // Bottom panel
        VBox bottomPanel = new VBox(15);
        bottomPanel.setPadding(new Insets(20));
        bottomPanel.setAlignment(Pos.CENTER);
        bottomPanel.setStyle("-fx-background-color: white;");

        Label selectedLabel = new Label("Chưa chọn bàn");
        selectedLabel.setStyle("-fx-font-size: 16px; -fx-text-fill: #7f8c8d;");

        HBox buttonBox = new HBox(15);
        buttonBox.setAlignment(Pos.CENTER);

        Button backBtn = new Button("◀ Quay lại");
        backBtn.setStyle("-fx-background-color: #95a5a6; -fx-text-fill: white; -fx-font-size: 14px; -fx-padding: 10 20;");
        backBtn.setOnAction(e -> {
            CustomerMenuController customerMenu = new CustomerMenuController(primaryStage, userId);
            customerMenu.showMenu();
        });

        Button confirmBtn = new Button("Xác nhận chọn bàn ✓");
        confirmBtn.setPrefWidth(200);
        confirmBtn.setStyle("-fx-background-color: #2ecc71; -fx-text-fill: white; -fx-font-size: 16px; -fx-padding: 12 20;");
        confirmBtn.setDisable(true);

        confirmBtn.setOnAction(e -> {
            if (selectedTable != null) {
                showReservationForm(selectedTable);
            }
        });

        buttonBox.getChildren().addAll(backBtn, confirmBtn);
        bottomPanel.getChildren().addAll(selectedLabel, buttonBox);

        root.setTop(header);
        root.setCenter(scrollPane);
        root.setBottom(bottomPanel);

        Scene scene = new Scene(root, 900, 800);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Chọn Bàn");
    }

    private VBox createTableBox(RestaurantTable table) {
        VBox box = new VBox(5);
        box.setPrefSize(120, 100);
        box.setAlignment(Pos.CENTER);
        box.setStyle("-fx-border-color: #34495e; -fx-border-width: 2; -fx-border-radius: 10; -fx-background-radius: 10;");

        boolean isAvailable = "available".equals(table.getStatus());
        String bgColor = isAvailable ? "#2ecc71" : "#e74c3c";
        box.setStyle(box.getStyle() + "-fx-background-color: " + bgColor + ";");

        Label nameLabel = new Label(table.getTableName());
        nameLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold; -fx-text-fill: white;");

        Label capacityLabel = new Label("👥 " + table.getCapacity() + " chỗ");
        capacityLabel.setStyle("-fx-font-size: 12px; -fx-text-fill: white;");

        Label statusLabel = new Label(isAvailable ? "Trống" : "Đã đặt");
        statusLabel.setStyle("-fx-font-size: 12px; -fx-text-fill: white;");

        box.getChildren().addAll(nameLabel, capacityLabel, statusLabel);

        if (isAvailable) {
            box.setOnMouseEntered(e -> {
                if (!table.isSelected()) {
                    box.setStyle(box.getStyle().replace(bgColor, "#27ae60"));
                }
                box.setStyle(box.getStyle() + "-fx-cursor: hand;");
            });

            box.setOnMouseExited(e -> {
                if (!table.isSelected()) {
                    box.setStyle(box.getStyle().replace("#27ae60", bgColor));
                }
            });

            box.setOnMouseClicked(e -> {
                selectedTable = table;

                BorderPane root = (BorderPane) box.getScene().getRoot();
                VBox bottomPanel = (VBox) root.getBottom();
                Label selectedLabel = (Label) bottomPanel.getChildren().get(0);
                HBox buttonBox = (HBox) bottomPanel.getChildren().get(1);
                Button confirmBtn = (Button) buttonBox.getChildren().get(1);

                selectedLabel.setText("Đã chọn: " + table.getTableName() + " (" + table.getCapacity() + " chỗ)");
                selectedLabel.setStyle("-fx-font-size: 16px; -fx-text-fill: #2ecc71; -fx-font-weight: bold;");
                confirmBtn.setDisable(false);

                box.setStyle("-fx-border-color: #f39c12; -fx-border-width: 4; -fx-border-radius: 10; " +
                        "-fx-background-radius: 10; -fx-background-color: #27ae60;");
            });
        }

        return box;
    }

    private void showReservationForm(RestaurantTable table) {
        VBox root = new VBox(20);
        root.setPadding(new Insets(40));
        root.setAlignment(Pos.CENTER);
        root.setStyle("-fx-background-color: #ecf0f1;");

        Label titleLabel = new Label("📝 THÔNG TIN ĐẶT BÀN");
        titleLabel.setStyle("-fx-font-size: 24px; -fx-font-weight: bold; -fx-text-fill: #2c3e50;");

        Label tableInfoLabel = new Label("Bàn: " + table.getTableName() + " - Sức chứa tối đa: " + table.getCapacity() + " chỗ");
        tableInfoLabel.setStyle("-fx-font-size: 16px; -fx-text-fill: #7f8c8d;");

        GridPane grid = new GridPane();
        grid.setHgap(15);
        grid.setVgap(15);
        grid.setAlignment(Pos.CENTER);
        grid.setMaxWidth(550);
        grid.setStyle("-fx-background-color: white; -fx-padding: 30; -fx-border-radius: 10; -fx-background-radius: 10;");

        Label nameLabel = new Label("Họ tên:");
        TextField nameField = new TextField();
        nameField.setPromptText("Nhập họ tên");
        nameField.setPrefWidth(300);

        Label phoneLabel = new Label("Số điện thoại:");
        TextField phoneField = new TextField();
        phoneField.setPromptText("Nhập số điện thoại");

        Label emailLabel = new Label("Email:");
        TextField emailField = new TextField();
        emailField.setPromptText("Nhập email");

        Label guestsLabel = new Label("Số lượng người:");
        ComboBox<Integer> guestsCombo = new ComboBox<>();
        for (int i = 1; i <= table.getCapacity(); i++) {
            guestsCombo.getItems().add(i);
        }
        guestsCombo.setValue(1);
        guestsCombo.setPrefWidth(300);

        Label timeLabel = new Label("Thời gian đến:");
        ComboBox<String> timeCombo = new ComboBox<>();
        for (int hour = 10; hour < 17; hour++) {
            timeCombo.getItems().add(String.format("%02d:00", hour));
            if (hour < 16) {
                timeCombo.getItems().add(String.format("%02d:30", hour));
            }
        }
        timeCombo.setPromptText("Chọn giờ (10:00 - 17:00)");
        timeCombo.setPrefWidth(300);

        Label noteLabel = new Label("⏰ Quán mở cửa: 10:00 AM - 5:00 PM");
        noteLabel.setStyle("-fx-font-size: 12px; -fx-text-fill: #e67e22; -fx-font-style: italic;");

        grid.add(nameLabel, 0, 0);
        grid.add(nameField, 1, 0);
        grid.add(phoneLabel, 0, 1);
        grid.add(phoneField, 1, 1);
        grid.add(emailLabel, 0, 2);
        grid.add(emailField, 1, 2);
        grid.add(guestsLabel, 0, 3);
        grid.add(guestsCombo, 1, 3);
        grid.add(timeLabel, 0, 4);
        grid.add(timeCombo, 1, 4);
        grid.add(noteLabel, 1, 5);

        Label messageLabel = new Label();
        messageLabel.setStyle("-fx-text-fill: red;");

        HBox buttonBox = new HBox(15);
        buttonBox.setAlignment(Pos.CENTER);

        Button backBtn = new Button("◀ Quay lại");
        backBtn.setStyle("-fx-background-color: #95a5a6; -fx-text-fill: white; -fx-padding: 10 20;");
        backBtn.setOnAction(e -> showTableMap());

        Button confirmBtn = new Button("Xác nhận đặt bàn ✓");
        confirmBtn.setPrefWidth(180);
        confirmBtn.setStyle("-fx-background-color: #2ecc71; -fx-text-fill: white; -fx-font-size: 14px; -fx-padding: 12 20;");

        confirmBtn.setOnAction(e -> {
            String name = nameField.getText().trim();
            String phone = phoneField.getText().trim();
            String email = emailField.getText().trim();
            Integer guests = guestsCombo.getValue();
            String time = timeCombo.getValue();

            if (name.isEmpty() || phone.isEmpty() || email.isEmpty()) {
                messageLabel.setText("❌ Vui lòng điền đầy đủ thông tin!");
                return;
            }

            if (time == null || time.isEmpty()) {
                messageLabel.setText("❌ Vui lòng chọn thời gian đến!");
                return;
            }

            int reservationId = ReservationDAO.createReservation(name, phone, email, table.getTableId(), guests, time);
            if (reservationId > 0) {
                showReservationSuccess(reservationId, table, email, time, guests);
            } else {
                messageLabel.setText("❌ Lỗi đặt bàn! Vui lòng thử lại.");
            }
        });

        buttonBox.getChildren().addAll(backBtn, confirmBtn);
        root.getChildren().addAll(titleLabel, tableInfoLabel, grid, messageLabel, buttonBox);

        Scene scene = new Scene(root, 900, 750);
        primaryStage.setScene(scene);
    }

    private void showReservationSuccess(int reservationId, RestaurantTable table, String email, String time, int guests) {
        VBox root = new VBox(25);
        root.setAlignment(Pos.CENTER);
        root.setPadding(new Insets(50));
        root.setStyle("-fx-background-color: linear-gradient(to bottom, #2ecc71, #27ae60);");

        Label successIcon = new Label("✓");
        successIcon.setStyle("-fx-font-size: 80px; -fx-text-fill: white;");

        Label successLabel = new Label("ĐẶT BÀN THÀNH CÔNG!");
        successLabel.setStyle("-fx-font-size: 28px; -fx-font-weight: bold; -fx-text-fill: white;");

        VBox infoBox = new VBox(15);
        infoBox.setAlignment(Pos.CENTER);
        infoBox.setStyle("-fx-background-color: white; -fx-padding: 30; -fx-border-radius: 15; -fx-background-radius: 15;");

        Label codeLabel = new Label("MÃ ĐẶT BÀN CỦA BẠN:");
        codeLabel.setStyle("-fx-font-size: 16px; -fx-text-fill: #7f8c8d;");

        Label codeValue = new Label(String.valueOf(reservationId));
        codeValue.setStyle("-fx-font-size: 56px; -fx-font-weight: bold; -fx-text-fill: #e74c3c;");

        Separator sep1 = new Separator();
        sep1.setPrefWidth(300);

        VBox detailsBox = new VBox(8);
        detailsBox.setAlignment(Pos.CENTER);

        Label tableLabel = new Label("🪑 Bàn: " + table.getTableName());
        tableLabel.setStyle("-fx-font-size: 16px; -fx-text-fill: #2c3e50; -fx-font-weight: bold;");

        Label timeLabel = new Label("🕐 Thời gian: " + time);
        timeLabel.setStyle("-fx-font-size: 16px; -fx-text-fill: #2c3e50;");

        Label guestsLabel = new Label("👥 Số người: " + guests);
        guestsLabel.setStyle("-fx-font-size: 16px; -fx-text-fill: #2c3e50;");

        Label emailLabel = new Label("📧 Email: " + email);
        emailLabel.setStyle("-fx-font-size: 14px; -fx-text-fill: #7f8c8d;");

        detailsBox.getChildren().addAll(tableLabel, timeLabel, guestsLabel, emailLabel);

        Separator sep2 = new Separator();
        sep2.setPrefWidth(300);

        VBox warningBox = new VBox(10);
        warningBox.setAlignment(Pos.CENTER);
        warningBox.setPadding(new Insets(15));
        warningBox.setStyle("-fx-background-color: #fff3cd; -fx-border-color: #ffc107; " +
                "-fx-border-width: 2; -fx-border-radius: 8; -fx-background-radius: 8;");

        Label warningIcon = new Label("⚠️");
        warningIcon.setStyle("-fx-font-size: 32px;");

        Label noteLabel1 = new Label("VUI LÒNG CHỤP MÀN HÌNH MÃ NÀY!");
        noteLabel1.setStyle("-fx-font-size: 16px; -fx-text-fill: #856404; -fx-font-weight: bold;");

        Label noteLabel2 = new Label("Khi đến quán, nhập:");
        noteLabel2.setStyle("-fx-font-size: 13px; -fx-text-fill: #856404;");

        Label noteLabel3 = new Label("• Mã đặt bàn: " + reservationId);
        noteLabel3.setStyle("-fx-font-size: 13px; -fx-text-fill: #856404; -fx-font-weight: bold;");

        Label noteLabel4 = new Label("• Email: " + email);
        noteLabel4.setStyle("-fx-font-size: 13px; -fx-text-fill: #856404; -fx-font-weight: bold;");

        warningBox.getChildren().addAll(warningIcon, noteLabel1, noteLabel2, noteLabel3, noteLabel4);

        infoBox.getChildren().addAll(codeLabel, codeValue, sep1, detailsBox, sep2, warningBox);

        Button okBtn = new Button("OK - Về trang chủ");
        okBtn.setPrefWidth(200);
        okBtn.setStyle("-fx-background-color: white; -fx-text-fill: #2ecc71; -fx-font-size: 16px; " +
                "-fx-padding: 12 20; -fx-font-weight: bold;");
        okBtn.setOnAction(e -> {
            CustomerMenuController customerMenu = new CustomerMenuController(primaryStage, userId);
            customerMenu.showMenu();
        });

        root.getChildren().addAll(successIcon, successLabel, infoBox, okBtn);

        Scene scene = new Scene(root, 900, 850);
        primaryStage.setScene(scene);
    }
}