
package com.library.dao;

import com.library.model.Reservation;
import com.library.util.DatabaseConnection;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class ReservationDAO {
    
    public boolean addReservation(Reservation reservation) {
        String sql = "INSERT INTO reservations (user_id, book_id, reservation_date, expiry_date, status, queue_position) VALUES (?, ?, ?, ?, ?, ?)";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, reservation.getUserId());
            stmt.setInt(2, reservation.getBookId());
            stmt.setDate(3, Date.valueOf(reservation.getReservationDate()));
            stmt.setDate(4, Date.valueOf(reservation.getExpiryDate()));
            stmt.setString(5, reservation.getStatus());
            stmt.setInt(6, getNextQueuePosition(reservation.getBookId()));
            
            return stmt.executeUpdate() > 0;
            
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    public Reservation getReservationById(int id) {
        String sql = "SELECT r.*, u.first_name, u.last_name, b.title, b.author " +
                    "FROM reservations r " +
                    "JOIN users u ON r.user_id = u.id " +
                    "JOIN books b ON r.book_id = b.id " +
                    "WHERE r.id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                return mapResultSetToReservation(rs);
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return null;
    }
    
    public List<Reservation> getAllReservations() {
        List<Reservation> reservations = new ArrayList<>();
        String sql = "SELECT r.*, u.first_name, u.last_name, b.title, b.author " +
                    "FROM reservations r " +
                    "JOIN users u ON r.user_id = u.id " +
                    "JOIN books b ON r.book_id = b.id " +
                    "ORDER BY r.reservation_date DESC";
        
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                reservations.add(mapResultSetToReservation(rs));
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return reservations;
    }
    
    public List<Reservation> getReservationsByUserId(int userId) {
        List<Reservation> reservations = new ArrayList<>();
        String sql = "SELECT r.*, u.first_name, u.last_name, b.title, b.author " +
                    "FROM reservations r " +
                    "JOIN users u ON r.user_id = u.id " +
                    "JOIN books b ON r.book_id = b.id " +
                    "WHERE r.user_id = ? " +
                    "ORDER BY r.reservation_date DESC";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                reservations.add(mapResultSetToReservation(rs));
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return reservations;
    }
    
    public List<Reservation> getActiveReservationsByBookId(int bookId) {
        List<Reservation> reservations = new ArrayList<>();
        String sql = "SELECT r.*, u.first_name, u.last_name, b.title, b.author " +
                    "FROM reservations r " +
                    "JOIN users u ON r.user_id = u.id " +
                    "JOIN books b ON r.book_id = b.id " +
                    "WHERE r.book_id = ? AND r.status = 'ACTIVE' " +
                    "ORDER BY r.queue_position";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, bookId);
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                reservations.add(mapResultSetToReservation(rs));
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return reservations;
    }
    
    public boolean cancelReservation(int reservationId) {
        String sql = "UPDATE reservations SET status = 'CANCELLED' WHERE id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, reservationId);
            return stmt.executeUpdate() > 0;
            
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    public boolean fulfillReservation(int reservationId) {
        String sql = "UPDATE reservations SET status = 'FULFILLED' WHERE id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, reservationId);
            return stmt.executeUpdate() > 0;
            
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    public boolean hasActiveReservation(int userId, int bookId) {
        String sql = "SELECT COUNT(*) FROM reservations WHERE user_id = ? AND book_id = ? AND status = 'ACTIVE'";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, userId);
            stmt.setInt(2, bookId);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return false;
    }
    
    public void updateQueuePositions(int bookId) {
        String sql = "UPDATE reservations SET queue_position = " +
                    "(SELECT new_position FROM " +
                    "(SELECT id, ROW_NUMBER() OVER (ORDER BY reservation_date) as new_position " +
                    "FROM reservations WHERE book_id = ? AND status = 'ACTIVE') AS ranked " +
                    "WHERE ranked.id = reservations.id) " +
                    "WHERE book_id = ? AND status = 'ACTIVE'";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, bookId);
            stmt.setInt(2, bookId);
            stmt.executeUpdate();
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    private int getNextQueuePosition(int bookId) {
        String sql = "SELECT COALESCE(MAX(queue_position), 0) + 1 FROM reservations WHERE book_id = ? AND status = 'ACTIVE'";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, bookId);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                return rs.getInt(1);
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return 1;
    }
    
    private Reservation mapResultSetToReservation(ResultSet rs) throws SQLException {
        Reservation reservation = new Reservation();
        reservation.setId(rs.getInt("id"));
        reservation.setUserId(rs.getInt("user_id"));
        reservation.setBookId(rs.getInt("book_id"));
        
        Date reservationDate = rs.getDate("reservation_date");
        if (reservationDate != null) {
            reservation.setReservationDate(reservationDate.toLocalDate());
        }
        
        Date expiryDate = rs.getDate("expiry_date");
        if (expiryDate != null) {
            reservation.setExpiryDate(expiryDate.toLocalDate());
        }
        
        reservation.setStatus(rs.getString("status"));
        reservation.setQueuePosition(rs.getInt("queue_position"));
        
        // Set transient fields
        reservation.setUserName(rs.getString("first_name") + " " + rs.getString("last_name"));
        reservation.setBookTitle(rs.getString("title"));
        reservation.setBookAuthor(rs.getString("author"));
        
        return reservation;
    }
}
