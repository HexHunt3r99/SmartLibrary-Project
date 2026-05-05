<!DOCTYPE html>
<html lang="en">

<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Manage Books - SmartLibrary Admin</title>
    <link href="https://fonts.googleapis.com/css2?family=Inter:wght@300;400;500;600;700&display=swap" rel="stylesheet">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
    <link rel="stylesheet" href="css/books.css">
</head>

<body>
    <aside class="sidebar">
        <div class="logo">
            <div class="logo-icon">
                <i class="fas fa-book-open"></i>
            </div>
            <span class="logo-text">SmartLibrary</span>
        </div>

        <ul class="nav-menu">
            <li class="nav-item">
                <a href="dashboard.jsp" class="nav-link">
                    <i class="fas fa-th-large"></i>
                    Overview
                </a>
            </li>
            <li class="nav-item">
                <a href="users.jsp" class="nav-link">
                    <i class="fas fa-users"></i>
                    Users
                </a>
            </li>
            <li class="nav-item">
                <a href="#" class="nav-link active">
                    <i class="fas fa-book"></i>
                    Books
                </a>
            </li>
            <li class="nav-item">
                <a href="borrows.jsp" class="nav-link">
                    <i class="fas fa-handshake"></i>
                    Borrows
                </a>
            </li>
            <li class="nav-item">
                <a href="reservations.jsp" class="nav-link">
                    <i class="fas fa-calendar-check"></i>
                    Reservations
                </a>
            </li>
            <li class="nav-item">
                <a href="fines.jsp" class="nav-link">
                    <i class="fas fa-wallet"></i>
                    Fines
                </a>
            </li>
        </ul>

        <div class="sidebar-footer">
            <a href="/SmartLibrary/Logout" class="nav-link">
                <i class="fas fa-sign-out-alt"></i>
                Logout
            </a>
        </div>
    </aside>

    <main class="main-content">
        <div id="addBookModal" class="modal">
            <div class="modal-content">
                <span class="close" onclick="closeModal()">&times;</span>
                <h2>Add New Book</h2>
                <form id="addBookForm" autocomplete="on">
                    <div class="form-group">
                        <label>Title</label>
                        <input type="text" name="title" id="title" autocomplete="on" required>
                    </div>
                    <div class="form-group">
                        <label>Author</label>
                        <input type="text" name="author" id="author" autocomplete="on" required>
                    </div>
                    <div class="form-group">
                        <label>Publisher</label>
                        <input type="text" name="publisher" id="publisher" autocomplete="on" required>
                    </div>
                    <div class="form-group">
                        <label>Publish Year</label>
                        <input type="number" name="publish_year" id="publish_year" autocomplete="off" required>
                    </div>
                    <div class="form-group">
                        <label>Category</label>
                        <select name="category" id="category" autocomplete="off" required>
                            <option value="">Select Category</option>
                            <option value="Programming">Programming</option>
                            <option value="Science">Science</option>
                            <option value="Mathematics">Mathematics</option>
                            <option value="History">History</option>
                            <option value="Literature">Literature</option>
                        </select>
                    </div>
                    <div class="form-group">
                        <label>Quantity</label>
                        <input type="number" name="quantity" id="quantity" autocomplete="off" required>
                    </div>
                    <button type="submit" class="btn-submit">Add Book</button>
                </form>
            </div>
        </div>
        
        <div id="editBookModal" class="modal">
            <div class="modal-content">
                <span class="close" onclick="closeEditModal()">&times;</span>
                <h2>Edit Book</h2>
                <form id="editBookForm">
                    <input type="hidden" id="editBookId">
                    <div class="form-group">
                        <label>Title</label>
                        <input type="text" id="editTitle" required>
                    </div>
                    <div class="form-group">
                        <label>Author</label>
                        <input type="text" id="editAuthor" required>
                    </div>
                    <div class="form-group">
                        <label>Publisher</label>
                        <input type="text" id="editPublisher" required>
                    </div>
                    <div class="form-group">
                        <label>Publish Year</label>
                        <input type="number" id="editPublishYear" required>
                    </div>
                    <div class="form-group">
                        <label>Category</label>
                        <select id="editCategory" required>
                            <option value="">Select Category</option>
                            <option value="Programming">Programming</option>
                            <option value="Science">Science</option>
                            <option value="Mathematics">Mathematics</option>
                            <option value="History">History</option>
                            <option value="Literature">Literature</option>
                        </select>
                    </div>
                    <div class="form-group">
                        <label>Quantity</label>
                        <input type="number" id="editQuantity" required>
                    </div>
                    <button type="submit" class="btn-submit">Update Book</button>
                </form>
            </div>
        </div>

        <header class="header">
            <h1 class="header-title">Manage Books</h1>

            <div class="header-right">
                <div class="search-box">
                    <i class="fas fa-search"></i>
                    <input type="text" id="searchInput" placeholder="Search by title, author or category..." onkeyup="searchBooks()">
                </div>

                <button class="btn-add" onclick="openAddModal()">
                    <i class="fas fa-plus"></i> Add Book
                </button>

            </div>
        </header>

        <div class="dashboard">
            <div class="stats-grid">
                <div class="stat-card">
                    <div class="stat-header">
                        <div class="stat-icon blue">
                            <i class="fas fa-book"></i>
                        </div>
                    </div>
                    <div class="stat-value" id="totalBooksStat">0</div>
                    <div class="stat-label">Total Books</div>
                </div>
                <div class="stat-card">
                    <div class="stat-header">
                        <div class="stat-icon green">
                            <i class="fas fa-check-circle"></i>
                        </div>
                    </div>
                    <div class="stat-value" id="availableStat">0</div>
                    <div class="stat-label">Available</div>
                </div>
                <div class="stat-card">
                    <div class="stat-header">
                        <div class="stat-icon orange">
                            <i class="fas fa-handshake"></i>
                        </div>
                    </div>
                    <div class="stat-value" id="borrowedStat">0</div>
                    <div class="stat-label">Currently Borrowed</div>
                </div>
                <div class="stat-card">
                    <div class="stat-header">
                        <div class="stat-icon purple">
                            <i class="fas fa-calendar"></i>
                        </div>
                    </div>
                    <div class="stat-value" id="reservedStat">0</div>
                    <div class="stat-label">Reserved</div>
                </div>
            </div>

            <div class="card">
                <div class="card-header">
                    <h2 class="card-title">All Books</h2>
                    <div class="filter-bar">
                        <div class="filter-group">
                            <span class="filter-label">Category:</span>
                            <select class="filter-select" id="categoryFilter">
                                <option value="">All Categories</option>
                            </select>
                        </div>
                        <div class="filter-group">
                            <span class="filter-label">Status:</span>
                            <select class="filter-select" id="statusFilter">
                                <option value="">All Status</option>
                                <option value="Available">Available</option>
                                <option value="Borrowed">Borrowed</option>
                                <option value="Reserved">Reserved</option>
                            </select>
                        </div>
                    </div>
                </div>
                <div class="table-container">
                    <table>
                        <thead>
                            <tr>
                                <th>Title</th>
                                <th>Category</th>
                                <th>Author</th>
                                <th>Copies</th>
                                <th>Status</th>
                                <th>Actions</th>
                            </tr>
                        </thead>
                        <tbody>
                        </tbody>
                    </table>
                </div>
            </div>
        </div>
    </main>
    <script src="js/books.js"></script>
</body>

</html>

