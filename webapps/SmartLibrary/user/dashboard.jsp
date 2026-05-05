<!DOCTYPE html>
<html lang="en">

<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>SmartLibrary - Dashboard</title>
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
                    Dashboard
                </a>
            </li>
            <li class="nav-item">
                <a href="books.jsp" class="nav-link">
                    <i class="fas fa-search"></i>
                    Browse Library
                </a>
            </li>
            <li class="nav-item">
                <a href="borrows.jsp" class="nav-link">
                    <i class="fas fa-book"></i>
                    My Books
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
                    My Fines
                </a>
            </li>
            <li class="nav-item">
                <a href="profile.jsp" class="nav-link">
                    <i class="fas fa-user"></i>
                    Profile Settings
                </a>
            </li>
            <li class="nav-item">
                <a href="help.jsp" class="nav-link">
                    <i class="fas fa-question-circle"></i>
                    <span>Help</span>
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
            <h1 class="header-title" id="welcomeMsg">Welcome back!</h1>
        </header>

        <div class="dashboard">
            <div class="stats-grid">
                <div class="stat-card">
                    <div class="stat-header">
                        <div class="stat-icon blue">
                            <i class="fas fa-book"></i>
                        </div>
                    </div>
                    <div class="stat-value" id="borrowedBooks"></div>
                    <div class="stat-label">Currently Borrowed</div>
                </div>

                <div class="stat-card">
                    <div class="stat-header">
                        <div class="stat-icon green">
                            <i class="fas fa-calendar-check"></i>
                        </div>
                    </div>
                    <div class="stat-value" id="reservations"></div>
                    <div class="stat-label">Reservations</div>
                </div>

                <div class="stat-card">
                    <div class="stat-header">
                        <div class="stat-icon orange">
                            <i class="fas fa-exclamation-circle"></i>
                        </div>
                    </div>
                    <div class="stat-value" id="unpaidFines"></div>
                    <div class="stat-label">Unpaid Fines</div>
                </div>
            </div>

            <div class="content-grid">
                <div class="recent-activity">
                    <div class="card-header">
                        <h2 class="card-title">Recent Activity</h2>
                    </div>
                    <div class="activity-list" id="recentActivity">
                    </div>
                </div>
            </div>
        </div>
    </main>
    <script src="js/dashboard.js"></script>
</body>

</html>

