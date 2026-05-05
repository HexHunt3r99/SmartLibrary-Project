<!DOCTYPE html>
<html lang="en">

<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Manage Users - SmartLibrary Admin</title>
    <link href="https://fonts.googleapis.com/css2?family=Inter:wght@300;400;500;600;700&display=swap" rel="stylesheet">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
    <link rel="stylesheet" href="css/users.css">
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
                <a href="users.jsp" class="nav-link active">
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
        <header class="header">
            <h1 class="header-title">Manage Users</h1>

            <div class="header-right">
                <div class="search-box">
                    <i class="fas fa-search"></i>
                    <input type="text" id="searchInput" placeholder="Search users...">
                </div>
            </div>
        </header>

        <div class="dashboard">
            <div class="stats-grid">
                <div class="stat-card">
                    <div class="stat-header">
                        <div class="stat-icon blue">
                            <i class="fas fa-users"></i>
                        </div>
                    </div>
                    <div class="stat-value" id="totalStudents">0</div>
                    <div class="stat-label">Students</div>
                </div>
                <div class="stat-card">
                    <div class="stat-header">
                        <div class="stat-icon green">
                            <i class="fas fa-chalkboard-teacher"></i>
                        </div>
                    </div>
                    <div class="stat-value" id="totalFaculty">0</div>
                    <div class="stat-label">Faculty</div>
                </div>
                <div class="stat-card">
                    <div class="stat-header">
                        <div class="stat-icon orange">
                            <i class="fas fa-user-shield"></i>
                        </div>
                    </div>
                    <div class="stat-value" id="totalLibrarians">0</div>
                    <div class="stat-label">Librarians</div>
                </div>
                <div class="stat-card">
                    <div class="stat-header">
                        <div class="stat-icon purple">
                            <i class="fas fa-user-cog"></i>
                        </div>
                    </div>
                    <div class="stat-value" id="totalAdmins">0</div>
                    <div class="stat-label">Admins</div>
                </div>
            </div>

            <div class="card">
                <div class="card-header">
                    <h2 class="card-title">All Users</h2>
                    <div class="filter-bar">
                        <div class="filter-group">
                            <span class="filter-label">Role:</span>
                            <select class="filter-select" id="roleFilter">
                                <option value="">All Roles</option>
                                <option value="student">Student</option>
                                <option value="faculty">Faculty</option>
                                <option value="librarian">Librarian</option>
                                <option value="admin">Admin</option>
                            </select>
                        </div>
                        <div class="filter-group">
                            <span class="filter-label">Status:</span>
                            <select class="filter-select" id="statusFilter">
                                <option value="">All Status</option>
                                <option value="active">Active</option>
                                <option value="inactive">Inactive</option>
                            </select>
                        </div>
                    </div>
                </div>
                <div class="table-container">
                    <table>
                        <thead>
                            <tr>
                                <th>User</th>
                                <th>Email</th>
                                <th>Role</th>
                                <th>Status</th>
                                <th>Actions</th>
                            </tr>
                        </thead>
                        <tbody id="userTable">
                        </tbody>
                    </table>
                </div>
            </div>
        </div>
    </main>
    
    <script src="js/users.js"></script>
</body>

</html>

