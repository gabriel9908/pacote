
package com.library.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseConnection {
    private static final String DB_URL = "jdbc:h2:mem:librarydb;DB_CLOSE_DELAY=-1";
    private static final String DB_USER = "sa";
    private static final String DB_PASSWORD = "";
    
    static {
        try {
            Class.forName("org.h2.Driver");
            initializeDatabase();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
    
    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
    }
    
    private static void initializeDatabase() {
        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement()) {
            
            // Create Users table
            stmt.execute("CREATE TABLE IF NOT EXISTS users (" +
                "id INT AUTO_INCREMENT PRIMARY KEY," +
                "username VARCHAR(50) UNIQUE NOT NULL," +
                "password VARCHAR(255) NOT NULL," +
                "first_name VARCHAR(50) NOT NULL," +
                "last_name VARCHAR(50) NOT NULL," +
                "email VARCHAR(100) UNIQUE NOT NULL," +
                "phone VARCHAR(20)," +
                "address TEXT," +
                "role VARCHAR(20) DEFAULT 'USER'," +
                "registration_date DATE DEFAULT CURRENT_DATE," +
                "status VARCHAR(20) DEFAULT 'ACTIVE'," +
                "max_books INT DEFAULT 5)");
            
            // Create Books table
            stmt.execute("CREATE TABLE IF NOT EXISTS books (" +
                "id INT AUTO_INCREMENT PRIMARY KEY," +
                "isbn VARCHAR(20) UNIQUE NOT NULL," +
                "title VARCHAR(255) NOT NULL," +
                "author VARCHAR(255) NOT NULL," +
                "publisher VARCHAR(255)," +
                "publish_date DATE," +
                "category VARCHAR(100)," +
                "total_copies INT DEFAULT 1," +
                "available_copies INT DEFAULT 1," +
                "description TEXT," +
                "status VARCHAR(20) DEFAULT 'AVAILABLE')");
            
            // Create Loans table
            stmt.execute("CREATE TABLE IF NOT EXISTS loans (" +
                "id INT AUTO_INCREMENT PRIMARY KEY," +
                "user_id INT NOT NULL," +
                "book_id INT NOT NULL," +
                "loan_date DATE NOT NULL," +
                "due_date DATE NOT NULL," +
                "return_date DATE," +
                "status VARCHAR(20) DEFAULT 'ACTIVE'," +
                "fine_amount DECIMAL(10,2) DEFAULT 0.00," +
                "FOREIGN KEY (user_id) REFERENCES users(id)," +
                "FOREIGN KEY (book_id) REFERENCES books(id))");
            
            // Create Reservations table
            stmt.execute("CREATE TABLE IF NOT EXISTS reservations (" +
                "id INT AUTO_INCREMENT PRIMARY KEY," +
                "user_id INT NOT NULL," +
                "book_id INT NOT NULL," +
                "reservation_date DATE NOT NULL," +
                "expiry_date DATE NOT NULL," +
                "status VARCHAR(20) DEFAULT 'ACTIVE'," +
                "queue_position INT DEFAULT 1," +
                "FOREIGN KEY (user_id) REFERENCES users(id)," +
                "FOREIGN KEY (book_id) REFERENCES books(id))");
            
            // Insert default admin user
            stmt.execute("INSERT INTO users (username, password, first_name, last_name, email, role) " +
                "VALUES ('admin', 'admin123', 'System', 'Administrator', 'admin@library.com', 'ADMIN') " +
                "ON DUPLICATE KEY UPDATE username=username");
            
            System.out.println("Database initialized successfully");
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
