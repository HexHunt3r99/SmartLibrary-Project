<!DOCTYPE html>
<html lang="en">

<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Register - SmartLibrary</title>
    <link href="https://fonts.googleapis.com/css2?family=Inter:wght@300;400;500;600;700&display=swap" rel="stylesheet">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
    <link rel="stylesheet" href="css/register.css">
</head>

<body>
    <a href="index.jsp" class="back-home"><i class="fas fa-arrow-left"></i> Back to Home</a>
    <div class="auth-wrapper">
        <div class="auth-card">
            <div class="brand">
                <div class="brand-icon"><i class="fas fa-book-open"></i></div>
                <h1>Create Account</h1>
                <p>Join SmartLibrary today</p>
            </div>
            <form id="registerForm" action="RegisterServlet" method="post">
                <div class="form-group">
                    <label>Full Name</label>
                    <input type="text" name="full_name" placeholder="Enter your full name" required>
                </div>
                <div class="form-group">
                    <label>Username</label>
                    <input type="text" name="username" placeholder="Enter your username" required>
                </div>
                <div class="form-group">
                    <label>Email Address</label>
                    <input type="email" name="email" placeholder="Enter your email" required>
                </div>
                <div class="form-group">
                    <label>Password</label>
                    <input type="password" name="password" placeholder="Create a password" required>
                </div>
                <div class="form-group">
                    <label>Confirm Password</label>
                    <input type="password" placeholder="Confirm your password" required>
                </div>
                <div class="terms">
                    <input type="checkbox" id="terms" required>
                    <label for="terms">I agree to the <a href="#">Terms of Service</a> and <a href="#">Privacy
                            Policy</a></label>
                </div>
                <button type="submit" class="btn-primary">Create Account</button>
            </form>
            <p class="signup-link">Already have an account? <a href="login.jsp">Login</a></p>
        </div>
    </div>
</body>

</html>