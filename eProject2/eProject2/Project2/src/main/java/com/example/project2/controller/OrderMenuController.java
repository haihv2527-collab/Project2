package com.example.project2.controller;

import com.example.project2.database.DishDAO;
import com.example.project2.model.Dish;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import java.util.*;

public class OrderMenuController {
    private Stage primaryStage;
    private int reservationId;
    private int tableId;
    private VBox dishDisplayBox;
    private VBox cartBox;
    private VBox orderedBox;
    private Label totalLabel;
    private Label orderedTotalLabel;
    private Map<Integer, CartItem> cartItems = new HashMap<>();
    private Map<Integer, CartItem> orderedItems = new HashMap<>();
    private String currentCategory = "all";

    public OrderMenuController(Stage primaryStage, int reservationId, int tableId) {
        this.primaryStage = primaryStage;
        this.reservationId = reservationId;
        this.tableId = tableId;
    }

    public void showMenu() {
        BorderPane root = new BorderPane();
        root.setStyle("-fx-background-color: #f5f5f5;");

        // Header
        HBox header = new HBox();
        header.setPadding(new Insets(15, 20, 15, 20));
        header.setStyle("-fx-background-color: #1E90FF;");
        header.setAlignment(Pos.CENTER_LEFT);
        header.setSpacing(15);

        Label titleLabel = new Label("🍽️ MENU");
        titleLabel.setStyle("-fx-font-size: 24px; -fx-font-weight: bold; -fx-text-fill: white;");

        Label tableLabel = new Label("Bàn: " + tableId);
        tableLabel.setStyle("-fx-font-size: 16px; -fx-text-fill: white;");

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        Button backBtn = new Button("◀ Đăng xuất");
        backBtn.setStyle("-fx-background-color: #e74c3c; -fx-text-fill: white;");

        header.getChildren().addAll(titleLabel, tableLabel, spacer, backBtn);

        // LEFT: Category filter
        VBox leftPanel = createCategoryPanel();

        // CENTER: Dish display
        VBox centerPanel = createDishPanel();

        // RIGHT: Cart + Ordered
        VBox rightPanel = createCartPanel();

        // Layout
        HBox mainContent = new HBox(10);
        mainContent.setPadding(new Insets(10));
        HBox.setHgrow(centerPanel, Priority.ALWAYS);
        mainContent.getChildren().addAll(leftPanel, centerPanel, rightPanel);

        root.setTop(header);
        root.setCenter(mainContent);

        Scene scene = new Scene(root, 1400, 800);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Order Menu");
    }

    private VBox createCategoryPanel() {
        VBox panel = new VBox(10);
        panel.setPrefWidth(200);
        panel.setPadding(new Insets(15));
        panel.setStyle("-fx-background-color: white; -fx-border-color: #ddd; -fx-border-width: 1;");

        Label titleLabel = new Label("📂 LỌC THEO");
        titleLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");

        Button allBtn = createCategoryButton("Tất cả", "all");
        Button chickenBtn = createCategoryButton("🍗 Gà", "chicken");
        Button pizzaBtn = createCategoryButton("🍕 Pizza", "pizza");
        Button burgerBtn = createCategoryButton("🍔 Burger", "burger");
        Button riceBtn = createCategoryButton("🍚 Cơm", "rice");
        Button saladBtn = createCategoryButton("🥗 Salad", "salad");
        Button drinkBtn = createCategoryButton("🥤 Đồ uống", "drink");

        panel.getChildren().addAll(titleLabel, new Separator(),
                allBtn, chickenBtn, pizzaBtn, burgerBtn, riceBtn, saladBtn, drinkBtn);

        return panel;
    }

    private Button createCategoryButton(String text, String category) {
        Button btn = new Button(text);
        btn.setPrefWidth(170);
        btn.setStyle("-fx-background-color: #ecf0f1; -fx-text-fill: #2c3e50; " +
                "-fx-font-size: 14px; -fx-padding: 10; -fx-alignment: center-left;");

        btn.setOnMouseEntered(e -> btn.setStyle(
                "-fx-background-color: #3498db; -fx-text-fill: white; " +
                        "-fx-font-size: 14px; -fx-padding: 10; -fx-alignment: center-left;"));

        btn.setOnMouseExited(e -> btn.setStyle(
                "-fx-background-color: #ecf0f1; -fx-text-fill: #2c3e50; " +
                        "-fx-font-size: 14px; -fx-padding: 10; -fx-alignment: center-left;"));

        btn.setOnAction(e -> {
            currentCategory = category;
            loadDishes();
        });

        return btn;
    }

    private VBox createDishPanel() {
        VBox panel = new VBox(10);
        panel.setPadding(new Insets(15));
        panel.setStyle("-fx-background-color: white;");

        Label titleLabel = new Label("📋 DANH SÁCH MÓN");
        titleLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");

        dishDisplayBox = new VBox(15);
        ScrollPane scrollPane = new ScrollPane(dishDisplayBox);
        scrollPane.setFitToWidth(true);
        scrollPane.setStyle("-fx-background-color: white;");
        VBox.setVgrow(scrollPane, Priority.ALWAYS);

        panel.getChildren().addAll(titleLabel, new Separator(), scrollPane);

        loadDishes();
        return panel;
    }

