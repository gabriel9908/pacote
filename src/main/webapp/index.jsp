
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Library Management System</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css" rel="stylesheet">
    <style>
        .hero-section {
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
            color: white;
            padding: 100px 0;
        }
        .feature-card {
            transition: transform 0.3s;
        }
        .feature-card:hover {
            transform: translateY(-5px);
        }
    </style>
</head>
<body>
    <!-- Navigation -->
    <nav class="navbar navbar-expand-lg navbar-dark bg-dark">
        <div class="container">
            <a class="navbar-brand" href="${pageContext.request.contextPath}/">
                <i class="fas fa-book"></i> Library Management System
            </a>
            <div class="navbar-nav ms-auto">
                <c:choose>
                    <c:when test="${sessionScope.user != null}">
                        <span class="navbar-text me-3">Welcome, ${sessionScope.user.firstName}!</span>
                        <a class="nav-link" href="${pageContext.request.contextPath}/logout">Logout</a>
                    </c:when>
                    <c:otherwise>
                        <a class="nav-link" href="${pageContext.request.contextPath}/login">Login</a>
                    </c:otherwise>
                </c:choose>
            </div>
        </div>
    </nav>

    <!-- Hero Section -->
    <section class="hero-section text-center">
        <div class="container">
            <h1 class="display-4 mb-4">Welcome to Our Library</h1>
            <p class="lead mb-4">Discover, learn, and explore our vast collection of books and resources</p>
            <c:choose>
                <c:when test="${sessionScope.user != null}">
                    <c:choose>
                        <c:when test="${sessionScope.userRole == 'ADMIN'}">
                            <a href="${pageContext.request.contextPath}/admin/dashboard" class="btn btn-light btn-lg">
                                <i class="fas fa-tachometer-alt"></i> Admin Dashboard
                            </a>
                        </c:when>
                        <c:otherwise>
                            <a href="${pageContext.request.contextPath}/user/dashboard" class="btn btn-light btn-lg">
                                <i class="fas fa-user"></i> My Dashboard
                            </a>
                        </c:otherwise>
                    </c:choose>
                </c:when>
                <c:otherwise>
                    <a href="${pageContext.request.contextPath}/login" class="btn btn-light btn-lg">
                        <i class="fas fa-sign-in-alt"></i> Get Started
                    </a>
                </c:otherwise>
            </c:choose>
        </div>
    </section>

    <!-- Features Section -->
    <section class="py-5">
        <div class="container">
            <div class="row text-center mb-5">
                <div class="col">
                    <h2>Library Features</h2>
                    <p class="text-muted">Everything you need for efficient library management</p>
                </div>
            </div>
            <div class="row g-4">
                <div class="col-md-4">
                    <div class="card feature-card h-100 text-center border-0 shadow">
                        <div class="card-body">
                            <div class="text-primary mb-3">
                                <i class="fas fa-book-open fa-3x"></i>
                            </div>
                            <h5 class="card-title">Book Management</h5>
                            <p class="card-text">Add, edit, and manage your book collection with ease. Search and categorize books efficiently.</p>
                        </div>
                    </div>
                </div>
                <div class="col-md-4">
                    <div class="card feature-card h-100 text-center border-0 shadow">
                        <div class="card-body">
                            <div class="text-success mb-3">
                                <i class="fas fa-users fa-3x"></i>
                            </div>
                            <h5 class="card-title">User Management</h5>
                            <p class="card-text">Manage library members, track their borrowing history, and handle user registrations.</p>
                        </div>
                    </div>
                </div>
                <div class="col-md-4">
                    <div class="card feature-card h-100 text-center border-0 shadow">
                        <div class="card-body">
                            <div class="text-warning mb-3">
                                <i class="fas fa-exchange-alt fa-3x"></i>
                            </div>
                            <h5 class="card-title">Loan System</h5>
                            <p class="card-text">Track book loans, returns, and manage overdue items with automatic fine calculations.</p>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </section>

    <!-- Statistics Section -->
    <section class="py-5 bg-light">
        <div class="container">
            <div class="row text-center">
                <div class="col-md-3">
                    <div class="card border-0 bg-transparent">
                        <div class="card-body">
                            <i class="fas fa-book text-primary fa-2x mb-3"></i>
                            <h3 class="text-primary">10,000+</h3>
                            <p class="text-muted">Books Available</p>
                        </div>
                    </div>
                </div>
                <div class="col-md-3">
                    <div class="card border-0 bg-transparent">
                        <div class="card-body">
                            <i class="fas fa-users text-success fa-2x mb-3"></i>
                            <h3 class="text-success">5,000+</h3>
                            <p class="text-muted">Active Members</p>
                        </div>
                    </div>
                </div>
                <div class="col-md-3">
                    <div class="card border-0 bg-transparent">
                        <div class="card-body">
                            <i class="fas fa-calendar text-warning fa-2x mb-3"></i>
                            <h3 class="text-warning">24/7</h3>
                            <p class="text-muted">Online Access</p>
                        </div>
                    </div>
                </div>
                <div class="col-md-3">
                    <div class="card border-0 bg-transparent">
                        <div class="card-body">
                            <i class="fas fa-star text-danger fa-2x mb-3"></i>
                            <h3 class="text-danger">4.9/5</h3>
                            <p class="text-muted">User Rating</p>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </section>

    <!-- Footer -->
    <footer class="bg-dark text-white py-4">
        <div class="container">
            <div class="row">
                <div class="col-md-6">
                    <h5>Library Management System</h5>
                    <p>Your digital library solution for modern management.</p>
                </div>
                <div class="col-md-6 text-md-end">
                    <p>&copy; 2024 Library Management System. All rights reserved.</p>
                </div>
            </div>
        </div>
    </footer>

    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
