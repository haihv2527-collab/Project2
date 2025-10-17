package com.example.project2.controller;

import com.example.project2.database.TableDAO;
import com.example.project2.database.ReservationDAO;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import java.util.Map;

public class PaymentController {
    private Stage primaryStage;
    private int reservationId;
    private int tableId;
    private Map<Integer, OrderMenuController.CartItem> orderedItems;

    public PaymentController(Stage primaryStage, int reservationId, int tableId,
                             Map<Integer, OrderMenuController.CartItem> orderedItems) {
        this.primaryStage = primaryStage;
        this.reservationId = reservationId;
        this.tableId = tableId;
        this.orderedItems = orderedItems;
    }

    public void showPayment() {
        BorderPane root = new BorderPane();
        root.setStyle("-fx-background-color: #ecf0f1;");

        // Header
        HBox header = new HBox();
        header.setPadding(new Insets(20));
        header.setStyle("-fx-background-color: #e74c3c;");
        header.setAlignment(Pos.CENTER);

        Label titleLabel = new Label("💳 THANH TOÁN");
        titleLabel.setStyle("-fx-font-size: 28px; -fx-font-weight: bold; -fx-text-fill: white;");

        header.getChildren().add(titleLabel);

        // Center: Bill + QR
        HBox centerBox = new HBox(30);
        centerBox.setPadding(new Insets(30));
        centerBox.setAlignment(Pos.CENTER);

        // Left: Bill
        VBox billBox = createBillBox();

        // Right: QR Code
        VBox qrBox = createQRBox();

        centerBox.getChildren().addAll(billBox, qrBox);

        // Bottom: Buttons
        HBox buttonBox = new HBox(15);
        buttonBox.setPadding(new Insets(20));
        buttonBox.setAlignment(Pos.CENTER);

        Button backBtn = new Button("◀ Quay lại menu");
        backBtn.setStyle("-fx-background-color: #95a5a6; -fx-text-fill: white; -fx-font-size: 14px; -fx-padding: 10 20;");
        backBtn.setOnAction(e -> {
            OrderMenuController orderMenu = new OrderMenuController(primaryStage, reservationId, tableId);
            orderMenu.showMenu();
        });

        Button confirmBtn = new Button("✓ XÁC NHẬN ĐÃ THANH TOÁN");
        confirmBtn.setPrefWidth(250);
        confirmBtn.setStyle("-fx-background-color: #2ecc71; -fx-text-fill: white; " +
                "-fx-font-size: 16px; -fx-padding: 12 20; -fx-font-weight: bold;");
        confirmBtn.setOnAction(e -> confirmPayment());

        buttonBox.getChildren().addAll(backBtn, confirmBtn);

        root.setTop(header);
        root.setCenter(centerBox);
        root.setBottom(buttonBox);

        Scene scene = new Scene(root, 1000, 700);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Thanh toán");
    }

    private VBox createBillBox() {
        VBox box = new VBox(15);
        box.setPrefWidth(450);
        box.setPadding(new Insets(25));
        box.setStyle("-fx-background-color: white; -fx-border-color: #bdc3c7; " +
                "-fx-border-width: 2; -fx-border-radius: 10; -fx-background-radius: 10;");

        Label titleLabel = new Label("📄 HÓA ĐƠN");
        titleLabel.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");

        Label tableLabel = new Label("Bàn: " + tableId);
        tableLabel.setStyle("-fx-font-size: 14px; -fx-text-fill: #7f8c8d;");

        Separator separator1 = new Separator();

        VBox itemsBox = new VBox(8);
        double total = 0;

        for (OrderMenuController.CartItem item : orderedItems.values()) {
            HBox itemRow = new HBox();
            itemRow.setAlignment(Pos.CENTER_LEFT);

            Label nameQtyLabel = new Label(item.dish.getDishName() + " x" + item.quantity);
            nameQtyLabel.setStyle("-fx-font-size: 13px;");
            HBox.setHgrow(nameQtyLabel, Priority.ALWAYS);

            Label priceLabel = new Label(String.format("%,.0fđ", item.dish.getPrice() * item.quantity));
            priceLabel.setStyle("-fx-font-size: 13px; -fx-font-weight: bold;");

            itemRow.getChildren().addAll(nameQtyLabel, priceLabel);
            itemsBox.getChildren().add(itemRow);

            total += item.dish.getPrice() * item.quantity;
        }

        Separator separator2 = new Separator();

        HBox totalRow = new HBox();
        totalRow.setAlignment(Pos.CENTER_LEFT);

        Label totalTextLabel = new Label("TỔNG CỘNG:");
        totalTextLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");
        HBox.setHgrow(totalTextLabel, Priority.ALWAYS);

        Label totalValueLabel = new Label(String.format("%,.0fđ", total));
        totalValueLabel.setStyle("-fx-font-size: 20px; -fx-font-weight: bold; -fx-text-fill: #e74c3c;");

        totalRow.getChildren().addAll(totalTextLabel, totalValueLabel);

        Label noteLabel = new Label("⚠ Vui lòng quét mã QR để thanh toán");
        noteLabel.setStyle("-fx-font-size: 12px; -fx-text-fill: #e67e22; -fx-font-style: italic;");
        noteLabel.setWrapText(true);

        box.getChildren().addAll(titleLabel, tableLabel, separator1, itemsBox, separator2, totalRow, noteLabel);

        return box;
    }

