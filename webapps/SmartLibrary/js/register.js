document.getElementById('registerForm').addEventListener('submit', function(e) {
    var username = document.querySelector('input[name="username"]').value;
    var email = document.querySelector('input[name="email"]').value;
    var password = document.querySelector('input[name="password"]').value;
    var confirmPassword = document.querySelectorAll('input[name="password"]')[1];
    
    if (username.trim() === '' || email.trim() === '' || password.trim() === '') {
        alert('Please fill all fields');
        e.preventDefault();
        return;
    }
    
    if (password.length < 6) {
        alert('Password must be at least 6 characters');
        e.preventDefault();
        return;
    }
    
    if (confirmPassword && password !== confirmPassword.value) {
        alert('Passwords do not match');
        e.preventDefault();
    }
});

var params = new URLSearchParams(window.location.search);
if (params.get('error') === '1') {
    alert('Registration failed! Please try again');
}
