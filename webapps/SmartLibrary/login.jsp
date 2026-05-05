<!DOCTYPE html>
<html lang="en">

<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Login - SmartLibrary</title>
    <link href="https://fonts.googleapis.com/css2?family=Inter:wght@300;400;500;600;700&display=swap" rel="stylesheet">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
    <link rel="stylesheet" href="css/login.css">
</head>

<body>
    <a href="index.jsp" class="back-home"><i class="fas fa-arrow-left"></i> Back to Home</a>
    <div class="auth-wrapper">
        <div class="auth-card">
            <div class="brand">
                <div class="brand-icon"><i class="fas fa-book-open"></i></div>
                <h1>Welcome Back</h1>
                <p>Login to continue to SmartLibrary</p>
            </div>
            <form id="loginForm" action="LoginServlet" method="post">
                <div class="form-group">
                    <label>Username</label>
                    <input type="text" name="username" placeholder="Enter your username" required>
                </div>
                <div class="form-group">
                    <label>Password</label>
                    <input type="password" name="password" placeholder="Enter your password" required>
                </div>
                <div class="forgot-password">
                    <a href="#">Forgot password?</a>
                </div>
                <button type="submit" class="btn-primary">Login</button>
            </form>
            <p class="signup-link">Don't have an account? <a href="register.jsp">Create one</a></p>
        </div>
    </div>
</body>

</html>