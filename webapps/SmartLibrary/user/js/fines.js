function initFines() {
    fetch('/SmartLibrary/UserStats')
    .then(res => res.json())
    .then(data => {
        if (!data.success) {
            window.location.href = '/SmartLibrary/Logout';
            return;
        }
        document.getElementById('pendingFines').innerHTML = '₹' + data.total_fines;
        document.getElementById('totalPaid').innerHTML = '₹' + data.total_fines_paid;
        document.getElementById('lateReturns').textContent = data.late_returns + ' Times';
        
        const tbody = document.getElementById('finesTable');
        if (data.fines && data.fines.length > 0) {
            let html = '';
            data.fines.forEach(fine => {
                let statusBadge = '';
                let actionBtn = '';
                
                if (fine.status === 'paid') {
                    statusBadge = '<span class="status-badge status-paid">Paid</span>';
                    actionBtn = '<button class="btn-action btn-paid" disabled><i class="fas fa-check"></i> Paid</button>';
                } else {
                    statusBadge = '<span class="status-badge status-unpaid">Unpaid</span>';
                    actionBtn = '<button class="btn-action btn-pay" onclick="payFine(' + fine.id + ', ' + fine.amount + ')"><i class="fas fa-credit-card"></i> Pay Now</button>';
                }
                
                html += '<tr>';
                html += '<td><div class="book-info"><div class="book-thumb" style="background: linear-gradient(135deg, #4A90D9, #2C5282);"><i class="fas fa-book"></i></div><div class="book-details"><h4>' + (fine.title || 'N/A') + '</h4><span>' + (fine.author || '') + '</span></div></div></td>';
                html += '<td><strong>₹' + fine.amount.toFixed(2) + '</strong></td>';
                html += '<td>' + (fine.reason || 'Late return') + '</td>';
                html += '<td>' + (fine.date || 'N/A') + '</td>';
                html += '<td>' + statusBadge + '</td>';
                html += '<td>' + actionBtn + '</td>';
                html += '</tr>';
            });
            tbody.innerHTML = html;
        } else {
            tbody.innerHTML = '<tr><td colspan="6" style="text-align: center; padding: 40px; color: #64748B;">No fines - you are all clear!</td></tr>';
        }
    })
    .catch(() => window.location.href = '/SmartLibrary/Logout');
}

function payFine(fineId, amount) {
    if (!confirm('Are you sure you want to pay this fine of ₹' + amount + '?')) {
        return;
    }
    
    fetch('/SmartLibrary/FineServlet?action=pay&fine_id=' + fineId, {
        method: 'POST'
    })
    .then(res => res.json())
    .then(data => {
        if (data.success) {
            alert('Fine paid successfully!');
            location.reload();
        } else {
            alert(data.message || 'Failed to pay fine');
        }
    })
    .catch(err => alert('Error paying fine'));
}

document.addEventListener('DOMContentLoaded', initFines);
