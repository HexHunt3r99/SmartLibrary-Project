function initReservations() {
    fetch('/SmartLibrary/UserStats')
    .then(res => res.json())
    .then(data => {
        if (!data.success) {
            window.location.href = '/SmartLibrary/Logout';
            return;
        }
        document.getElementById('activeReservations').textContent = data.active_reservations;
        document.getElementById('readyPickup').textContent = data.ready_pickup;
        document.getElementById('inQueue').textContent = data.in_queue;
        
        const tbody = document.getElementById('reservationsTable');
        if (data.reservations && data.reservations.length > 0) {
            let html = '';
            data.reservations.forEach(res => {
                let statusBadge = '';
                let actionBtn = '';
                
                if (res.status === 'ready') {
                    statusBadge = '<span class="status-badge status-available">Available for Pickup</span>';
                    actionBtn = '<button class="btn-action btn-primary" onclick="borrowReservedBook(' + res.id + ')">Borrow Now</button>';
                } else if (res.status === 'active') {
                    statusBadge = '<span class="status-badge status-active">Active</span>';
                    actionBtn = '<button class="btn-action btn-danger" onclick="cancelReservation(' + res.id + ')">Cancel</button>';
                } else if (res.status === 'pending') {
                    statusBadge = '<span class="status-badge status-queue">In Queue</span>';
                    actionBtn = '<button class="btn-action btn-danger" onclick="cancelReservation(' + res.id + ')">Cancel</button>';
                } else if (res.status === 'completed') {
                    statusBadge = '<span class="status-badge status-returned">Completed</span>';
                    actionBtn = '';
                }
                
                html += '<tr>';
                html += '<td><div class="book-info"><div class="book-thumb" style="background: linear-gradient(135deg, #4A90D9, #2C5282);"><i class="fas fa-book"></i></div><div class="book-details"><h4>' + res.title + '</h4><span>' + res.author + '</span></div></div></td>';
                html += '<td><strong>#' + res.queue_position + '</strong></td>';
                html += '<td>' + res.reserved_date + '</td>';
                html += '<td>' + statusBadge + '</td>';
                html += '<td>' + actionBtn + '</td>';
                html += '</tr>';
            });
            tbody.innerHTML = html;
        } else {
            tbody.innerHTML = '<tr><td colspan="5" style="text-align: center; padding: 40px; color: #64748B;">No reservations yet - reserve a book to get started</td></tr>';
        }
    })
    .catch(() => window.location.href = '/SmartLibrary/Logout');
}

function borrowReservedBook(reservationId) {
    fetch('/SmartLibrary/ReserveBook?action=borrow&reservation_id=' + reservationId, {
        method: 'POST'
    })
    .then(res => res.json())
    .then(data => {
        if (data.success) {
            location.reload();
        } else {
            alert(data.message || 'Failed to borrow book');
        }
    })
    .catch(err => alert('Error borrowing book'));
}

function cancelReservation(reservationId) {
    if (!confirm('Are you sure you want to cancel this reservation?')) {
        return;
    }
    
    fetch('/SmartLibrary/ReserveBook?action=cancel&reservation_id=' + reservationId, {
        method: 'POST'
    })
    .then(res => res.json())
    .then(data => {
        if (data.success) {
            location.reload();
        } else {
            alert(data.message || 'Failed to cancel reservation');
        }
    })
    .catch(err => alert('Error cancelling reservation'));
}

document.addEventListener('DOMContentLoaded', initReservations);
