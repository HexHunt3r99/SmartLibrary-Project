document.getElementById('loginForm').addEventListener('submit', function(e) {
    var username = document.querySelector('input[name="username"]').value;
    var password = document.querySelector('input[name="password"]').value;
    
    if (username.trim() === '' || password.trim() === '') {
        alert('Please enter username and password');
        e.preventDefault();
    }
});

var params = new URLSearchParams(window.location.search);
if (params.get('error') === '1') {
    alert('Login failed! Wrong username or password');
}
