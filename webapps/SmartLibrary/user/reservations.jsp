<!DOCTYPE html>
<html lang="en">

<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Reservations - SmartLibrary</title>
    <link href="https://fonts.googleapis.com/css2?family=Inter:wght@300;400;500;600;700&display=swap" rel="stylesheet">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
    <link rel="stylesheet" href="css/reservations.css">
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
                    <span>Dashboard</span>
                </a>
            </li>
            <li class="nav-item">
                <a href="books.jsp" class="nav-link">
                    <i class="fas fa-search"></i>
                    <span>Browse Library</span>
                </a>
            </li>
            <li class="nav-item">
                <a href="borrows.jsp" class="nav-link">
                    <i class="fas fa-book"></i>
                    <span>My Books</span>
                </a>
            </li>
            <li class="nav-item">
                <a href="reservations.jsp" class="nav-link active">
                    <i class="fas fa-calendar-check"></i>
                    <span>Reservations</span>
                </a>
            </li>
            <li class="nav-item">
                <a href="fines.jsp" class="nav-link">
                    <i class="fas fa-wallet"></i>
                    <span>My Fines</span>
                </a>
            </li>
            <li class="nav-item">
                <a href="profile.jsp" class="nav-link">
                    <i class="fas fa-cog"></i>
                    <span>Profile Settings</span>
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
                <span>Logout</span>
            </a>
        </div>
    </aside>

    <main class="main-content">
        <header class="header">
            <h1 class="header-title">Your Active Reservations</h1>
        </header>

        <div class="dashboard">
            <div class="stats-grid">
                <div class="stat-card">
                    <div class="stat-header">
                        <div class="stat-icon blue">
                            <i class="fas fa-calendar-check"></i>
                        </div>
                    </div>
                    <div class="stat-value" id="activeReservations"></div>
                    <div class="stat-label">Active Reservations</div>
                </div>
                <div class="stat-card">
                    <div class="stat-header">
                        <div class="stat-icon green">
                            <i class="fas fa-check-circle"></i>
                        </div>
                    </div>
                    <div class="stat-value" id="readyPickup"></div>
                    <div class="stat-label">Ready for Pickup</div>
                </div>
                <div class="stat-card">
                    <div class="stat-header">
                        <div class="stat-icon orange">
                            <i class="fas fa-clock"></i>
                        </div>
                    </div>
                    <div class="stat-value" id="inQueue"></div>
                    <div class="stat-label">In Queue</div>
                </div>
            </div>

            <div class="card">
                <div class="card-header">
                    <h2 class="card-title">All Reservations</h2>
                </div>
                <div class="table-container">
                    <table>
                        <thead>
                            <tr>
                                <th>Title</th>
                                <th>Queue Position</th>
                                <th>Reserved Date</th>
                                <th>Status</th>
                                <th>Action</th>
                            </tr>
                        </thead>
                        <tbody id="reservationsTable">
                        </tbody>
                    </table>
                </div>
            </div>
        </div>
    </main>
    <script>
        fetch('/SmartLibrary/UserStats')
        .then(res => res.json())
        .then(data => {
            if (!data.success) {
                window.location.href = '/SmartLibrary/Logout';
                return;
            }
            document.getElementById('activeReservations').textContent = data.active_reservations;
            document.getElementById('readyPickup').textContent = data.ready_pickup;
            document.getElementById('inQueue').textContent = data.in_queue;
        })
        .catch(() => window.location.href = '/SmartLibrary/Logout');
    </script>
    <script src="js/reservations.js"></script>
</body>

</html>


