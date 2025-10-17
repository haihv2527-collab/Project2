package com.example.project2.database;

import com.example.project2.model.Dish;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DishDAO {

    // Lấy tất cả món
    public static List<Dish> getAllDishes() {
        List<Dish> dishes = new ArrayList<>();
        String sql = "SELECT * FROM menu WHERE available = TRUE";

        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                dishes.add(new Dish(
                        rs.getInt("dish_id"),
                        rs.getString("dish_name"),
                        rs.getDouble("price"),
                        rs.getString("category"),
                        rs.getString("description"),
                        rs.getString("image_url"),
                        rs.getBoolean("available")
                ));
            }
        } catch (SQLException e) {
            System.out.println("Lỗi lấy món: " + e.getMessage());
        }
        return dishes;
    }

    // Lấy món theo category
    public static List<Dish> getDishesByCategory(String category) {
        List<Dish> dishes = new ArrayList<>();
        String sql = "SELECT * FROM menu WHERE category = ? AND available = TRUE";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, category);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                dishes.add(new Dish(
                        rs.getInt("dish_id"),
                        rs.getString("dish_name"),
                        rs.getDouble("price"),
                        rs.getString("category"),
                        rs.getString("description"),
                        rs.getString("image_url"),
                        rs.getBoolean("available")
                ));
            }
        } catch (SQLException e) {
            System.out.println("Lỗi lấy món theo category: " + e.getMessage());
        }
        return dishes;
    }

    // Thêm món mới (Admin)
    public static boolean addDish(String name, double price, String category,
                                  String description, String imageUrl) {
        String sql = "INSERT INTO menu (dish_name, price, category, description, image_url) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, name);
            stmt.setDouble(2, price);
            stmt.setString(3, category);
            stmt.setString(4, description);
            stmt.setString(5, imageUrl);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Lỗi thêm món: " + e.getMessage());
            return false;
        }
    }

    // Sửa món (Admin)
    public static boolean updateDish(int dishId, String name, double price,
                                     String category, String description, String imageUrl) {
        String sql = "UPDATE menu SET dish_name=?, price=?, category=?, description=?, image_url=? WHERE dish_id=?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, name);
            stmt.setDouble(2, price);
            stmt.setString(3, category);
            stmt.setString(4, description);
            stmt.setString(5, imageUrl);
            stmt.setInt(6, dishId);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Lỗi sửa món: " + e.getMessage());
            return false;
        }
    }

    // Xóa món (Admin) - chỉ đánh dấu không available
    public static boolean deleteDish(int dishId) {
        String sql = "UPDATE menu SET available = FALSE WHERE dish_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, dishId);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Lỗi xóa món: " + e.getMessage());
            return false;
        }
    }
}