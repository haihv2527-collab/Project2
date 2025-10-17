package com.example.project2.database;

import java.sql.*;

public class DatabaseConnection {
    private static final String URL = "jdbc:mysql://localhost:3306/restaurant_db";
    private static final String USER = "root";
    private static final String PASSWORD = "Hieu@11082001luohy.abcxyz"; // Thay password MySQL của bạn
    private static Connection connection;

    public static Connection getConnection() {
        try {
            if (connection == null || connection.isClosed()) {
                Class.forName("com.mysql.cj.jdbc.Driver");
                connection = DriverManager.getConnection(URL, USER, PASSWORD);
                System.out.println("✓ Kết nối database thành công");
            }
            return connection;
        } catch (SQLException | ClassNotFoundException e) {
            System.out.println("✗ Lỗi kết nối: " + e.getMessage());
            return null;
        }
    }

    public static void closeConnection() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
                System.out.println("✓ Đóng kết nối database");
            }
        } catch (SQLException e) {
            System.out.println("✗ Lỗi đóng kết nối: " + e.getMessage());
        }
    }
}