
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Add Book - Library Management System</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css" rel="stylesheet">
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
        <div class="row justify-content-center">
            <div class="col-md-8">
                <div class="card">
                    <div class="card-header">
                        <h4><i class="fas fa-plus"></i> Add New Book</h4>
                    </div>
                    <div class="card-body">
                        <c:if test="${not empty error}">
                            <div class="alert alert-danger">
                                <i class="fas fa-exclamation-triangle"></i> ${error}
                            </div>
                        </c:if>

                        <form method="post" action="${pageContext.request.contextPath}/books/add">
                            <div class="row">
                                <div class="col-md-6">
                                    <div class="mb-3">
                                        <label for="isbn" class="form-label">ISBN *</label>
                                        <input type="text" class="form-control" id="isbn" name="isbn" 
                                               value="${book.isbn}" required>
                                    </div>
                                </div>
                                <div class="col-md-6">
                                    <div class="mb-3">
                                        <label for="category" class="form-label">Category</label>
                                        <select class="form-select" id="category" name="category">
                                            <option value="">Select Category</option>
                                            <option value="Fiction" ${book.category == 'Fiction' ? 'selected' : ''}>Fiction</option>
                                            <option value="Non-Fiction" ${book.category == 'Non-Fiction' ? 'selected' : ''}>Non-Fiction</option>
                                            <option value="Science" ${book.category == 'Science' ? 'selected' : ''}>Science</option>
                                            <option value="Technology" ${book.category == 'Technology' ? 'selected' : ''}>Technology</option>
                                            <option value="History" ${book.category == 'History' ? 'selected' : ''}>History</option>
                                            <option value="Biography" ${book.category == 'Biography' ? 'selected' : ''}>Biography</option>
                                            <option value="Education" ${book.category == 'Education' ? 'selected' : ''}>Education</option>
                                            <option value="Reference" ${book.category == 'Reference' ? 'selected' : ''}>Reference</option>
                                        </select>
                                    </div>
                                </div>
                            </div>

                            <div class="mb-3">
                                <label for="title" class="form-label">Title *</label>
                                <input type="text" class="form-control" id="title" name="title" 
                                       value="${book.title}" required>
                            </div>

                            <div class="mb-3">
                                <label for="author" class="form-label">Author *</label>
                                <input type="text" class="form-control" id="author" name="author" 
                                       value="${book.author}" required>
                            </div>

                            <div class="row">
                                <div class="col-md-6">
                                    <div class="mb-3">
                                        <label for="publisher" class="form-label">Publisher</label>
                                        <input type="text" class="form-control" id="publisher" name="publisher" 
                                               value="${book.publisher}">
                                    </div>
                                </div>
                                <div class="col-md-6">
                                    <div class="mb-3">
                                        <label for="publishDate" class="form-label">Publish Date</label>
                                        <input type="date" class="form-control" id="publishDate" name="publishDate" 
                                               value="${book.publishDate}">
                                    </div>
                                </div>
                            </div>

                            <div class="mb-3">
                                <label for="totalCopies" class="form-label">Total Copies *</label>
                                <input type="number" class="form-control" id="totalCopies" name="totalCopies" 
                                       value="${book.totalCopies != null ? book.totalCopies : 1}" min="1" required>
                            </div>

                            <div class="mb-3">
                                <label for="description" class="form-label">Description</label>
                                <textarea class="form-control" id="description" name="description" 
                                          rows="3">${book.description}</textarea>
                            </div>

                            <div class="d-flex justify-content-between">
                                <a href="${pageContext.request.contextPath}/books" class="btn btn-secondary">
                                    <i class="fas fa-arrow-left"></i> Back to Books
                                </a>
                                <button type="submit" class="btn btn-primary">
                                    <i class="fas fa-save"></i> Add Book
                                </button>
                            </div>
                        </form>
                    </div>
                </div>
            </div>
        </div>
    </div>

    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
