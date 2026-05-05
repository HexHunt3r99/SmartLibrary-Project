<!DOCTYPE html>
<html lang="en">

<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Manage Borrows - SmartLibrary Admin</title>
    <link href="https://fonts.googleapis.com/css2?family=Inter:wght@300;400;500;600;700&display=swap" rel="stylesheet">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
    <link rel="stylesheet" href="css/borrows.css">
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
                <a href="books.jsp" class="nav-link">
                    <i class="fas fa-book"></i>
                    Books
                </a>
            </li>
            <li class="nav-item">
                <a href="#" class="nav-link active">
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
        <header class="header">
            <h1 class="header-title">Manage Borrows</h1>

            <div class="header-right">
                <div class="search-box">
                    <i class="fas fa-search"></i>
                    <input type="text" id="searchInput" placeholder="Search borrows..." onkeyup="searchData()">
                </div>
            </div>
        </header>

        <div class="dashboard">
            <div class="stats-grid">
                <div class="stat-card">
                    <div class="stat-header">
                        <div class="stat-icon blue">
                            <i class="fas fa-handshake"></i>
                        </div>
                    </div>
                    <div class="stat-value" id="totalBorrows">0</div>
                    <div class="stat-label">Total Borrows This Month</div>
                </div>
                <div class="stat-card">
                    <div class="stat-header">
                        <div class="stat-icon green">
                            <i class="fas fa-check-circle"></i>
                        </div>
                    </div>
                    <div class="stat-value" id="returnedCount">0</div>
                    <div class="stat-label">Returned</div>
                </div>
                <div class="stat-card">
                    <div class="stat-header">
                        <div class="stat-icon orange">
                            <i class="fas fa-clock"></i>
                        </div>
                    </div>
                    <div class="stat-value" id="activeBorrows">0</div>
                    <div class="stat-label">Active Borrows</div>
                </div>
                <div class="stat-card">
                    <div class="stat-header">
                        <div class="stat-icon red">
                            <i class="fas fa-exclamation-circle"></i>
                        </div>
                    </div>
                    <div class="stat-value" id="overdueCount">0</div>
                    <div class="stat-label">Overdue</div>
                </div>
            </div>

            <div class="card">
                <div class="card-header">
                    <h2 class="card-title">All Borrows</h2>
                    <div class="filter-bar">
                        <div class="filter-group">
                            <span class="filter-label">Status:</span>
                            <select class="filter-select" id="statusFilter" onchange="applyBorrowFilters()">
                                <option value="">All Status</option>
                                <option value="borrowed">Active</option>
                                <option value="returned">Returned</option>
                                <option value="overdue">Overdue</option>
                            </select>
                        </div>
                    </div>
                </div>
                <div class="table-container">
                    <table>
                        <thead>
                            <tr>
                                <th>User</th>
                                <th>Title</th>
                                <th>Borrowed Date</th>
                                <th>Due Date</th>
                                <th>Returned Date</th>
                                <th>Status</th>
                                <th>Actions</th>
                            </tr>
                        </thead>
                        <tbody id="borrowsTableBody">
                        </tbody>
                    </table>
                </div>
            </div>
        </div>
    </main>
    <script src="js/borrows.js"></script>
</body>

</html>

