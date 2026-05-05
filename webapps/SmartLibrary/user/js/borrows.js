function initBorrows() {
    fetch('/SmartLibrary/UserStats')
    .then(res => res.json())
    .then(data => {
        if (!data.success) {
            window.location.href = '/SmartLibrary/Logout';
            return;
        }
        document.getElementById('currentlyBorrowed').textContent = data.borrowed_books;
        document.getElementById('pendingRenewal').textContent = data.pending_renewal;
        document.getElementById('overdueBooks').textContent = data.overdue;
        
        const tbody = document.getElementById('borrowsTable');
        if (data.recent_borrows && data.recent_borrows.length > 0) {
            let html = '';
            data.recent_borrows.forEach(borrow => {
                let statusBadge = '';
                let actionBtn = '';
                
                if (borrow.status === 'returned') {
                    statusBadge = '<span class="status-badge status-returned">' + borrow.status + '</span>';
                    actionBtn = '';
                } else if (borrow.status === 'overdue') {
                    statusBadge = '<span class="status-badge status-overdue">' + borrow.status + '</span>';
                    actionBtn = '<button class="btn-action btn-renew" onclick="renewBook(' + borrow.id + ')"><i class="fas fa-sync-alt"></i> Renew</button>';
                } else {
                    statusBadge = '<span class="status-badge status-borrowed">' + borrow.status + '</span>';
                    actionBtn = '<button class="btn-action btn-renew" onclick="renewBook(' + borrow.id + ')"><i class="fas fa-sync-alt"></i> Renew</button>';
                }
                
                html += '<tr>';
                html += '<td><div class="book-info"><div class="book-thumb" style="background: linear-gradient(135deg, #4A90D9, #2C5282);"><i class="fas fa-book"></i></div><div class="book-details"><h4>' + borrow.title + '</h4><span>' + borrow.author + '</span></div></div></td>';
                html += '<td>' + borrow.borrowed_date + '</td>';
                html += '<td>' + borrow.due_date + '</td>';
                html += '<td>' + statusBadge + '</td>';
                html += '<td>' + actionBtn + '</td>';
                html += '</tr>';
            });
            tbody.innerHTML = html;
        } else {
            tbody.innerHTML = '<tr><td colspan="5" style="text-align: center; padding: 40px; color: #64748B;">No books borrowed yet - browse books to borrow</td></tr>';
        }
    })
    .catch(() => window.location.href = '/SmartLibrary/Logout');
}

function renewBook(borrowId) {
    const days = prompt('How many days do you want to extend?', '7');
    if (days === null || days === '' || isNaN(days) || parseInt(days) <= 0) {
        return;
    }
    
    fetch('/SmartLibrary/RenewBook?borrow_id=' + borrowId + '&days=' + days, {
        method: 'POST'
    })
    .then(res => res.json())
    .then(data => {
        if (data.success) {
            location.reload();
        }
    })
    .catch(err => location.reload());
}

document.addEventListener('DOMContentLoaded', initBorrows);
