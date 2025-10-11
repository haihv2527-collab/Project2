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

    // create user, returns true if success, false if duplicate or error
    public boolean createUser(String email, String password) {
        String sql = "INSERT INTO users(email, password) VALUES(?, ?)";
        try (Connection conn = connect();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, email);
            ps.setString(2, password); // NOTE: plain text for demo only
            ps.executeUpdate();
            return true;
        } catch (SQLException e) {
            // unique constraint violation -> user exists
            // e.printStackTrace();
            return false;
        }
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
}
