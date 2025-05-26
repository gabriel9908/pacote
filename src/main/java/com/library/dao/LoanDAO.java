
package com.library.dao;

import com.library.model.Loan;
import com.library.util.DatabaseConnection;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class LoanDAO {
    
    public boolean addLoan(Loan loan) {
        String sql = "INSERT INTO loans (user_id, book_id, loan_date, due_date, status, fine_amount) VALUES (?, ?, ?, ?, ?, ?)";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, loan.getUserId());
            stmt.setInt(2, loan.getBookId());
            stmt.setDate(3, Date.valueOf(loan.getLoanDate()));
            stmt.setDate(4, Date.valueOf(loan.getDueDate()));
            stmt.setString(5, loan.getStatus());
            stmt.setDouble(6, loan.getFineAmount());
            
            return stmt.executeUpdate() > 0;
            
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    public Loan getLoanById(int id) {
        String sql = "SELECT l.*, u.first_name, u.last_name, b.title, b.author " +
                    "FROM loans l " +
                    "JOIN users u ON l.user_id = u.id " +
                    "JOIN books b ON l.book_id = b.id " +
                    "WHERE l.id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                return mapResultSetToLoan(rs);
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return null;
    }
    
    public List<Loan> getAllLoans() {
        List<Loan> loans = new ArrayList<>();
        String sql = "SELECT l.*, u.first_name, u.last_name, b.title, b.author " +
                    "FROM loans l " +
                    "JOIN users u ON l.user_id = u.id " +
                    "JOIN books b ON l.book_id = b.id " +
                    "ORDER BY l.loan_date DESC";
        
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                loans.add(mapResultSetToLoan(rs));
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return loans;
    }
    
    public List<Loan> getLoansByUserId(int userId) {
        List<Loan> loans = new ArrayList<>();
        String sql = "SELECT l.*, u.first_name, u.last_name, b.title, b.author " +
                    "FROM loans l " +
                    "JOIN users u ON l.user_id = u.id " +
                    "JOIN books b ON l.book_id = b.id " +
                    "WHERE l.user_id = ? " +
                    "ORDER BY l.loan_date DESC";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                loans.add(mapResultSetToLoan(rs));
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return loans;
    }
    
    public List<Loan> getActiveLoans() {
        List<Loan> loans = new ArrayList<>();
        String sql = "SELECT l.*, u.first_name, u.last_name, b.title, b.author " +
                    "FROM loans l " +
                    "JOIN users u ON l.user_id = u.id " +
                    "JOIN books b ON l.book_id = b.id " +
                    "WHERE l.status = 'ACTIVE' " +
                    "ORDER BY l.due_date";
        
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                loans.add(mapResultSetToLoan(rs));
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return loans;
    }
    
    public List<Loan> getOverdueLoans() {
        List<Loan> loans = new ArrayList<>();
        String sql = "SELECT l.*, u.first_name, u.last_name, b.title, b.author " +
                    "FROM loans l " +
                    "JOIN users u ON l.user_id = u.id " +
                    "JOIN books b ON l.book_id = b.id " +
                    "WHERE l.status = 'ACTIVE' AND l.due_date < CURRENT_DATE " +
                    "ORDER BY l.due_date";
        
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                loans.add(mapResultSetToLoan(rs));
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return loans;
    }
    
    public boolean returnBook(int loanId, LocalDate returnDate, double fineAmount) {
        String sql = "UPDATE loans SET return_date = ?, status = 'RETURNED', fine_amount = ? WHERE id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setDate(1, Date.valueOf(returnDate));
            stmt.setDouble(2, fineAmount);
            stmt.setInt(3, loanId);
            
            return stmt.executeUpdate() > 0;
            
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    public boolean updateLoan(Loan loan) {
        String sql = "UPDATE loans SET user_id = ?, book_id = ?, loan_date = ?, due_date = ?, return_date = ?, status = ?, fine_amount = ? WHERE id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, loan.getUserId());
            stmt.setInt(2, loan.getBookId());
            stmt.setDate(3, Date.valueOf(loan.getLoanDate()));
            stmt.setDate(4, Date.valueOf(loan.getDueDate()));
            stmt.setDate(5, loan.getReturnDate() != null ? Date.valueOf(loan.getReturnDate()) : null);
            stmt.setString(6, loan.getStatus());
            stmt.setDouble(7, loan.getFineAmount());
            stmt.setInt(8, loan.getId());
            
            return stmt.executeUpdate() > 0;
            
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    public int getActiveLoanCount(int userId) {
        String sql = "SELECT COUNT(*) FROM loans WHERE user_id = ? AND status = 'ACTIVE'";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                return rs.getInt(1);
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return 0;
    }
    
    public boolean hasActiveLoan(int userId, int bookId) {
        String sql = "SELECT COUNT(*) FROM loans WHERE user_id = ? AND book_id = ? AND status = 'ACTIVE'";
        
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
    
    private Loan mapResultSetToLoan(ResultSet rs) throws SQLException {
        Loan loan = new Loan();
        loan.setId(rs.getInt("id"));
        loan.setUserId(rs.getInt("user_id"));
        loan.setBookId(rs.getInt("book_id"));
        
        Date loanDate = rs.getDate("loan_date");
        if (loanDate != null) {
            loan.setLoanDate(loanDate.toLocalDate());
        }
        
        Date dueDate = rs.getDate("due_date");
        if (dueDate != null) {
            loan.setDueDate(dueDate.toLocalDate());
        }
        
        Date returnDate = rs.getDate("return_date");
        if (returnDate != null) {
            loan.setReturnDate(returnDate.toLocalDate());
        }
        
        loan.setStatus(rs.getString("status"));
        loan.setFineAmount(rs.getDouble("fine_amount"));
        
        // Set transient fields
        loan.setUserName(rs.getString("first_name") + " " + rs.getString("last_name"));
        loan.setBookTitle(rs.getString("title"));
        loan.setBookAuthor(rs.getString("author"));
        
        return loan;
    }
}
