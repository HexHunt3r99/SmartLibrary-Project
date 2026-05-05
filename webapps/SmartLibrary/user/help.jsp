<!DOCTYPE html>
<html lang="en">

<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Help & Support - SmartLibrary</title>
    <link href="https://fonts.googleapis.com/css2?family=Inter:wght@300;400;500;600;700&display=swap" rel="stylesheet">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
    <link rel="stylesheet" href="css/help.css">
    <script src="js/help.js"></script>
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
                <a href="profile.jsp" class="nav-link">
                    <i class="fas fa-user"></i>
                    Profile Settings
                </a>
            </li>
            <li class="nav-item">
                <a href="help.jsp" class="nav-link active">
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
            <h1 class="header-title">Help & Support</h1>
        </header>

        <div class="dashboard">

            <div class="help-grid">
                <div class="help-card">
                    <div class="help-icon blue">
                        <i class="fas fa-book"></i>
                    </div>
                    <h3>Borrowing Books</h3>
                    <p>How to borrow, renew, and return books</p>
                </div>
                <div class="help-card">
                    <div class="help-icon green">
                        <i class="fas fa-user"></i>
                    </div>
                    <h3>Account & Profile</h3>
                    <p>Manage your account settings</p>
                </div>
                <div class="help-card">
                    <div class="help-icon orange">
                        <i class="fas fa-wallet"></i>
                    </div>
                    <h3>Fines & Payments</h3>
                    <p>Understand fines and make payments</p>
                </div>
                <div class="help-card">
                    <div class="help-icon purple">
                        <i class="fas fa-calendar-check"></i>
                    </div>
                    <h3>Reservations</h3>
                    <p>How to reserve and pick up books</p>
                </div>
                <div class="help-card">
                    <div class="help-icon red">
                        <i class="fas fa-search"></i>
                    </div>
                    <h3>Finding Books</h3>
                    <p>Search and locate books in library</p>
                </div>
                <div class="help-card">
                    <div class="help-icon teal">
                        <i class="fas fa-envelope"></i>
                    </div>
                    <h3>Notifications</h3>
                    <p>Manage email and alert preferences</p>
                </div>
            </div>

            <div class="faq-section">
                <h2 class="section-title"><i class="fas fa-question-circle"></i> Frequently Asked Questions</h2>
                <div class="faq-list">
                    <div class="faq-item">
                        <div class="faq-question" onclick="this.parentElement.classList.toggle('active')">
                            <h4>How many books can I borrow at a time?</h4>
                            <i class="fas fa-chevron-down"></i>
                        </div>
                        <div class="faq-answer">
                            <p>Students can borrow up to 5 books at a time. Faculty members can borrow up to 10 books.
                                The borrowing period is 14 days for students and 30 days for faculty.</p>
                        </div>
                    </div>
                    <div class="faq-item">
                        <div class="faq-question" onclick="this.parentElement.classList.toggle('active')">
                            <h4>How do I renew my borrowed books?</h4>
                            <i class="fas fa-chevron-down"></i>
                        </div>
                        <div class="faq-answer">
                            <p>You can renew books through your dashboard by clicking the "Renew" button next to the
                                borrowed book. Books can be renewed up to 2 times. If there are holds on the book,
                                renewal may not be allowed.</p>
                        </div>
                    </div>
                    <div class="faq-item">
                        <div class="faq-question" onclick="this.parentElement.classList.toggle('active')">
                            <h4>What happens if I return a book late?</h4>
                            <i class="fas fa-chevron-down"></i>
                        </div>
                        <div class="faq-answer">
                            <p>Late returns incur a fine of 10 per day. After 30 days of overdue, borrowing privileges
                                may be suspended until all fines are paid.</p>
                        </div>
                    </div>
                    <div class="faq-item">
                        <div class="faq-question" onclick="this.parentElement.classList.toggle('active')">
                            <h4>How do I reserve a book that is currently unavailable?</h4>
                            <i class="fas fa-chevron-down"></i>
                        </div>
                        <div class="faq-answer">
                            <p>Navigate to the book's page and click the "Reserve" button. You will be notified when the
                                book becomes available. Reservations are held for 3 days once available.</p>
                        </div>
                    </div>
                    <div class="faq-item">
                        <div class="faq-question" onclick="this.parentElement.classList.toggle('active')">
                            <h4>How can I pay my library fines?</h4>
                            <i class="fas fa-chevron-down"></i>
                        </div>
                        <div class="faq-answer">
                            <p>You can pay fines through the "My Fines" page in your dashboard. We accept credit/debit
                                cards and UPI payments. Fines can also be paid at the library counter.</p>
                        </div>
                    </div>
                    <div class="faq-item">
                        <div class="faq-question" onclick="this.parentElement.classList.toggle('active')">
                            <h4>Can I extend my borrowing period?</h4>
                            <i class="fas fa-chevron-down"></i>
                        </div>
                        <div class="faq-answer">
                            <p>Yes, you can extend your borrowing period by renewing the book. Each book can be renewed
                                up to 2 times. If you need a longer extension, please contact the library staff.</p>
                        </div>
                    </div>
                </div>
            </div>

            <div class="contact-section">
                <h2 class="section-title"><i class="fas fa-headset"></i> Contact Us</h2>
                <p style="color: var(--text-gray);">Still have questions? Reach out to us through any of these channels.
                </p>
                <div class="contact-grid">
                    <div class="contact-item">
                        <div class="contact-icon blue">
                            <i class="fas fa-envelope"></i>
                        </div>
                        <div>
                            <h4>Email Support</h4>
                            <p>cesabt.office@gmail.com</p>
                        </div>
                    </div>
                    <div class="contact-item">
                        <div class="contact-icon green">
                            <i class="fas fa-phone"></i>
                        </div>
                        <div>
                            <h4>Phone</h4>
                            <p>+91 20 27658596</p>
                        </div>
                    </div>
                    <div class="contact-item">
                        <div class="contact-icon orange">
                            <i class="fas fa-map-marker-alt"></i>
                        </div>
                        <div>
                            <h4>Library Location</h4>
                            <p>Sector No. 27-A, Pradhikaran Nigdi, Pune - 411 044</p>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </main>
</body>

</html>

