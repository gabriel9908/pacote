
package com.library.servlet;

import com.library.dao.UserDAO;
import com.library.model.User;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet("/users/*")
public class UserServlet extends HttpServlet {
    private UserDAO userDAO;
    
    @Override
    public void init() throws ServletException {
        userDAO = new UserDAO();
    }
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        String pathInfo = request.getPathInfo();
        
        if (pathInfo == null || "/".equals(pathInfo)) {
            listUsers(request, response);
        } else if ("/add".equals(pathInfo)) {
            showAddForm(request, response);
        } else if ("/edit".equals(pathInfo)) {
            showEditForm(request, response);
        } else if ("/delete".equals(pathInfo)) {
            deleteUser(request, response);
        } else if ("/search".equals(pathInfo)) {
            searchUsers(request, response);
        } else {
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
        }
    }
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        String pathInfo = request.getPathInfo();
        
        if ("/add".equals(pathInfo)) {
            addUser(request, response);
        } else if ("/edit".equals(pathInfo)) {
            updateUser(request, response);
        } else {
            response.sendError(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
        }
    }
    
    private void listUsers(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        List<User> users = userDAO.getAllUsers();
        request.setAttribute("users", users);
        request.getRequestDispatcher("/WEB-INF/jsp/users/list.jsp").forward(request, response);
    }
    
    private void searchUsers(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        String searchTerm = request.getParameter("q");
        List<User> users;
        
        if (searchTerm != null && !searchTerm.trim().isEmpty()) {
            users = userDAO.searchUsers(searchTerm.trim());
        } else {
            users = userDAO.getAllUsers();
        }
        
        request.setAttribute("users", users);
        request.setAttribute("searchTerm", searchTerm);
        request.getRequestDispatcher("/WEB-INF/jsp/users/list.jsp").forward(request, response);
    }
    
    private void showAddForm(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        request.getRequestDispatcher("/WEB-INF/jsp/users/add.jsp").forward(request, response);
    }
    
    private void addUser(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        try {
            User user = createUserFromRequest(request);
            
            // Check if username already exists
            if (userDAO.isUsernameExists(user.getUsername())) {
                request.setAttribute("error", "Username already exists");
                request.setAttribute("user", user);
                request.getRequestDispatcher("/WEB-INF/jsp/users/add.jsp").forward(request, response);
                return;
            }
            
            // Check if email already exists
            if (userDAO.isEmailExists(user.getEmail())) {
                request.setAttribute("error", "Email already exists");
                request.setAttribute("user", user);
                request.getRequestDispatcher("/WEB-INF/jsp/users/add.jsp").forward(request, response);
                return;
            }
            
            if (userDAO.addUser(user)) {
                response.sendRedirect(request.getContextPath() + "/users?success=added");
            } else {
                request.setAttribute("error", "Failed to add user");
                request.setAttribute("user", user);
                request.getRequestDispatcher("/WEB-INF/jsp/users/add.jsp").forward(request, response);
            }
        } catch (Exception e) {
            request.setAttribute("error", "Invalid input: " + e.getMessage());
            request.getRequestDispatcher("/WEB-INF/jsp/users/add.jsp").forward(request, response);
        }
    }
    
    private void showEditForm(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        String idParam = request.getParameter("id");
        if (idParam == null) {
            response.sendRedirect(request.getContextPath() + "/users");
            return;
        }
        
        try {
            int id = Integer.parseInt(idParam);
            User user = userDAO.getUserById(id);
            
            if (user == null) {
                response.sendRedirect(request.getContextPath() + "/users?error=notfound");
                return;
            }
            
            request.setAttribute("user", user);
            request.getRequestDispatcher("/WEB-INF/jsp/users/edit.jsp").forward(request, response);
        } catch (NumberFormatException e) {
            response.sendRedirect(request.getContextPath() + "/users?error=invalid");
        }
    }
    
    private void updateUser(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        try {
            String idParam = request.getParameter("id");
            int id = Integer.parseInt(idParam);
            
            User user = createUserFromRequest(request);
            user.setId(id);
            
            if (userDAO.updateUser(user)) {
                response.sendRedirect(request.getContextPath() + "/users?success=updated");
            } else {
                request.setAttribute("error", "Failed to update user");
                request.setAttribute("user", user);
                request.getRequestDispatcher("/WEB-INF/jsp/users/edit.jsp").forward(request, response);
            }
        } catch (Exception e) {
            request.setAttribute("error", "Invalid input: " + e.getMessage());
            request.getRequestDispatcher("/WEB-INF/jsp/users/edit.jsp").forward(request, response);
        }
    }
    
    private void deleteUser(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        String idParam = request.getParameter("id");
        if (idParam == null) {
            response.sendRedirect(request.getContextPath() + "/users");
            return;
        }
        
        try {
            int id = Integer.parseInt(idParam);
            
            if (userDAO.deleteUser(id)) {
                response.sendRedirect(request.getContextPath() + "/users?success=deleted");
            } else {
                response.sendRedirect(request.getContextPath() + "/users?error=deletefailed");
            }
        } catch (NumberFormatException e) {
            response.sendRedirect(request.getContextPath() + "/users?error=invalid");
        }
    }
    
    private User createUserFromRequest(HttpServletRequest request) {
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        String firstName = request.getParameter("firstName");
        String lastName = request.getParameter("lastName");
        String email = request.getParameter("email");
        String phone = request.getParameter("phone");
        String address = request.getParameter("address");
        String role = request.getParameter("role");
        
        return new User(username, password, firstName, lastName, email, phone, address, role);
    }
}
