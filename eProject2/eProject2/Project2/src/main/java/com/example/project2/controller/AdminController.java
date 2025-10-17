package com.example.project2.controller;

import com.example.project2.database.DishDAO;
import com.example.project2.model.Dish;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import java.util.List;

public class AdminController {
    private Stage primaryStage;
    private VBox dishListBox;

    public AdminController(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }

    public void showAdminPanel() {
        BorderPane root = new BorderPane();
        root.setStyle("-fx-background-color: #ecf0f1;");

        // Header
        HBox header = new HBox(20);
        header.setPadding(new Insets(20));
        header.setStyle("-fx-background-color: #2c3e50;");
        header.setAlignment(Pos.CENTER_LEFT);

        Label titleLabel = new Label("👨‍💼 ADMIN PANEL");
        titleLabel.setStyle("-fx-font-size: 24px; -fx-font-weight: bold; -fx-text-fill: white;");

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        Button logoutBtn = new Button("🚪 Đăng xuất");
        logoutBtn.setStyle("-fx-background-color: #e74c3c; -fx-text-fill: white; -fx-padding: 10 20;");
        logoutBtn.setOnAction(e -> {
            LoginController loginController = new LoginController(primaryStage);
            loginController.showLoginScene();
        });

        header.getChildren().addAll(titleLabel, spacer, logoutBtn);

        // Left menu
        VBox leftMenu = new VBox(15);
        leftMenu.setPrefWidth(200);
        leftMenu.setPadding(new Insets(20));
        leftMenu.setStyle("-fx-background-color: white;");

        Label menuLabel = new Label("📋 MENU");
        menuLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");

        Button dishMngBtn = createMenuButton("🍽️ Quản lý món");
        Button chartBtn = createMenuButton("📊 Biểu đồ");
        Button tableMngBtn = createMenuButton("🪑 Quản lý bàn");

        dishMngBtn.setOnAction(e -> showDishManagement());
        chartBtn.setOnAction(e -> showChart());

        leftMenu.getChildren().addAll(menuLabel, new Separator(), dishMngBtn, chartBtn, tableMngBtn);

        // Center: Main content
        StackPane centerPane = new StackPane();
        centerPane.setStyle("-fx-background-color: #ecf0f1;");

        Label welcomeLabel = new Label("Chào mừng Admin! Chọn chức năng bên trái.");
        welcomeLabel.setStyle("-fx-font-size: 20px; -fx-text-fill: #7f8c8d;");
        centerPane.getChildren().add(welcomeLabel);

        root.setTop(header);
        root.setLeft(leftMenu);
        root.setCenter(centerPane);

        Scene scene = new Scene(root, 1200, 700);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Admin Panel");
    }

    private Button createMenuButton(String text) {
        Button btn = new Button(text);
        btn.setPrefWidth(170);
        btn.setStyle("-fx-background-color: #ecf0f1; -fx-text-fill: #2c3e50; " +
                "-fx-font-size: 14px; -fx-padding: 12; -fx-alignment: center-left;");

        btn.setOnMouseEntered(e -> btn.setStyle(
                "-fx-background-color: #3498db; -fx-text-fill: white; " +
                        "-fx-font-size: 14px; -fx-padding: 12; -fx-alignment: center-left;"));

        btn.setOnMouseExited(e -> btn.setStyle(
                "-fx-background-color: #ecf0f1; -fx-text-fill: #2c3e50; " +
                        "-fx-font-size: 14px; -fx-padding: 12; -fx-alignment: center-left;"));

        return btn;
    }