    private void loadDishes() {
        dishDisplayBox.getChildren().clear();

        List<Dish> dishes;
        if ("all".equals(currentCategory)) {
            dishes = DishDAO.getAllDishes();
        } else {
            dishes = DishDAO.getDishesByCategory(currentCategory);
        }

        for (Dish dish : dishes) {
            HBox dishBox = createDishBox(dish);
            dishDisplayBox.getChildren().add(dishBox);
        }
    }

    private HBox createDishBox(Dish dish) {
        HBox box = new HBox(15);
        box.setPadding(new Insets(15));
        box.setStyle("-fx-background-color: #f8f9fa; -fx-border-color: #dee2e6; " +
                "-fx-border-width: 1; -fx-border-radius: 8; -fx-background-radius: 8;");
        box.setAlignment(Pos.CENTER_LEFT);

        // Placeholder image
        Label imgLabel = new Label("🖼️");
        imgLabel.setStyle("-fx-font-size: 48px; -fx-pref-width: 80; -fx-pref-height: 80; " +
                "-fx-background-color: #e9ecef; -fx-alignment: center;");

        VBox infoBox = new VBox(5);
        HBox.setHgrow(infoBox, Priority.ALWAYS);

        Label nameLabel = new Label(dish.getDishName());
        nameLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");

        Label priceLabel = new Label(dish.getFormattedPrice());
        priceLabel.setStyle("-fx-font-size: 14px; -fx-text-fill: #e74c3c; -fx-font-weight: bold;");

        Label descLabel = new Label(dish.getDescription());
        descLabel.setStyle("-fx-font-size: 12px; -fx-text-fill: #6c757d;");

        infoBox.getChildren().addAll(nameLabel, priceLabel, descLabel);

        Button addBtn = new Button("+ Thêm");
        addBtn.setStyle("-fx-background-color: #2ecc71; -fx-text-fill: white; -fx-padding: 8 20;");
        addBtn.setOnAction(e -> addToCart(dish));

        box.getChildren().addAll(imgLabel, infoBox, addBtn);
        return box;
    }

    private VBox createCartPanel() {
        VBox panel = new VBox(10);
        panel.setPrefWidth(350);
        panel.setPadding(new Insets(15));
        panel.setStyle("-fx-background-color: white; -fx-border-color: #ddd; -fx-border-width: 1;");

        // PHẦN 1: Giỏ hàng chưa đặt
        Label cartTitle = new Label("🛒 GIỎ HÀNG");
        cartTitle.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");

        cartBox = new VBox(8);
        ScrollPane cartScroll = new ScrollPane(cartBox);
        cartScroll.setFitToWidth(true);
        cartScroll.setPrefHeight(250);
        cartScroll.setStyle("-fx-background-color: #f8f9fa;");

        totalLabel = new Label("Tổng: 0đ");
        totalLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold; -fx-text-fill: #e74c3c;");

        Button orderBtn = new Button("✓ ĐẶT MÓN");
        orderBtn.setPrefWidth(320);
        orderBtn.setStyle("-fx-background-color: #3498db; -fx-text-fill: white; " +
                "-fx-font-size: 14px; -fx-padding: 10;");
        orderBtn.setOnAction(e -> confirmOrder());

        // PHẦN 2: Đã đặt
        Label orderedTitle = new Label("📝 ĐÃ ĐẶT");
        orderedTitle.setStyle("-fx-font-size: 16px; -fx-font-weight: bold; -fx-text-fill: #27ae60;");

        orderedBox = new VBox(8);
        ScrollPane orderedScroll = new ScrollPane(orderedBox);
        orderedScroll.setFitToWidth(true);
        orderedScroll.setPrefHeight(200);
        orderedScroll.setStyle("-fx-background-color: #e8f8f5;");

        orderedTotalLabel = new Label("Tổng đã đặt: 0đ");
        orderedTotalLabel.setStyle("-fx-font-size: 14px; -fx-font-weight: bold; -fx-text-fill: #27ae60;");

        Button payBtn = new Button("💳 THANH TOÁN");
        payBtn.setPrefWidth(320);
        payBtn.setStyle("-fx-background-color: #e74c3c; -fx-text-fill: white; " +
                "-fx-font-size: 14px; -fx-padding: 10;");
        payBtn.setOnAction(e -> showPayment());

        panel.getChildren().addAll(
                cartTitle, cartScroll, totalLabel, orderBtn,
                new Separator(), orderedTitle, orderedScroll, orderedTotalLabel, payBtn
        );

        return panel;
    }

    private void addToCart(Dish dish) {
        if (cartItems.containsKey(dish.getDishId())) {
            cartItems.get(dish.getDishId()).quantity++;
        } else {
            cartItems.put(dish.getDishId(), new CartItem(dish, 1));
        }
        updateCartDisplay();
    }

