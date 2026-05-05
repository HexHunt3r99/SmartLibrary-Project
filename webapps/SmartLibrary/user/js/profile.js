function initProfile() {
    fetch('/SmartLibrary/UserStats')
    .then(res => res.json())
    .then(data => {
        if (!data.success) window.location.href = '/SmartLibrary/Logout';
    })
    .catch(() => window.location.href = '/SmartLibrary/Logout');

    fetch('/SmartLibrary/ProfileServlet')
    .then(res => res.json())
    .then(data => {
        if (data.success) {
            document.getElementById('fullName').value = data.full_name || '';
            document.getElementById('email').value = data.email || '';
            document.getElementById('username').value = data.username || '';
            document.getElementById('role').value = data.role || '';
        }
    });
}

function saveProfile() {
    let formData = new URLSearchParams();
    formData.append('action', 'updateProfile');
    formData.append('full_name', document.getElementById('fullName').value);
    formData.append('email', document.getElementById('email').value);
    
    fetch('/SmartLibrary/ProfileServlet', {
        method: 'POST',
        body: formData
    })
    .then(res => res.json())
    .then(data => {
        if (data.success) location.reload();
    });
}

function showPasswordModal() {
    document.getElementById('passwordModal').style.display = 'block';
}

function closePasswordModal() {
    document.getElementById('passwordModal').style.display = 'none';
}

function changePassword() {
    let current = document.getElementById('currentPassword').value;
    let newPass = document.getElementById('newPassword').value;
    let confirm = document.getElementById('confirmPassword').value;
    
    if (newPass !== confirm || newPass.length < 6) {
        return;
    }
    
    let formData = new URLSearchParams();
    formData.append('action', 'changePassword');
    formData.append('current_password', current);
    formData.append('new_password', newPass);
    
    fetch('/SmartLibrary/ProfileServlet', {
        method: 'POST',
        body: formData
    })
    .then(res => res.json())
    .then(data => {
        if (data.success) {
            closePasswordModal();
            document.getElementById('currentPassword').value = '';
            document.getElementById('newPassword').value = '';
            document.getElementById('confirmPassword').value = '';
        }
    });
}

document.addEventListener('DOMContentLoaded', initProfile);
