package com.example.project2.controllers;

import java.sql.*;

public class UserDAO {
    private static final String DB_URL = "jdbc:sqlite:users.db";
    private static UserDAO instance;

    private UserDAO() {
        createTableIfNotExist();
    }

    public static synchronized UserDAO getInstance() {
        if (instance == null) instance = new UserDAO();
        return instance;
    }

    private Connection connect() throws SQLException {
        return DriverManager.getConnection(DB_URL);
    }

    private void createTableIfNotExist() {
        String sql = "CREATE TABLE IF NOT EXISTS users (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "username TEXT NOT NULL, " +
                "email TEXT NOT NULL UNIQUE, " +
                "password TEXT NOT NULL" +
                ");";
        try (Connection conn = connect();
             Statement st = conn.createStatement()) {
            st.execute(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public String getUsernameByEmail(String email) {
        String sql = "SELECT username FROM users WHERE email = ?";
        try (Connection conn = connect();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, email);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getString("username");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }


    // validate login
    public boolean validateLogin(String email, String password) {
        String sql = "SELECT id FROM users WHERE email = ? AND password = ?";
        try (Connection conn = connect();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, email);
            ps.setString(2, password);
            ResultSet rs = ps.executeQuery();
            return rs.next();
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Tạo tài khoản mới
    public boolean createUser(String username, String email, String password) {
        String checkSql = "SELECT email FROM users WHERE email = ?";
        String insertSql = "INSERT INTO users (username, email, password) VALUES (?, ?, ?)";

        try (Connection conn = connect()) {
            // Kiểm tra email tồn tại
            try (PreparedStatement checkStmt = conn.prepareStatement(checkSql)) {
                checkStmt.setString(1, email);
                ResultSet rs = checkStmt.executeQuery();
                if (rs.next()) {
                    return false; // Email đã tồn tại
                }
            }

            // Thêm người dùng mới
            try (PreparedStatement insertStmt = conn.prepareStatement(insertSql)) {
                insertStmt.setString(1, username);
                insertStmt.setString(2, email);
                insertStmt.setString(3, password);
                insertStmt.executeUpdate();
                return true; // Thành công
            }

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
