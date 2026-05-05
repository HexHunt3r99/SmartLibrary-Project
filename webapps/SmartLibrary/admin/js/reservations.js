document.addEventListener('DOMContentLoaded', function() {
    fetch('/SmartLibrary/AdminStats')
    .then(res => res.json())
    .then(data => {
        if (!data.success) {
            window.location.href = '/SmartLibrary/Logout';
            return;
        }
        document.getElementById('activeReservations').textContent = data.active_reservations;
        document.getElementById('readyPickup').textContent = data.ready_pickup;
        document.getElementById('pendingApproval').textContent = data.pending_approval;
        document.getElementById('completedThisMonth').textContent = data.completed_reservations;
    })
    .catch(() => window.location.href = '/SmartLibrary/Logout');
    
    loadReservations();
});

function loadReservations() {
    const status = document.getElementById('statusFilter').value;
    fetch('/SmartLibrary/AdminReservations' + (status ? '?status=' + status : ''))
    .then(res => res.json())
    .then(reservations => {
        const tbody = document.getElementById('reservationsTableBody');
        tbody.innerHTML = '';
        
        if (reservations.length === 0) {
            tbody.innerHTML = '<tr><td colspan="6" style="text-align:center;padding:40px;color:#6B7280;">No reservations found</td></tr>';
            return;
        }
        
        reservations.forEach(res => {
            const initials = res.full_name.split(' ').map(n => n[0]).join('').substring(0, 2).toUpperCase();
            const reservedDate = new Date(res.reservation_date).toLocaleDateString('en-US', { month: 'short', day: 'numeric', year: 'numeric' });
            const pickupDate = res.expiration_date ? new Date(res.expiration_date).toLocaleDateString('en-US', { month: 'short', day: 'numeric', year: 'numeric' }) : '-';
            
            let statusClass = 'status-pending';
            let statusText = 'Pending';
            if (res.status === 'active') { statusClass = 'status-active'; statusText = 'Active'; }
            else if (res.status === 'ready') { statusClass = 'status-ready'; statusText = 'Ready'; }
            else if (res.status === 'completed') { statusClass = 'status-completed'; statusText = 'Completed'; }
            else if (res.status === 'cancelled') { statusClass = 'status-cancelled'; statusText = 'Cancelled'; }
            
            const row = document.createElement('tr');
            row.innerHTML = `
                <td>
                    <div class="user-cell">
                        <div class="user-cell-img">${initials}</div>
                        <div class="user-cell-info">
                            <h4>${res.full_name}</h4>
                            <p>${res.username}</p>
                        </div>
                    </div>
                </td>
                <td>
                    <div class="book-cell">
                        <div class="book-cover" style="background: linear-gradient(135deg, #4A90D9, #2C5282);">
                            <i class="fas fa-book"></i>
                        </div>
                        <div class="book-info">
                            <h4>${res.title}</h4>
                        </div>
                    </div>
                </td>
                <td>${reservedDate}</td>
                <td>${pickupDate}</td>
                <td><span class="status-badge ${statusClass}">${statusText}</span></td>
                <td>
                    <button class="action-btn view" onclick="viewReservation(${res.reservation_id})"><i class="fas fa-eye"></i></button>
                    ${res.status === 'pending' ? '<button class="action-btn approve" onclick="approveReservation(' + res.reservation_id + ')"><i class="fas fa-check"></i></button>' : ''}
                    ${res.status === 'pending' || res.status === 'active' ? '<button class="action-btn cancel" onclick="cancelReservation(' + res.reservation_id + ')"><i class="fas fa-times"></i></button>' : ''}
                </td>
            `;
            tbody.appendChild(row);
        });
    })
    .catch(err => console.error('Error loading reservations:', err));
}

function viewReservation(reservationId) {
    fetch('/SmartLibrary/AdminReservations')
    .then(res => res.json())
    .then(reservations => {
        const res = reservations.find(r => r.reservation_id === reservationId);
        if (res) {
            alert('Reservation Details\n\n' +
                'User: ' + res.full_name + '\n' +
                'Book: ' + res.title + '\n' +
                'Reserved: ' + new Date(res.reservation_date).toLocaleString() + '\n' +
                'Pickup by: ' + (res.expiration_date ? new Date(res.expiration_date).toLocaleDateString() : '-') + '\n' +
                'Status: ' + res.status + '\n' +
                'Queue Position: ' + res.queue_position);
        }
    });
}

function approveReservation(reservationId) {
    if (!confirm('Approve this reservation?')) return;
    
    fetch('/SmartLibrary/AdminReservations', {
        method: 'POST',
        headers: { 'Content-Type': 'application/x-www-form-urlencoded' },
        body: 'action=approve&reservation_id=' + reservationId
    })
    .then(res => res.json())
    .then(data => {
        if (data.success) {
            loadReservations();
            fetch('/SmartLibrary/AdminStats').then(res => res.json()).then(data => {
                document.getElementById('activeReservations').textContent = data.active_reservations;
                document.getElementById('readyPickup').textContent = data.ready_pickup;
                document.getElementById('pendingApproval').textContent = data.pending_approval;
                document.getElementById('completedThisMonth').textContent = data.completed_reservations;
            });
        } else {
            alert(data.message || 'Error approving reservation');
        }
    })
    .catch(err => alert('Error'));
}

function cancelReservation(reservationId) {
    if (!confirm('Cancel this reservation?')) return;
    
    fetch('/SmartLibrary/AdminReservations', {
        method: 'POST',
        headers: { 'Content-Type': 'application/x-www-form-urlencoded' },
        body: 'action=cancel&reservation_id=' + reservationId
    })
    .then(res => res.json())
    .then(data => {
        if (data.success) {
            loadReservations();
            fetch('/SmartLibrary/AdminStats').then(res => res.json()).then(data => {
                document.getElementById('activeReservations').textContent = data.active_reservations;
                document.getElementById('readyPickup').textContent = data.ready_pickup;
                document.getElementById('pendingApproval').textContent = data.pending_approval;
                document.getElementById('completedThisMonth').textContent = data.completed_reservations;
            });
        } else {
            alert(data.message || 'Error cancelling reservation');
        }
    })
    .catch(err => alert('Error'));
}

function searchData() {
    const term = document.getElementById('searchInput').value.toLowerCase();
    const rows = document.querySelectorAll('tbody tr');
    rows.forEach(row => {
        const text = row.textContent.toLowerCase();
        row.style.display = text.includes(term) ? '' : 'none';
    });
}
