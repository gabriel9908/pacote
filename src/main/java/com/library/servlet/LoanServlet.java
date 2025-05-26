
package com.library.servlet;

import com.library.dao.BookDAO;
import com.library.dao.LoanDAO;
import com.library.dao.UserDAO;
import com.library.model.Book;
import com.library.model.Loan;
import com.library.model.User;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

@WebServlet("/loans/*")
public class LoanServlet extends HttpServlet {
    private LoanDAO loanDAO;
    private BookDAO bookDAO;
    private UserDAO userDAO;
    private static final double DAILY_FINE = 1.0; // $1 per day
    
    @Override
    public void init() throws ServletException {
        loanDAO = new LoanDAO();
        bookDAO = new BookDAO();
        userDAO = new UserDAO();
    }
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        String pathInfo = request.getPathInfo();
        
        if (pathInfo == null || "/".equals(pathInfo)) {
            listLoans(request, response);
        } else if ("/issue".equals(pathInfo)) {
            showIssueForm(request, response);
        } else if ("/return".equals(pathInfo)) {
            showReturnForm(request, response);
        } else if ("/overdue".equals(pathInfo)) {
            listOverdueLoans(request, response);
        } else if ("/user".equals(pathInfo)) {
            listUserLoans(request, response);
        } else {
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
        }
    }
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        String pathInfo = request.getPathInfo();
        
        if ("/issue".equals(pathInfo)) {
            issueBook(request, response);
        } else if ("/return".equals(pathInfo)) {
            returnBook(request, response);
        } else {
            response.sendError(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
        }
    }
    
    private void listLoans(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        List<Loan> loans = loanDAO.getAllLoans();
        request.setAttribute("loans", loans);
        request.getRequestDispatcher("/WEB-INF/jsp/loans/list.jsp").forward(request, response);
    }
    
    private void listOverdueLoans(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        List<Loan> overdueLoans = loanDAO.getOverdueLoans();
        request.setAttribute("loans", overdueLoans);
        request.setAttribute("isOverdue", true);
        request.getRequestDispatcher("/WEB-INF/jsp/loans/list.jsp").forward(request, response);
    }
    
    private void listUserLoans(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        String userIdParam = request.getParameter("userId");
        if (userIdParam == null) {
            response.sendRedirect(request.getContextPath() + "/loans");
            return;
        }
        
        try {
            int userId = Integer.parseInt(userIdParam);
            List<Loan> loans = loanDAO.getLoansByUserId(userId);
            User user = userDAO.getUserById(userId);
            
            request.setAttribute("loans", loans);
            request.setAttribute("user", user);
            request.getRequestDispatcher("/WEB-INF/jsp/loans/user-loans.jsp").forward(request, response);
        } catch (NumberFormatException e) {
            response.sendRedirect(request.getContextPath() + "/loans?error=invalid");
        }
    }
    
    private void showIssueForm(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        List<User> users = userDAO.getAllUsers();
        List<Book> books = bookDAO.getAllBooks();
        
        request.setAttribute("users", users);
        request.setAttribute("books", books);
        request.getRequestDispatcher("/WEB-INF/jsp/loans/issue.jsp").forward(request, response);
    }
    
    private void issueBook(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        try {
            int userId = Integer.parseInt(request.getParameter("userId"));
            int bookId = Integer.parseInt(request.getParameter("bookId"));
            int loanPeriod = Integer.parseInt(request.getParameter("loanPeriod"));
            
            User user = userDAO.getUserById(userId);
            Book book = bookDAO.getBookById(bookId);
            
            if (user == null || book == null) {
                request.setAttribute("error", "Invalid user or book selected");
                showIssueForm(request, response);
                return;
            }
            
            // Check if book is available
            if (book.getAvailableCopies() <= 0) {
                request.setAttribute("error", "Book is not available");
                showIssueForm(request, response);
                return;
            }
            
            // Check user's loan limit
            int activeLoanCount = loanDAO.getActiveLoanCount(userId);
            if (activeLoanCount >= user.getMaxBooks()) {
                request.setAttribute("error", "User has reached maximum loan limit");
                showIssueForm(request, response);
                return;
            }
            
            // Check if user already has this book
            if (loanDAO.hasActiveLoan(userId, bookId)) {
                request.setAttribute("error", "User already has this book on loan");
                showIssueForm(request, response);
                return;
            }
            
            LocalDate loanDate = LocalDate.now();
            LocalDate dueDate = loanDate.plusDays(loanPeriod);
            
            Loan loan = new Loan(userId, bookId, loanDate, dueDate);
            
            if (loanDAO.addLoan(loan) && bookDAO.updateAvailability(bookId, -1)) {
                response.sendRedirect(request.getContextPath() + "/loans?success=issued");
            } else {
                request.setAttribute("error", "Failed to issue book");
                showIssueForm(request, response);
            }
            
        } catch (Exception e) {
            request.setAttribute("error", "Invalid input: " + e.getMessage());
            showIssueForm(request, response);
        }
    }
    
    private void showReturnForm(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        List<Loan> activeLoans = loanDAO.getActiveLoans();
        request.setAttribute("activeLoans", activeLoans);
        request.getRequestDispatcher("/WEB-INF/jsp/loans/return.jsp").forward(request, response);
    }
    
    private void returnBook(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        try {
            int loanId = Integer.parseInt(request.getParameter("loanId"));
            
            Loan loan = loanDAO.getLoanById(loanId);
            if (loan == null || !"ACTIVE".equals(loan.getStatus())) {
                request.setAttribute("error", "Invalid loan selected");
                showReturnForm(request, response);
                return;
            }
            
            LocalDate returnDate = LocalDate.now();
            double fineAmount = 0.0;
            
            // Calculate fine if overdue
            if (returnDate.isAfter(loan.getDueDate())) {
                long overdueDays = ChronoUnit.DAYS.between(loan.getDueDate(), returnDate);
                fineAmount = overdueDays * DAILY_FINE;
            }
            
            if (loanDAO.returnBook(loanId, returnDate, fineAmount) && 
                bookDAO.updateAvailability(loan.getBookId(), 1)) {
                
                String message = "Book returned successfully";
                if (fineAmount > 0) {
                    message += ". Fine amount: $" + String.format("%.2f", fineAmount);
                }
                response.sendRedirect(request.getContextPath() + "/loans?success=" + message);
            } else {
                request.setAttribute("error", "Failed to return book");
                showReturnForm(request, response);
            }
            
        } catch (Exception e) {
            request.setAttribute("error", "Invalid input: " + e.getMessage());
            showReturnForm(request, response);
        }
    }
}
