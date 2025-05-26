
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Books - Library Management System</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css" rel="stylesheet">
    <link href="https://cdn.datatables.net/1.11.5/css/dataTables.bootstrap5.min.css" rel="stylesheet">
</head>
<body>
    <nav class="navbar navbar-expand-lg navbar-dark bg-dark">
        <div class="container">
            <a class="navbar-brand" href="${pageContext.request.contextPath}/">
                <i class="fas fa-book"></i> Library Management
            </a>
            <div class="navbar-nav ms-auto">
                <span class="navbar-text me-3">Welcome, ${sessionScope.user.firstName}!</span>
                <a class="nav-link" href="${pageContext.request.contextPath}/logout">Logout</a>
            </div>
        </div>
    </nav>

    <div class="container mt-4">
        <div class="row">
            <div class="col-12">
                <div class="d-flex justify-content-between align-items-center mb-4">
                    <h2><i class="fas fa-book"></i> Books Management</h2>
                    <c:if test="${sessionScope.userRole == 'ADMIN'}">
                        <a href="${pageContext.request.contextPath}/books/add" class="btn btn-primary">
                            <i class="fas fa-plus"></i> Add New Book
                        </a>
                    </c:if>
                </div>

                <!-- Search Form -->
                <div class="card mb-4">
                    <div class="card-body">
                        <form method="get" action="${pageContext.request.contextPath}/books/search" class="row g-3">
                            <div class="col-md-10">
                                <input type="text" class="form-control" name="q" value="${searchTerm}" 
                                       placeholder="Search by title, author, or ISBN...">
                            </div>
                            <div class="col-md-2">
                                <button type="submit" class="btn btn-outline-primary w-100">
                                    <i class="fas fa-search"></i> Search
                                </button>
                            </div>
                        </form>
                    </div>
                </div>

                <!-- Success/Error Messages -->
                <c:if test="${param.success == 'added'}">
                    <div class="alert alert-success alert-dismissible fade show">
                        <i class="fas fa-check-circle"></i> Book added successfully!
                        <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
                    </div>
                </c:if>
                <c:if test="${param.success == 'updated'}">
                    <div class="alert alert-success alert-dismissible fade show">
                        <i class="fas fa-check-circle"></i> Book updated successfully!
                        <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
                    </div>
                </c:if>
                <c:if test="${param.success == 'deleted'}">
                    <div class="alert alert-success alert-dismissible fade show">
                        <i class="fas fa-check-circle"></i> Book deleted successfully!
                        <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
                    </div>
                </c:if>

                <!-- Books Table -->
                <div class="card">
                    <div class="card-body">
                        <table id="booksTable" class="table table-striped table-hover">
                            <thead class="table-dark">
                                <tr>
                                    <th>ISBN</th>
                                    <th>Title</th>
                                    <th>Author</th>
                                    <th>Category</th>
                                    <th>Copies</th>
                                    <th>Available</th>
                                    <th>Status</th>
                                    <th>Actions</th>
                                </tr>
                            </thead>
                            <tbody>
                                <c:forEach var="book" items="${books}">
                                    <tr>
                                        <td>${book.isbn}</td>
                                        <td>
                                            <strong>${book.title}</strong>
                                            <c:if test="${not empty book.publisher}">
                                                <br><small class="text-muted">${book.publisher}</small>
                                            </c:if>
                                        </td>
                                        <td>${book.author}</td>
                                        <td>
                                            <span class="badge bg-secondary">${book.category}</span>
                                        </td>
                                        <td class="text-center">${book.totalCopies}</td>
                                        <td class="text-center">
                                            <span class="badge ${book.availableCopies > 0 ? 'bg-success' : 'bg-danger'}">
                                                ${book.availableCopies}
                                            </span>
                                        </td>
                                        <td>
                                            <span class="badge ${book.status == 'AVAILABLE' ? 'bg-success' : 'bg-warning'}">
                                                ${book.status}
                                            </span>
                                        </td>
                                        <td>
                                            <div class="btn-group btn-group-sm">
                                                <button type="button" class="btn btn-info" data-bs-toggle="modal" 
                                                        data-bs-target="#viewModal${book.id}">
                                                    <i class="fas fa-eye"></i>
                                                </button>
                                                <c:if test="${sessionScope.userRole == 'ADMIN'}">
                                                    <a href="${pageContext.request.contextPath}/books/edit?id=${book.id}" 
                                                       class="btn btn-warning">
                                                        <i class="fas fa-edit"></i>
                                                    </a>
                                                    <button type="button" class="btn btn-danger" 
                                                            onclick="confirmDelete(${book.id}, '${book.title}')">
                                                        <i class="fas fa-trash"></i>
                                                    </button>
                                                </c:if>
                                            </div>
                                        </td>
                                    </tr>

                                    <!-- View Modal -->
                                    <div class="modal fade" id="viewModal${book.id}" tabindex="-1">
                                        <div class="modal-dialog modal-lg">
                                            <div class="modal-content">
                                                <div class="modal-header">
                                                    <h5 class="modal-title">Book Details</h5>
                                                    <button type="button" class="btn-close" data-bs-dismiss="modal"></button>
                                                </div>
                                                <div class="modal-body">
                                                    <div class="row">
                                                        <div class="col-md-6">
                                                            <p><strong>ISBN:</strong> ${book.isbn}</p>
                                                            <p><strong>Title:</strong> ${book.title}</p>
                                                            <p><strong>Author:</strong> ${book.author}</p>
                                                            <p><strong>Publisher:</strong> ${book.publisher}</p>
                                                        </div>
                                                        <div class="col-md-6">
                                                            <p><strong>Category:</strong> ${book.category}</p>
                                                            <p><strong>Publish Date:</strong> 
                                                                <fmt:formatDate value="${book.publishDate}" pattern="yyyy-MM-dd"/>
                                                            </p>
                                                            <p><strong>Total Copies:</strong> ${book.totalCopies}</p>
                                                            <p><strong>Available:</strong> ${book.availableCopies}</p>
                                                        </div>
                                                    </div>
                                                    <c:if test="${not empty book.description}">
                                                        <p><strong>Description:</strong></p>
                                                        <p class="text-muted">${book.description}</p>
                                                    </c:if>
                                                </div>
                                            </div>
                                        </div>
                                    </div>
                                </c:forEach>
                            </tbody>
                        </table>
                    </div>
                </div>
            </div>
        </div>
    </div>

    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/js/bootstrap.bundle.min.js"></script>
    <script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
    <script src="https://cdn.datatables.net/1.11.5/js/jquery.dataTables.min.js"></script>
    <script src="https://cdn.datatables.net/1.11.5/js/dataTables.bootstrap5.min.js"></script>
    
    <script>
        $(document).ready(function() {
            $('#booksTable').DataTable({
                "pageLength": 10,
                "order": [[1, "asc"]],
                "columnDefs": [
                    { "orderable": false, "targets": 7 }
                ]
            });
        });
        
        function confirmDelete(bookId, bookTitle) {
            if (confirm('Are you sure you want to delete "' + bookTitle + '"?')) {
                window.location.href = '${pageContext.request.contextPath}/books/delete?id=' + bookId;
            }
        }
    </script>
</body>
</html>