    private void updateCartDisplay() {
        cartBox.getChildren().clear();
        double total = 0;

        for (CartItem item : cartItems.values()) {
            HBox itemBox = new HBox(10);
            itemBox.setStyle("-fx-background-color: white; -fx-padding: 8; -fx-border-color: #ddd; " +
                    "-fx-border-width: 1; -fx-border-radius: 5; -fx-background-radius: 5;");
            itemBox.setAlignment(Pos.CENTER_LEFT);

            VBox infoBox = new VBox(3);
            HBox.setHgrow(infoBox, Priority.ALWAYS);

            Label nameLabel = new Label(item.dish.getDishName());
            nameLabel.setStyle("-fx-font-size: 12px; -fx-font-weight: bold;");

            Label priceLabel = new Label(item.dish.getFormattedPrice());
            priceLabel.setStyle("-fx-font-size: 11px; -fx-text-fill: #e74c3c;");

            infoBox.getChildren().addAll(nameLabel, priceLabel);

            HBox quantityBox = new HBox(5);
            quantityBox.setAlignment(Pos.CENTER);

            Button minusBtn = new Button("-");
            minusBtn.setStyle("-fx-background-color: #e74c3c; -fx-text-fill: white; -fx-font-size: 10px;");
            minusBtn.setOnAction(e -> {
                if (item.quantity > 1) {
                    item.quantity--;
                } else {
                    cartItems.remove(item.dish.getDishId());
                }
                updateCartDisplay();
            });

            Label qtyLabel = new Label(String.valueOf(item.quantity));
            qtyLabel.setStyle("-fx-font-size: 12px; -fx-font-weight: bold;");

            Button plusBtn = new Button("+");
            plusBtn.setStyle("-fx-background-color: #2ecc71; -fx-text-fill: white; -fx-font-size: 10px;");
            plusBtn.setOnAction(e -> {
                item.quantity++;
                updateCartDisplay();
            });

            quantityBox.getChildren().addAll(minusBtn, qtyLabel, plusBtn);
            itemBox.getChildren().addAll(infoBox, quantityBox);
            cartBox.getChildren().add(itemBox);

            total += item.dish.getPrice() * item.quantity;
        }

        totalLabel.setText("Tổng: " + String.format("%,.0fđ", total));
    }

    private void confirmOrder() {
        if (cartItems.isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Giỏ hàng trống");
            alert.setContentText("Vui lòng chọn món trước khi đặt!");
            alert.showAndWait();
            return;
        }

        // Di chuyển từ giỏ hàng sang đã đặt
        for (CartItem item : cartItems.values()) {
            if (orderedItems.containsKey(item.dish.getDishId())) {
                orderedItems.get(item.dish.getDishId()).quantity += item.quantity;
            } else {
                orderedItems.put(item.dish.getDishId(), new CartItem(item.dish, item.quantity));
            }
        }
        cartItems.clear();
        updateCartDisplay();
        updateOrderedDisplay();

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Thành công");
        alert.setContentText("✓ Đã gửi order cho bếp!");
        alert.showAndWait();
    }

    private void updateOrderedDisplay() {
        orderedBox.getChildren().clear();
        double total = 0;

        for (CartItem item : orderedItems.values()) {
            HBox itemBox = new HBox(10);
            itemBox.setStyle("-fx-background-color: white; -fx-padding: 8; -fx-border-color: #27ae60; " +
                    "-fx-border-width: 1; -fx-border-radius: 5; -fx-background-radius: 5;");
            itemBox.setAlignment(Pos.CENTER_LEFT);

            VBox infoBox = new VBox(3);
            HBox.setHgrow(infoBox, Priority.ALWAYS);

            Label nameLabel = new Label(item.dish.getDishName());
            nameLabel.setStyle("-fx-font-size: 12px; -fx-font-weight: bold;");

            Label priceLabel = new Label(item.dish.getFormattedPrice() + " x " + item.quantity);
            priceLabel.setStyle("-fx-font-size: 11px; -fx-text-fill: #27ae60;");

            infoBox.getChildren().addAll(nameLabel, priceLabel);

            Label totalItemLabel = new Label(String.format("%,.0fđ", item.dish.getPrice() * item.quantity));
            totalItemLabel.setStyle("-fx-font-size: 12px; -fx-font-weight: bold; -fx-text-fill: #e74c3c;");

            itemBox.getChildren().addAll(infoBox, totalItemLabel);
            orderedBox.getChildren().add(itemBox);

            total += item.dish.getPrice() * item.quantity;
        }

        orderedTotalLabel.setText("Tổng đã đặt: " + String.format("%,.0fđ", total));
    }

    private void showPayment() {
        if (orderedItems.isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Chưa có món");
            alert.setContentText("Bạn chưa đặt món nào!");
            alert.showAndWait();
            return;
        }

        PaymentController paymentController = new PaymentController(primaryStage, reservationId, tableId, orderedItems);
        paymentController.showPayment();
    }

    // Inner class cho Cart Item
    class CartItem {
        Dish dish;
        int quantity;

        CartItem(Dish dish, int quantity) {
            this.dish = dish;
            this.quantity = quantity;
        }
    }
}