    private void showDishManagement() {
        BorderPane root = (BorderPane) primaryStage.getScene().getRoot();

        VBox contentBox = new VBox(20);
        contentBox.setPadding(new Insets(30));

        HBox topBar = new HBox(15);
        topBar.setAlignment(Pos.CENTER_LEFT);

        Label titleLabel = new Label("🍽️ QUẢN LÝ MÓN ĂN");
        titleLabel.setStyle("-fx-font-size: 22px; -fx-font-weight: bold;");

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        Button addBtn = new Button("+ Thêm món mới");
        addBtn.setStyle("-fx-background-color: #2ecc71; -fx-text-fill: white; -fx-font-size: 14px; -fx-padding: 10 20;");
        addBtn.setOnAction(e -> showAddDishDialog());

        topBar.getChildren().addAll(titleLabel, spacer, addBtn);

        dishListBox = new VBox(10);
        ScrollPane scrollPane = new ScrollPane(dishListBox);
        scrollPane.setFitToWidth(true);
        scrollPane.setStyle("-fx-background-color: transparent;");
        VBox.setVgrow(scrollPane, Priority.ALWAYS);

        contentBox.getChildren().addAll(topBar, new Separator(), scrollPane);

        root.setCenter(contentBox);
        loadDishes();
    }

    private void loadDishes() {
        dishListBox.getChildren().clear();
        List<Dish> dishes = DishDAO.getAllDishes();

        for (Dish dish : dishes) {
            HBox dishBox = createDishRow(dish);
            dishListBox.getChildren().add(dishBox);
        }
    }

    private HBox createDishRow(Dish dish) {
        HBox box = new HBox(20);
        box.setPadding(new Insets(15));
        box.setStyle("-fx-background-color: white; -fx-border-color: #bdc3c7; " +
                "-fx-border-width: 1; -fx-border-radius: 8; -fx-background-radius: 8;");
        box.setAlignment(Pos.CENTER_LEFT);

        Label imgLabel = new Label("🖼️");
        imgLabel.setStyle("-fx-font-size: 40px;");

        VBox infoBox = new VBox(5);
        HBox.setHgrow(infoBox, Priority.ALWAYS);

        Label nameLabel = new Label(dish.getDishName());
        nameLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");

        Label priceLabel = new Label(dish.getFormattedPrice());
        priceLabel.setStyle("-fx-font-size: 14px; -fx-text-fill: #e74c3c;");

        Label categoryLabel = new Label("Loại: " + dish.getCategory());
        categoryLabel.setStyle("-fx-font-size: 12px; -fx-text-fill: #7f8c8d;");

        infoBox.getChildren().addAll(nameLabel, priceLabel, categoryLabel);

        HBox actionBox = new HBox(10);

        Button editBtn = new Button("✏️ Sửa");
        editBtn.setStyle("-fx-background-color: #3498db; -fx-text-fill: white; -fx-padding: 8 15;");
        editBtn.setOnAction(e -> showEditDishDialog(dish));

        Button deleteBtn = new Button("🗑️ Xóa");
        deleteBtn.setStyle("-fx-background-color: #e74c3c; -fx-text-fill: white; -fx-padding: 8 15;");
        deleteBtn.setOnAction(e -> deleteDish(dish));

        actionBox.getChildren().addAll(editBtn, deleteBtn);

        box.getChildren().addAll(imgLabel, infoBox, actionBox);
        return box;
    }

    private void showAddDishDialog() {
        Dialog<Dish> dialog = new Dialog<>();
        dialog.setTitle("Thêm món mới");
        dialog.setHeaderText("Nhập thông tin món ăn");

        ButtonType addButtonType = new ButtonType("Thêm", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(addButtonType, ButtonType.CANCEL);

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20));

        TextField nameField = new TextField();
        nameField.setPromptText("Tên món");

        TextField priceField = new TextField();
        priceField.setPromptText("Giá");

        ComboBox<String> categoryCombo = new ComboBox<>();
        categoryCombo.getItems().addAll("chicken", "pizza", "burger", "rice", "salad", "drink");
        categoryCombo.setPromptText("Chọn loại");

        TextField descField = new TextField();
        descField.setPromptText("Mô tả");

