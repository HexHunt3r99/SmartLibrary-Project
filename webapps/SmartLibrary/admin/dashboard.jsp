<!DOCTYPE html>
<html lang="en">

<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Admin Overview & Analytics - SmartLibrary</title>
    <link href="https://fonts.googleapis.com/css2?family=Inter:wght@300;400;500;600;700&display=swap" rel="stylesheet">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
    <link rel="stylesheet" href="css/dashboard.css">
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
                <a href="dashboard.jsp" class="nav-link active">
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
            <h1 class="header-title">Dashboard Overview</h1>
            <div class="header-right" style="min-height: 1px;">
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
                    <div class="stat-value" id="totalUsers">0</div>
                    <div class="stat-label">Total Users</div>
                </div>
                <div class="stat-card">
                    <div class="stat-header">
                        <div class="stat-icon green">
                            <i class="fas fa-book"></i>
                        </div>
                    </div>
                    <div class="stat-value" id="totalBooks">0</div>
                    <div class="stat-label">Total Books</div>
                </div>
                <div class="stat-card">
                    <div class="stat-header">
                        <div class="stat-icon orange">
                            <i class="fas fa-handshake"></i>
                        </div>
                    </div>
                    <div class="stat-value" id="activeBorrows">0</div>
                    <div class="stat-label">Active Borrows</div>
                </div>
                <div class="stat-card">
                    <div class="stat-header">
                        <div class="stat-icon purple">
                            <i class="fas fa-calendar-check"></i>
                        </div>
                    </div>
                    <div class="stat-value" id="totalReservations">0</div>
                    <div class="stat-label">Reservations</div>
                </div>
            </div>

            <div class="charts-grid">
                <div class="chart-card">
                    <div class="chart-header">
                        <h2 class="chart-title">Borrowing Trends</h2>
                    </div>
                    <div class="chart-container">
                        <canvas id="borrowChart"></canvas>
                    </div>
                </div>
                <div class="chart-card">
                    <div class="chart-header">
                        <h2 class="chart-title">Category Distribution</h2>
                    </div>
                    <div class="chart-container-sm">
                        <canvas id="categoryChart"></canvas>
                    </div>
                </div>
            </div>

            <div class="recent-activity">
                <div class="chart-header">
                    <h2 class="chart-title">Recent Activity</h2>
                </div>
                <div class="activity-list" id="activityList">
                </div>
            </div>
        </div>
    </main>
    <script src="https://cdn.jsdelivr.net/npm/chart.js"></script>
    <script src="js/dashboard.js"></script>
</body>

</html>

