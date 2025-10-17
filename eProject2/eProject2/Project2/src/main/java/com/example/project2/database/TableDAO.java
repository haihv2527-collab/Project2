package com.example.project2.database;

import com.example.project2.model.RestaurantTable;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TableDAO {

    public static List<RestaurantTable> getAllTables() {
        List<RestaurantTable> tables = new ArrayList<>();
        String sql = "SELECT table_id, table_name, capacity, status, position_x, position_y FROM tables";

        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                tables.add(new RestaurantTable(
                        rs.getInt("table_id"),
                        rs.getString("table_name"),
                        rs.getInt("capacity"),
                        rs.getString("status"),
                        rs.getInt("position_x"),
                        rs.getInt("position_y")
                ));
            }
        } catch (SQLException e) {
            System.out.println("Lỗi lấy bàn: " + e.getMessage());
        }
        return tables;
    }

    public static RestaurantTable getTableById(int tableId) {
        String sql = "SELECT table_id, table_name, capacity, status, position_x, position_y FROM tables WHERE table_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, tableId);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return new RestaurantTable(
                        rs.getInt("table_id"),
                        rs.getString("table_name"),
                        rs.getInt("capacity"),
                        rs.getString("status"),
                        rs.getInt("position_x"),
                        rs.getInt("position_y")
                );
            }
        } catch (SQLException e) {
            System.out.println("Lỗi lấy bàn: " + e.getMessage());
        }
        return null;
    }

    public static boolean updateTableStatus(int tableId, String status) {
        String sql = "UPDATE tables SET status = ? WHERE table_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, status);
            stmt.setInt(2, tableId);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Lỗi cập nhật bàn: " + e.getMessage());
            return false;
        }
    }
}