<!DOCTYPE html>
<html lang="en">

<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>My Profile - SmartLibrary</title>
    <link href="https://fonts.googleapis.com/css2?family=Inter:wght@300;400;500;600;700&display=swap" rel="stylesheet">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
    <link rel="stylesheet" href="css/profile.css">
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
                <a href="profile.jsp" class="nav-link active">
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
            <h1 class="header-title">My Profile</h1>
        </header>

        <div class="dashboard">

            <div class="profile-grid">
                <div class="profile-content">
                    <div class="settings-section">
                        <div class="card">
                            <div class="card-header">
                                <h2 class="card-title">Personal Information</h2>
                            </div>
                            <div class="card-body">
                                <div class="form-row">
                                    <div class="form-group">
                                        <label class="form-label">Full Name</label>
                                        <input type="text" id="fullName" class="form-input" value="">
                                    </div>
                                    <div class="form-group">
                                        <label class="form-label">Username</label>
                                        <input type="text" id="username" class="form-input" value="" disabled>
                                    </div>
                                </div>
                                <div class="form-row">
                                    <div class="form-group">
                                        <label class="form-label">Role</label>
                                        <input type="text" id="role" class="form-input" value="" disabled>
                                    </div>
                                    <div class="form-group">
                                        <label class="form-label">Email Address</label>
                                        <input type="email" id="email" class="form-input" value="">
                                    </div>
                                </div>
                                <div class="form-actions">
                                    <button class="btn-save-changes" onclick="saveProfile()">Save Changes</button>
                                </div>
                            </div>
                        </div>

                        <div class="card">
                            <div class="card-header">
                                <h2 class="card-title">Account Security</h2>
                            </div>
                            <div class="card-body">
                                <div class="security-item">
                                    <div class="security-info">
                                        <div class="security-icon">
                                            <i class="fas fa-lock"></i>
                                        </div>
                                        <div>
                                            <h4>Change Password</h4>
                                            <p>Update your account password</p>
                                        </div>
                                    </div>
                                    <button class="btn-change-password" onclick="showPasswordModal()">
                                        <i class="fas fa-key"></i> Change Password
                                    </button>
                                </div>
                            </div>
                        </div>

                    </div>
                </div>
            </div>
        </div>
    </main>

    <div id="passwordModal" class="modal" style="display:none;">
        <div class="modal-content">
            <span class="close" onclick="closePasswordModal()">&times;</span>
            <h2>Change Password</h2>
            <div class="form-group">
                <label class="form-label">New Password</label>
                <input type="password" id="newPassword" class="form-input">
            </div>
            <div class="form-group">
                <label class="form-label">Confirm New Password</label>
                <input type="password" id="confirmPassword" class="form-input">
            </div>
            <div class="form-actions">
                <button class="btn-save-changes" onclick="changePassword()">Change Password</button>
            </div>
        </div>
    </div>

    <script src="js/profile.js"></script>
</body>

</html>