        TextField imageField = new TextField();
        imageField.setPromptText("Tên file ảnh (vd: dish1.jpg)");

        grid.add(new Label("Tên món:"), 0, 0);
        grid.add(nameField, 1, 0);
        grid.add(new Label("Giá:"), 0, 1);
        grid.add(priceField, 1, 1);
        grid.add(new Label("Loại:"), 0, 2);
        grid.add(categoryCombo, 1, 2);
        grid.add(new Label("Mô tả:"), 0, 3);
        grid.add(descField, 1, 3);
        grid.add(new Label("Ảnh:"), 0, 4);
        grid.add(imageField, 1, 4);

        dialog.getDialogPane().setContent(grid);

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == addButtonType) {
                try {
                    String name = nameField.getText().trim();
                    double price = Double.parseDouble(priceField.getText().trim());
                    String category = categoryCombo.getValue();
                    String desc = descField.getText().trim();
                    String image = imageField.getText().trim();

                    if (DishDAO.addDish(name, price, category, desc, image)) {
                        loadDishes();
                        Alert alert = new Alert(Alert.AlertType.INFORMATION);
                        alert.setContentText("✓ Thêm món thành công!");
                        alert.showAndWait();
                    }
                } catch (Exception e) {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setContentText("❌ Lỗi: " + e.getMessage());
                    alert.showAndWait();
                }
            }
            return null;
        });

        dialog.showAndWait();
    }

    private void showEditDishDialog(Dish dish) {
        Dialog<Dish> dialog = new Dialog<>();
        dialog.setTitle("Sửa món");
        dialog.setHeaderText("Chỉnh sửa thông tin món: " + dish.getDishName());

        ButtonType saveButtonType = new ButtonType("Lưu", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(saveButtonType, ButtonType.CANCEL);

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20));

        TextField nameField = new TextField(dish.getDishName());
        TextField priceField = new TextField(String.valueOf(dish.getPrice()));
        ComboBox<String> categoryCombo = new ComboBox<>();
        categoryCombo.getItems().addAll("chicken", "pizza", "burger", "rice", "salad", "drink");
        categoryCombo.setValue(dish.getCategory());
        TextField descField = new TextField(dish.getDescription());
        TextField imageField = new TextField(dish.getImageUrl());

        grid.add(new Label("Tên món:"), 0, 0);
        grid.add(nameField, 1, 0);
        grid.add(new Label("Giá:"), 0, 1);
        grid.add(priceField, 1, 1);
        grid.add(new Label("Loại:"), 0, 2);
        grid.add(categoryCombo, 1, 2);
        grid.add(new Label("Mô tả:"), 0, 3);
        grid.add(descField, 1, 3);
        grid.add(new Label("Ảnh:"), 0, 4);
        grid.add(imageField, 1, 4);

        dialog.getDialogPane().setContent(grid);

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == saveButtonType) {
                try {
                    String name = nameField.getText().trim();
                    double price = Double.parseDouble(priceField.getText().trim());
                    String category = categoryCombo.getValue();
                    String desc = descField.getText().trim();
                    String image = imageField.getText().trim();

                    if (DishDAO.updateDish(dish.getDishId(), name, price, category, desc, image)) {
                        loadDishes();
                        Alert alert = new Alert(Alert.AlertType.INFORMATION);
                        alert.setContentText("✓ Cập nhật món thành công!");
                        alert.showAndWait();
                    }
                } catch (Exception e) {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setContentText("❌ Lỗi: " + e.getMessage());
                    alert.showAndWait();
                }
            }
            return null;
        });

        dialog.showAndWait();
    }

    private void deleteDish(Dish dish) {
        Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION);
        confirmAlert.setTitle("Xác nhận xóa");
        confirmAlert.setHeaderText("Xóa món: " + dish.getDishName());
        confirmAlert.setContentText("Bạn có chắc chắn muốn xóa món này?");

        confirmAlert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                if (DishDAO.deleteDish(dish.getDishId())) {
                    loadDishes();
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setContentText("✓ Đã xóa món!");
                    alert.showAndWait();
                }
            }
        });
    }

    private void showChart() {
        BorderPane root = (BorderPane) primaryStage.getScene().getRoot();

        VBox contentBox = new VBox(20);
        contentBox.setPadding(new Insets(30));

        Label titleLabel = new Label("📊 BIỂU ĐỒ THU CHI HÀNG THÁNG");
        titleLabel.setStyle("-fx-font-size: 22px; -fx-font-weight: bold;");

        // Biểu đồ đơn giản (giả lập data)
        VBox chartBox = new VBox(15);
        chartBox.setPadding(new Insets(20));
        chartBox.setStyle("-fx-background-color: white; -fx-border-color: #bdc3c7; " +
                "-fx-border-width: 2; -fx-border-radius: 10; -fx-background-radius: 10;");

        Label chartTitle = new Label("Doanh thu theo tháng (2024)");
        chartTitle.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");

        GridPane chartGrid = new GridPane();
        chartGrid.setHgap(20);
        chartGrid.setVgap(15);
        chartGrid.setPadding(new Insets(20));

        String[] months = {"T1", "T2", "T3", "T4", "T5", "T6", "T7", "T8", "T9", "T10", "T11", "T12"};
        int[] revenues = {15000000, 18000000, 20000000, 22000000, 25000000, 28000000,
                30000000, 32000000, 29000000, 31000000, 33000000, 35000000};

        for (int i = 0; i < months.length; i++) {
            VBox monthBox = new VBox(5);
            monthBox.setAlignment(Pos.BOTTOM_CENTER);

            int barHeight = (int) (revenues[i] / 200000); // Scale down
            StackPane bar = new StackPane();
            bar.setPrefSize(40, barHeight);
            bar.setStyle("-fx-background-color: #3498db; -fx-border-color: #2980b9; -fx-border-width: 1;");

            Label valueLabel = new Label(String.format("%.1fM", revenues[i] / 1000000.0));
            valueLabel.setStyle("-fx-font-size: 10px; -fx-font-weight: bold; -fx-text-fill: white;");

            bar.getChildren().add(valueLabel);

            Label monthLabel = new Label(months[i]);
            monthLabel.setStyle("-fx-font-size: 12px;");

            monthBox.getChildren().addAll(bar, monthLabel);
            chartGrid.add(monthBox, i, 0);
        }

        HBox statsBox = new HBox(40);
        statsBox.setAlignment(Pos.CENTER);
        statsBox.setPadding(new Insets(20));

        VBox totalBox = createStatBox("💰 Tổng doanh thu", "324,000,000đ", "#2ecc71");
        VBox avgBox = createStatBox("📊 TB/tháng", "27,000,000đ", "#3498db");
        VBox maxBox = createStatBox("⬆️ Cao nhất", "35,000,000đ (T12)", "#e74c3c");

        statsBox.getChildren().addAll(totalBox, avgBox, maxBox);

        chartBox.getChildren().addAll(chartTitle, new Separator(), chartGrid, new Separator(), statsBox);
        contentBox.getChildren().addAll(titleLabel, chartBox);

        root.setCenter(contentBox);
    }

    private VBox createStatBox(String title, String value, String color) {
        VBox box = new VBox(8);
        box.setAlignment(Pos.CENTER);
        box.setPadding(new Insets(15));
        box.setStyle("-fx-background-color: " + color + "; -fx-border-radius: 8; " +
                "-fx-background-radius: 8;");

        Label titleLabel = new Label(title);
        titleLabel.setStyle("-fx-font-size: 14px; -fx-text-fill: white;");

        Label valueLabel = new Label(value);
        valueLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold; -fx-text-fill: white;");

        box.getChildren().addAll(titleLabel, valueLabel);
        return box;
    }
}