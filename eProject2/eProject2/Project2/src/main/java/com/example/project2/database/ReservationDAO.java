package com.example.project2.database;

import com.example.project2.model.Reservation;
import java.sql.*;

public class ReservationDAO {

    // Tạo đặt bàn mới (trả về mã số đặt bàn)
    public static int createReservation(String customerName, String customerPhone,
                                        String customerEmail, int tableId, int numGuests, String time) {
        String sql = "INSERT INTO reservations (table_id, customer_name, customer_phone, customer_email, " +
                "reservation_time, num_guests, status) VALUES (?, ?, ?, ?, CONCAT(CURDATE(), ' ', ?), ?, 'confirmed')";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setInt(1, tableId);
            stmt.setString(2, customerName);
            stmt.setString(3, customerPhone);
            stmt.setString(4, customerEmail);
            stmt.setString(5, time + ":00");
            stmt.setInt(6, numGuests);

            if (stmt.executeUpdate() > 0) {
                // Cập nhật trạng thái bàn
                TableDAO.updateTableStatus(tableId, "reserved");

                ResultSet keys = stmt.getGeneratedKeys();
                if (keys.next()) {
                    return keys.getInt(1);
                }
            }
        } catch (SQLException e) {
            System.out.println("Lỗi tạo đặt bàn: " + e.getMessage());
        }
        return -1;
    }

    // Xác thực đặt bàn (email và mã đặt bàn)
    public static Reservation verifyReservation(String email, int reservationId) {
        String sql = "SELECT r.reservation_id, r.table_id, r.customer_name, r.customer_phone, r.status " +
                "FROM reservations r WHERE r.customer_email = ? AND r.reservation_id = ? AND r.status = 'confirmed'";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, email);
            stmt.setInt(2, reservationId);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return new Reservation(rs.getInt("reservation_id"), rs.getInt("table_id"),
                        rs.getString("customer_name"), rs.getString("customer_phone"),
                        rs.getString("status"));
            }
        } catch (SQLException e) {
            System.out.println("Lỗi xác thực: " + e.getMessage());
        }
        return null;
    }

    // Lấy thông tin đặt bàn
    public static Reservation getReservationById(int reservationId) {
        String sql = "SELECT reservation_id, table_id, customer_name, customer_phone, status " +
                "FROM reservations WHERE reservation_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, reservationId);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return new Reservation(rs.getInt("reservation_id"), rs.getInt("table_id"),
                        rs.getString("customer_name"), rs.getString("customer_phone"),
                        rs.getString("status"));
            }
        } catch (SQLException e) {
            System.out.println("Lỗi lấy đặt bàn: " + e.getMessage());
        }
        return null;
    }

    // Hoàn thành đặt bàn
    public static boolean completeReservation(int reservationId) {
        String sql = "UPDATE reservations SET status = 'completed' WHERE reservation_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, reservationId);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Lỗi hoàn thành: " + e.getMessage());
            return false;
        }
    }
}