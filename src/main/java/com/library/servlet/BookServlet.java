
package com.library.servlet;

import com.library.dao.BookDAO;
import com.library.model.Book;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

@WebServlet("/books/*")
public class BookServlet extends HttpServlet {
    private BookDAO bookDAO;
    
    @Override
    public void init() throws ServletException {
        bookDAO = new BookDAO();
    }
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        String pathInfo = request.getPathInfo();
        
        if (pathInfo == null || "/".equals(pathInfo)) {
            listBooks(request, response);
        } else if ("/add".equals(pathInfo)) {
            showAddForm(request, response);
        } else if ("/edit".equals(pathInfo)) {
            showEditForm(request, response);
        } else if ("/delete".equals(pathInfo)) {
            deleteBook(request, response);
        } else if ("/search".equals(pathInfo)) {
            searchBooks(request, response);
        } else {
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
        }
    }
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        String pathInfo = request.getPathInfo();
        
        if ("/add".equals(pathInfo)) {
            addBook(request, response);
        } else if ("/edit".equals(pathInfo)) {
            updateBook(request, response);
        } else {
            response.sendError(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
        }
    }
    
    private void listBooks(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        List<Book> books = bookDAO.getAllBooks();
        request.setAttribute("books", books);
        request.getRequestDispatcher("/WEB-INF/jsp/books/list.jsp").forward(request, response);
    }
    
    private void searchBooks(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        String searchTerm = request.getParameter("q");
        List<Book> books;
        
        if (searchTerm != null && !searchTerm.trim().isEmpty()) {
            books = bookDAO.searchBooks(searchTerm.trim());
        } else {
            books = bookDAO.getAllBooks();
        }
        
        request.setAttribute("books", books);
        request.setAttribute("searchTerm", searchTerm);
        request.getRequestDispatcher("/WEB-INF/jsp/books/list.jsp").forward(request, response);
    }
    
    private void showAddForm(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        request.getRequestDispatcher("/WEB-INF/jsp/books/add.jsp").forward(request, response);
    }
    
    private void addBook(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        try {
            Book book = createBookFromRequest(request);
            
            if (bookDAO.getBookByIsbn(book.getIsbn()) != null) {
                request.setAttribute("error", "A book with this ISBN already exists");
                request.setAttribute("book", book);
                request.getRequestDispatcher("/WEB-INF/jsp/books/add.jsp").forward(request, response);
                return;
            }
            
            if (bookDAO.addBook(book)) {
                response.sendRedirect(request.getContextPath() + "/books?success=added");
            } else {
                request.setAttribute("error", "Failed to add book");
                request.setAttribute("book", book);
                request.getRequestDispatcher("/WEB-INF/jsp/books/add.jsp").forward(request, response);
            }
        } catch (Exception e) {
            request.setAttribute("error", "Invalid input: " + e.getMessage());
            request.getRequestDispatcher("/WEB-INF/jsp/books/add.jsp").forward(request, response);
        }
    }
    
    private void showEditForm(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        String idParam = request.getParameter("id");
        if (idParam == null) {
            response.sendRedirect(request.getContextPath() + "/books");
            return;
        }
        
        try {
            int id = Integer.parseInt(idParam);
            Book book = bookDAO.getBookById(id);
            
            if (book == null) {
                response.sendRedirect(request.getContextPath() + "/books?error=notfound");
                return;
            }
            
            request.setAttribute("book", book);
            request.getRequestDispatcher("/WEB-INF/jsp/books/edit.jsp").forward(request, response);
        } catch (NumberFormatException e) {
            response.sendRedirect(request.getContextPath() + "/books?error=invalid");
        }
    }
    
    private void updateBook(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        try {
            String idParam = request.getParameter("id");
            int id = Integer.parseInt(idParam);
            
            Book book = createBookFromRequest(request);
            book.setId(id);
            
            if (bookDAO.updateBook(book)) {
                response.sendRedirect(request.getContextPath() + "/books?success=updated");
            } else {
                request.setAttribute("error", "Failed to update book");
                request.setAttribute("book", book);
                request.getRequestDispatcher("/WEB-INF/jsp/books/edit.jsp").forward(request, response);
            }
        } catch (Exception e) {
            request.setAttribute("error", "Invalid input: " + e.getMessage());
            request.getRequestDispatcher("/WEB-INF/jsp/books/edit.jsp").forward(request, response);
        }
    }
    
    private void deleteBook(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        String idParam = request.getParameter("id");
        if (idParam == null) {
            response.sendRedirect(request.getContextPath() + "/books");
            return;
        }
        
        try {
            int id = Integer.parseInt(idParam);
            
            if (bookDAO.deleteBook(id)) {
                response.sendRedirect(request.getContextPath() + "/books?success=deleted");
            } else {
                response.sendRedirect(request.getContextPath() + "/books?error=deletefailed");
            }
        } catch (NumberFormatException e) {
            response.sendRedirect(request.getContextPath() + "/books?error=invalid");
        }
    }
    
    private Book createBookFromRequest(HttpServletRequest request) {
        String isbn = request.getParameter("isbn");
        String title = request.getParameter("title");
        String author = request.getParameter("author");
        String publisher = request.getParameter("publisher");
        String publishDateStr = request.getParameter("publishDate");
        String category = request.getParameter("category");
        String totalCopiesStr = request.getParameter("totalCopies");
        String description = request.getParameter("description");
        
        LocalDate publishDate = null;
        if (publishDateStr != null && !publishDateStr.trim().isEmpty()) {
            publishDate = LocalDate.parse(publishDateStr);
        }
        
        int totalCopies = Integer.parseInt(totalCopiesStr);
        
        return new Book(isbn, title, author, publisher, publishDate, category, totalCopies, description);
    }
}
