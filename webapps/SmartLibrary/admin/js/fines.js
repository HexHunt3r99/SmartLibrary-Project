document.addEventListener('DOMContentLoaded', function() {
    fetch('/SmartLibrary/AdminStats')
    .then(res => res.json())
    .then(data => {
        if (!data.success) {
            window.location.href = '/SmartLibrary/Logout';
            return;
        }
        document.getElementById('totalFines').innerHTML = '₹' + data.total_fines;
        document.getElementById('pendingFines').innerHTML = '₹' + data.pending_fines;
        document.getElementById('collectedFines').innerHTML = '₹' + data.collected_fines;
        document.getElementById('usersWithFines').textContent = data.users_with_fines;
    })
    .catch(() => window.location.href = '/SmartLibrary/Logout');
    
    loadFines();
});

function loadFines() {
    const status = document.getElementById('statusFilter').value;
    fetch('/SmartLibrary/AdminFines' + (status ? '?status=' + status : ''))
    .then(res => res.json())
    .then(fines => {
        const tbody = document.getElementById('finesTableBody');
        tbody.innerHTML = '';
        
        if (fines.length === 0) {
            tbody.innerHTML = '<tr><td colspan="7" style="text-align:center;padding:40px;color:#6B7280;">No fines found</td></tr>';
            return;
        }
        
        fines.forEach(fine => {
            const initials = fine.full_name.split(' ').map(n => n[0]).join('').substring(0, 2).toUpperCase();
            const date = new Date(fine.created_at).toLocaleDateString('en-US', { month: 'short', day: 'numeric', year: 'numeric' });
            const statusClass = fine.status === 'paid' ? 'status-paid' : 'status-unpaid';
            const statusText = fine.status === 'paid' ? 'Paid' : 'Unpaid';
            
            const row = document.createElement('tr');
            row.innerHTML = `
                <td>
                    <div class="user-cell">
                        <div class="user-cell-img">${initials}</div>
                        <div class="user-cell-info">
                            <h4>${fine.full_name}</h4>
                            <p>${fine.username}</p>
                        </div>
                    </div>
                </td>
                <td>
                    <div class="book-cell">
                        <div class="book-cover" style="background: linear-gradient(135deg, #4A90D9, #2C5282);">
                            <i class="fas fa-book"></i>
                        </div>
                        <div class="book-info">
                            <h4>${fine.title}</h4>
                        </div>
                    </div>
                </td>
                <td>${fine.reason || '-'}</td>
                <td><span class="amount negative">₹${fine.amount}</span></td>
                <td>${date}</td>
                <td><span class="status-badge ${statusClass}">${statusText}</span></td>
                <td>
                    <button class="action-btn view" onclick="viewFine(${fine.fine_id})"><i class="fas fa-eye"></i></button>
                    ${fine.status === 'unpaid' ? '<button class="action-btn pay" onclick="payFine(' + fine.fine_id + ')"><i class="fas fa-credit-card"></i></button>' : ''}
                </td>
            `;
            tbody.appendChild(row);
        });
    })
    .catch(err => console.error('Error loading fines:', err));
}

function payFine(fineId) {
    if (!confirm('Mark this fine as paid?')) return;
    
    fetch('/SmartLibrary/FineServlet', {
        method: 'POST',
        headers: { 'Content-Type': 'application/x-www-form-urlencoded' },
        body: 'action=pay&fine_id=' + fineId
    })
    .then(res => res.json())
    .then(data => {
        if (data.success) {
            loadFines();
            fetch('/SmartLibrary/AdminStats').then(res => res.json()).then(data => {
                document.getElementById('totalFines').innerHTML = '₹' + data.total_fines;
                document.getElementById('pendingFines').innerHTML = '₹' + data.pending_fines;
                document.getElementById('collectedFines').innerHTML = '₹' + data.collected_fines;
                document.getElementById('usersWithFines').textContent = data.users_with_fines;
            });
        } else {
            alert(data.message || 'Error paying fine');
        }
    })
    .catch(err => alert('Error paying fine'));
}

function viewFine(fineId) {
    fetch('/SmartLibrary/AdminFines')
    .then(res => res.json())
    .then(fines => {
        const fine = fines.find(f => f.fine_id === fineId);
        if (fine) {
            alert('Fine Details\n\n' +
                'User: ' + fine.full_name + '\n' +
                'Book: ' + fine.title + '\n' +
                'Amount: ₹' + fine.amount + '\n' +
                'Reason: ' + (fine.reason || '-') + '\n' +
                'Status: ' + fine.status + '\n' +
                'Date: ' + new Date(fine.created_at).toLocaleString());
        }
    });
}

function searchData() {
    const term = document.getElementById('searchInput').value.toLowerCase();
    const rows = document.querySelectorAll('tbody tr');
    rows.forEach(row => {
        const text = row.textContent.toLowerCase();
        row.style.display = text.includes(term) ? '' : 'none';
    });
}
