window.allUsers = [];

function checkLogin() {
    fetch('/SmartLibrary/AdminStats')
    .then(response => response.json())
    .then(data => {
        if (!data.success) {
            window.location.href = '/SmartLibrary/Logout';
        } else {
            document.getElementById('totalStudents').textContent = data.total_students;
            document.getElementById('totalFaculty').textContent = data.total_faculty;
            document.getElementById('totalLibrarians').textContent = data.total_librarians;
            document.getElementById('totalAdmins').textContent = data.total_admins;
            loadUsers();
        }
    })
    .catch(err => {
        window.location.href = '/SmartLibrary/Logout';
    });
}

function loadUsers() {
    fetch('/SmartLibrary/UserServlet')
    .then(response => response.json())
    .then(data => {
        window.allUsers = data;
        applyUserFilters();
        updateStats(data);
    });
}

function displayUsers(users) {
    const tbody = document.getElementById('userTable');
    tbody.innerHTML = '';
    
    if (users.length === 0) {
        tbody.innerHTML = '<tr><td colspan="5" style="text-align: center; padding: 40px; color: #64748B;">No users found</td></tr>';
        return;
    }
    
    users.forEach(user => {
        let roleClass = 'role-student';
        if (user.role === 'admin') roleClass = 'role-admin';
        else if (user.role === 'faculty') roleClass = 'role-faculty';
        else if (user.role === 'librarian') roleClass = 'role-librarian';
        
        let roleDisplay = user.role || 'student';
        
        let statusClass = user.status === 'active' ? 'status-active' : 'status-inactive';
        
        const initials = user.username.substring(0, 2).toUpperCase();
        
        const row = '<tr>' +
            '<td><div class="user-cell">' +
            '<div class="user-cell-img">' + initials + '</div>' +
            '<div class="user-cell-info"><h4>' + user.full_name + '</h4><p>' + user.username + '</p></div></div></td>' +
            '<td>' + user.email + '</td>' +
            '<td><span class="role-badge ' + roleClass + '">' + roleDisplay + '</span></td>' +
            '<td><span class="status-badge ' + statusClass + '">' + (user.status || 'active') + '</span></td>' +
            '<td>' +
            '<button class="action-btn edit"><i class="fas fa-edit"></i></button>' +
            '<button class="action-btn delete" onclick="deleteUser(' + user.user_id + ')"><i class="fas fa-trash"></i></button>' +
            '</td></tr>';
        tbody.innerHTML += row;
    });
}

function applyUserFilters() {
    const search = document.getElementById('searchInput').value.toLowerCase();
    const role = document.getElementById('roleFilter').value;
    const status = document.getElementById('statusFilter').value;

    const filteredUsers = (window.allUsers || []).filter(user => {
        const matchesSearch =
            user.username.toLowerCase().includes(search) ||
            user.email.toLowerCase().includes(search) ||
            user.full_name.toLowerCase().includes(search);
        const matchesRole = !role || (user.role || '').toLowerCase() === role;
        const matchesStatus = !status || (user.status || '').toLowerCase() === status;

        return matchesSearch && matchesRole && matchesStatus;
    });

    displayUsers(filteredUsers);
}

function updateStats(users) {
    let students = users.filter(u => u.role === 'student').length;
    let faculty = users.filter(u => u.role === 'faculty').length;
    let librarians = users.filter(u => u.role === 'librarian').length;
    let admins = users.filter(u => u.role === 'admin').length;
    
    document.getElementById('totalStudents').textContent = students;
    document.getElementById('totalFaculty').textContent = faculty;
    document.getElementById('totalLibrarians').textContent = librarians;
    document.getElementById('totalAdmins').textContent = admins;
}

function deleteUser(userId) {
    if (confirm('Are you sure you want to delete this user?')) {
        let formData = new URLSearchParams();
        formData.append('action', 'delete');
        formData.append('id', userId);
        
        fetch('/SmartLibrary/UserServlet', {
            method: 'POST',
            body: formData
        })
        .then(response => response.json())
        .then(data => {
            loadUsers();
        });
    }
}

document.getElementById('searchInput').addEventListener('input', applyUserFilters);
document.getElementById('roleFilter').addEventListener('change', applyUserFilters);
document.getElementById('statusFilter').addEventListener('change', applyUserFilters);

document.addEventListener('DOMContentLoaded', checkLogin);
