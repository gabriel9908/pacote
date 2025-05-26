
package com.library.model;

import java.time.LocalDate;

public class Reservation {
    private int id;
    private int userId;
    private int bookId;
    private LocalDate reservationDate;
    private LocalDate expiryDate;
    private String status;
    private int queuePosition;
    
    // Transient fields for display
    private String userName;
    private String bookTitle;
    private String bookAuthor;
    
    public Reservation() {}
    
    public Reservation(int userId, int bookId, LocalDate reservationDate) {
        this.userId = userId;
        this.bookId = bookId;
        this.reservationDate = reservationDate;
        this.expiryDate = reservationDate.plusDays(7);
        this.status = "ACTIVE";
        this.queuePosition = 1;
    }
    
    // Getters and Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    
    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }
    
    public int getBookId() { return bookId; }
    public void setBookId(int bookId) { this.bookId = bookId; }
    
    public LocalDate getReservationDate() { return reservationDate; }
    public void setReservationDate(LocalDate reservationDate) { this.reservationDate = reservationDate; }
    
    public LocalDate getExpiryDate() { return expiryDate; }
    public void setExpiryDate(LocalDate expiryDate) { this.expiryDate = expiryDate; }
    
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    
    public int getQueuePosition() { return queuePosition; }
    public void setQueuePosition(int queuePosition) { this.queuePosition = queuePosition; }
    
    public String getUserName() { return userName; }
    public void setUserName(String userName) { this.userName = userName; }
    
    public String getBookTitle() { return bookTitle; }
    public void setBookTitle(String bookTitle) { this.bookTitle = bookTitle; }
    
    public String getBookAuthor() { return bookAuthor; }
    public void setBookAuthor(String bookAuthor) { this.bookAuthor = bookAuthor; }
    
    public boolean isExpired() {
        return LocalDate.now().isAfter(expiryDate);
    }
}
