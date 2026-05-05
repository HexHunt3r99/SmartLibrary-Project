<!DOCTYPE html>
<html lang="en">

<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Browse Library - SmartLibrary</title>
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
                    Dashboard
                </a>
            </li>
            <li class="nav-item">
                <a href="books.jsp" class="nav-link active">
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
            <h1 class="header-title">Browse Library</h1>
        </header>


        <div class="dashboard">

            <div class="search-hero">
                <div class="search-hero-box">
                    <input type="text" id="searchInput" class="search-hero-input" placeholder="Search by title or author..." onkeyup="searchBooks()">
                    <button class="search-hero-btn" onclick="searchBooks()">
                        <i class="fas fa-search"></i> Search
                    </button>
                </div>
            </div>

            <div class="card">
                <div class="card-header">
                    <h2 class="card-title">Search Results</h2>
                    <span class="results-count"></span>
                </div>
                <div class="table-container">
                    <table>
                        <thead>
                            <tr>
                                <th>Title</th>
                                <th>Publisher</th>
                                <th>Category</th>
                                <th>Status</th>
                                <th>Action</th>
                            </tr>
                        </thead>
                        <tbody id="booksTableBody">
                        </tbody>
                    </table>
                </div>
            </div>
        </div>
    </main>
    <script>
        const resultsCount = document.querySelector('.results-count');

        fetch('/SmartLibrary/UserStats')
        .then(res => res.json())
        .then(data => {
            if (!data.success) window.location.href = '/SmartLibrary/Logout';
        })
        .catch(() => window.location.href = '/SmartLibrary/Logout');
    </script>
    <script src="js/books.js"></script>
</body>

</html>


