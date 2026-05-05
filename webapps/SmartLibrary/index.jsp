<!DOCTYPE html>
<html lang="en">

<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>SmartLibrary - Smart Library Management</title>
    <link href="https://fonts.googleapis.com/css2?family=Inter:wght@300;400;500;600;700;800&display=swap"
        rel="stylesheet">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
    <link rel="stylesheet" href="css/index.css">
</head>

<body class="landing-page">
    <nav class="navbar">
        <a href="index.jsp" class="nav-brand">
            <div class="nav-brand-icon"><i class="fas fa-book-open"></i></div>
            <span class="nav-brand-text">SmartLibrary</span>
        </a>
        <ul class="nav-menu">
            <li><a href="#home">Home</a></li>
            <li><a href="#features">Features</a></li>
            <li><a href="#how-it-works">How It Works</a></li>
            <li><a href="#contact">Contact</a></li>
        </ul>
        <div class="nav-buttons">
            <a href="login.jsp" class="btn btn-outline">Login</a>
            <a href="register.jsp" class="btn btn-primary">Register</a>
        </div>
    </nav>

    <section class="hero" id="home">
        <div class="hero-badge"><i class="fas fa-sparkles"></i> Modern Library Solution</div>
        <h1>SmartLibrary - Manage Books <span>the Smart Way</span></h1>
        <p>Streamline your library operations with our intuitive platform. Easy borrowing, tracking, and management for
            modern libraries.</p>
        <div class="hero-buttons">
            <a href="login.jsp" class="btn btn-primary"><i class="fas fa-sign-in-alt"></i> Login</a>
            <a href="register.jsp" class="btn btn-outline"><i class="fas fa-user-plus"></i> Register</a>
        </div>
        <div class="hero-stats">
            <div>
                <div class="hero-stat-value">10,000+</div>
                <div class="hero-stat-label">Books</div>
            </div>
            <div>
                <div class="hero-stat-value">5,000+</div>
                <div class="hero-stat-label">Users</div>
            </div>
            <div>
                <div class="hero-stat-value">50,000+</div>
                <div class="hero-stat-label">Active Borrows</div>
            </div>
        </div>
    </section>

    <section class="features" id="features">
        <div class="features-header">
            <span class="section-badge">Features</span>
            <h2 class="section-title">Everything You Need</h2>
        </div>
        <div class="feature-list">
            <div class="feature-item">
                <div class="feature-icon blue"><i class="fas fa-search"></i></div>
                <div class="feature-text">
                    <h3>Easy Book Search</h3>
                    <p>Find any book instantly with our powerful search engine.</p>
                </div>
            </div>
            <div class="feature-item">
                <div class="feature-icon green"><i class="fas fa-clock"></i></div>
                <div class="feature-text">
                    <h3>Due Date Tracking</h3>
                    <p>Get timely reminders and automatic fine calculations.</p>
                </div>
            </div>
            <div class="feature-item">
                <div class="feature-icon purple"><i class="fas fa-chart-pie"></i></div>
                <div class="feature-text">
                    <h3>Admin Reports</h3>
                    <p>Comprehensive analytics for data-driven decisions.</p>
                </div>
            </div>
        </div>
    </section>

    <section class="how-it-works" id="how-it-works">
        <div class="features-header">
            <span class="section-badge">How It Works</span>
            <h2 class="section-title">Get Started in 3 Steps</h2>
        </div>
        <div class="steps-list">
            <div class="step-item">
                <div class="step-num">1</div>
                <div class="step-text">
                    <h3>Register</h3>
                    <p>Create your account with university email.</p>
                </div>
            </div>
            <div class="step-item">
                <div class="step-num">2</div>
                <div class="step-text">
                    <h3>Borrow Books</h3>
                    <p>Browse and borrow books with few clicks.</p>
                </div>
            </div>
            <div class="step-item">
                <div class="step-num">3</div>
                <div class="step-text">
                    <h3>Return & Track</h3>
                    <p>Return on time and track your journey.</p>
                </div>
            </div>
        </div>
    </section>

    <section class="cta">
        <h2 class="cta-title">Ready to Get Started?</h2>
        <p class="cta-desc">Join thousands using SmartLibrary</p>
        <div class="cta-buttons">
            <a href="register.jsp" class="btn btn-white"><i class="fas fa-user-plus"></i> Create Account</a>
            <a href="#contact" class="btn btn-outline-light"><i class="fas fa-envelope"></i> Contact</a>
        </div>
    </section>

    <footer class="footer" id="contact">
        <div class="footer-content">
            <div class="footer-brand">
                <a href="index.jsp" class="nav-brand">
                    <div class="nav-brand-icon"><i class="fas fa-book-open"></i></div>
                    <span class="nav-brand-text">SmartLibrary</span>
                </a>
                <p>Modern library management system for educational institutions.</p>
                <div class="footer-social">
                    <a href="#"><i class="fab fa-facebook-f"></i></a>
                    <a href="#"><i class="fab fa-twitter"></i></a>
                    <a href="#"><i class="fab fa-instagram"></i></a>
                </div>
            </div>
            <div class="footer-col">
                <h4>Contact</h4>
                <ul class="footer-links">
                    <li><a href="mailto:cesabt.office@gmail.com">cesabt.office@gmail.com</a></li>
                    <li><a href="#">+91 20 27658596</a></li>
                    <li><a href="#">Sector No. 27-A, Pradhikaran Nigdi, Pune - 411 044</a></li>
                </ul>
            </div>
        </div>
        <div class="footer-bottom">
            <p>&copy; 2026 SmartLibrary. All rights reserved.</p>
        </div>
    </footer>

    </script>
</body>

</html>