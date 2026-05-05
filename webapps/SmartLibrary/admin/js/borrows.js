window.allBorrows = [];

document.addEventListener('DOMContentLoaded', function() {
    fetch('/SmartLibrary/AdminStats')
    .then(res => res.json())
    .then(data => {
        if (!data.success) {
            window.location.href = '/SmartLibrary/Logout';
            return;
        }
        document.getElementById('totalBorrows').textContent = data.total_borrows;
        document.getElementById('returnedCount').textContent = data.returned_borrows;
        document.getElementById('activeBorrows').textContent = data.active_borrows;
        document.getElementById('overdueCount').textContent = data.overdue_borrows;
    })
    .catch(() => window.location.href = '/SmartLibrary/Logout');
    
    loadBorrows();
});

function loadBorrows() {
    fetch('/SmartLibrary/AdminBorrows')
    .then(res => res.json())
    .then(borrows => {
        window.allBorrows = borrows;
        applyBorrowFilters();
    })
    .catch(err => console.error('Error loading borrows:', err));
}

function applyBorrowFilters() {
    const status = document.getElementById('statusFilter').value;
    const searchTerm = document.getElementById('searchInput').value.toLowerCase();
    const tbody = document.getElementById('borrowsTableBody');
    tbody.innerHTML = '';

    if (!window.allBorrows || window.allBorrows.length === 0) {
        tbody.innerHTML = '<tr><td colspan="7" style="text-align:center;padding:40px;color:#6B7280;">No borrows found</td></tr>';
        return;
    }

    const filteredBorrows = window.allBorrows.filter(borrow => {
        const matchesStatus = !status || borrow.status === status;
        const matchesSearch =
            borrow.full_name.toLowerCase().includes(searchTerm) ||
            borrow.username.toLowerCase().includes(searchTerm) ||
            borrow.title.toLowerCase().includes(searchTerm) ||
            borrow.status.toLowerCase().includes(searchTerm);

        return matchesStatus && matchesSearch;
    });

    if (filteredBorrows.length === 0) {
        tbody.innerHTML = '<tr><td colspan="7" style="text-align:center;padding:40px;color:#6B7280;">No borrows match your filters</td></tr>';
        return;
    }

    filteredBorrows.forEach(borrow => {
        const initials = borrow.full_name.split(' ').map(n => n[0]).join('').substring(0, 2).toUpperCase();
        const borrowDate = new Date(borrow.borrow_date).toLocaleDateString('en-US', { month: 'short', day: 'numeric', year: 'numeric' });
        const dueDate = new Date(borrow.due_date).toLocaleDateString('en-US', { month: 'short', day: 'numeric', year: 'numeric' });
        const returnDate = borrow.return_date ? new Date(borrow.return_date).toLocaleDateString('en-US', { month: 'short', day: 'numeric', year: 'numeric' }) : '-';

        let statusClass = 'status-active';
        let statusText = 'Active';
        if (borrow.status === 'returned') { statusClass = 'status-returned'; statusText = 'Returned'; }
        else if (borrow.status === 'overdue') { statusClass = 'status-overdue'; statusText = 'Overdue'; }

        const row = document.createElement('tr');
        row.innerHTML = `
            <td>
                <div class="user-cell">
                    <div class="user-cell-img">${initials}</div>
                    <div class="user-cell-info">
                        <h4>${borrow.full_name}</h4>
                        <p>${borrow.username}</p>
                    </div>
                </div>
            </td>
            <td>
                <div class="book-cell">
                    <div class="book-cover" style="background: linear-gradient(135deg, #4A90D9, #2C5282);">
                        <i class="fas fa-book"></i>
                    </div>
                    <div class="book-info">
                        <h4>${borrow.title}</h4>
                    </div>
                </div>
            </td>
            <td>${borrowDate}</td>
            <td>${dueDate}</td>
            <td>${returnDate}</td>
            <td><span class="status-badge ${statusClass}">${statusText}</span></td>
            <td>
                <button class="action-btn view" onclick="viewBorrow(${borrow.borrow_id})"><i class="fas fa-eye"></i></button>
                ${borrow.status === 'borrowed' || borrow.status === 'overdue' ? '<button class="action-btn return" onclick="returnBook(' + borrow.borrow_id + ')"><i class="fas fa-check"></i></button>' : ''}
            </td>
        `;
        tbody.appendChild(row);
    });
}

function viewBorrow(borrowId) {
    const borrow = (window.allBorrows || []).find(b => b.borrow_id === borrowId);
    if (borrow) {
        alert('Borrow Details\n\n' +
            'User: ' + borrow.full_name + '\n' +
            'Book: ' + borrow.title + '\n' +
            'Borrowed: ' + new Date(borrow.borrow_date).toLocaleString() + '\n' +
            'Due: ' + new Date(borrow.due_date).toLocaleDateString() + '\n' +
            'Returned: ' + (borrow.return_date ? new Date(borrow.return_date).toLocaleDateString() : 'Not returned') + '\n' +
            'Status: ' + borrow.status);
    }
}

function returnBook(borrowId) {
    if (!confirm('Mark this book as returned?')) return;
    
    fetch('/SmartLibrary/AdminBorrows', {
        method: 'POST',
        headers: { 'Content-Type': 'application/x-www-form-urlencoded' },
        body: 'action=return&borrow_id=' + borrowId
    })
    .then(res => res.json())
    .then(data => {
        if (data.success) {
            loadBorrows();
            fetch('/SmartLibrary/AdminStats').then(res => res.json()).then(data => {
                document.getElementById('totalBorrows').textContent = data.total_borrows;
                document.getElementById('returnedCount').textContent = data.returned_borrows;
                document.getElementById('activeBorrows').textContent = data.active_borrows;
                document.getElementById('overdueCount').textContent = data.overdue_borrows;
            });
        } else {
            alert(data.message || 'Error returning book');
        }
    })
    .catch(err => alert('Error'));
}

function searchData() {
    applyBorrowFilters();
}