    private VBox createQRBox() {
        VBox box = new VBox(15);
        box.setPrefWidth(350);
        box.setPadding(new Insets(25));
        box.setAlignment(Pos.CENTER);
        box.setStyle("-fx-background-color: white; -fx-border-color: #bdc3c7; " +
                "-fx-border-width: 2; -fx-border-radius: 10; -fx-background-radius: 10;");

        Label titleLabel = new Label("📱 QUÉT MÃ QR");
        titleLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");

        // QR Code placeholder (chữ nhật lớn)
        StackPane qrPane = new StackPane();
        qrPane.setPrefSize(250, 250);
        qrPane.setStyle("-fx-background-color: white; -fx-border-color: #34495e; -fx-border-width: 3;");

        VBox qrContent = new VBox(10);
        qrContent.setAlignment(Pos.CENTER);

        Label qrIcon = new Label("📲");
        qrIcon.setStyle("-fx-font-size: 80px;");

        Label qrText = new Label("QR CODE\nTHANH TOÁN");
        qrText.setStyle("-fx-font-size: 16px; -fx-font-weight: bold; -fx-text-fill: #2c3e50; -fx-text-alignment: center;");
        qrText.setWrapText(true);

        // Tính tổng tiền
        double total = 0;
        for (OrderMenuController.CartItem item : orderedItems.values()) {
            total += item.dish.getPrice() * item.quantity;
        }

        Label amountLabel = new Label(String.format("%,.0fđ", total));
        amountLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold; -fx-text-fill: #e74c3c;");

        qrContent.getChildren().addAll(qrIcon, qrText, amountLabel);
        qrPane.getChildren().add(qrContent);

        Label infoLabel = new Label("Ngân hàng: MB Bank\nSTK: 0123456789\nChủ TK: RESTAURANT");
        infoLabel.setStyle("-fx-font-size: 12px; -fx-text-fill: #7f8c8d; -fx-text-alignment: center;");
        infoLabel.setWrapText(true);

        box.getChildren().addAll(titleLabel, qrPane, infoLabel);

        return box;
    }

    private void confirmPayment() {
        Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION);
        confirmAlert.setTitle("Xác nhận thanh toán");
        confirmAlert.setHeaderText("Bạn đã thanh toán xong chưa?");
        confirmAlert.setContentText("Vui lòng xác nhận sau khi quét mã QR thành công!");

        confirmAlert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                // Cập nhật trạng thái bàn về available
                TableDAO.updateTableStatus(tableId, "available");

                // Hoàn thành reservation
                ReservationDAO.completeReservation(reservationId);

                showPaymentSuccess();
            }
        });
    }

    private void showPaymentSuccess() {
        VBox root = new VBox(30);
        root.setAlignment(Pos.CENTER);
        root.setPadding(new Insets(50));
        root.setStyle("-fx-background-color: linear-gradient(to bottom, #2ecc71, #27ae60);");

        Label successIcon = new Label("✓");
        successIcon.setStyle("-fx-font-size: 100px; -fx-text-fill: white;");

        Label successLabel = new Label("THANH TOÁN THÀNH CÔNG!");
        successLabel.setStyle("-fx-font-size: 32px; -fx-font-weight: bold; -fx-text-fill: white;");

        Label thankLabel = new Label("Cảm ơn quý khách đã sử dụng dịch vụ!");
        thankLabel.setStyle("-fx-font-size: 18px; -fx-text-fill: white;");

        Label emojiLabel = new Label("🎉 🍽️ 🎊");
        emojiLabel.setStyle("-fx-font-size: 48px;");

        Button homeBtn = new Button("🏠 Về trang chủ");
        homeBtn.setPrefWidth(200);
        homeBtn.setStyle("-fx-background-color: white; -fx-text-fill: #2ecc71; " +
                "-fx-font-size: 16px; -fx-padding: 12 20; -fx-font-weight: bold;");
//        homeBtn.setOnAction(e -> {
//            LoginController loginController = new LoginController(primaryStage);
//            loginController.showLoginScene();
//        });

        root.getChildren().addAll(successIcon, successLabel, thankLabel, emojiLabel, homeBtn);

        Scene scene = new Scene(root, 800, 600);
        primaryStage.setScene(scene);
    }
}