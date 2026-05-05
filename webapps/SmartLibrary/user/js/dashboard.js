function initDashboard() {
    fetch('/SmartLibrary/UserStats')
    .then(res => res.json())
    .then(data => {
        if (!data.success) {
            window.location.href = '../login.jsp';
            return;
        }
        
        let name = data.full_name || data.username || 'User';
        document.getElementById('welcomeMsg').textContent = 'Welcome back, ' + name + '!';
        document.getElementById('borrowedBooks').textContent = data.borrowed_books;
        document.getElementById('reservations').textContent = data.active_reservations;
        document.getElementById('unpaidFines').innerHTML = '₹' + data.total_fines;
        
        const activityList = document.getElementById('recentActivity');
        if (data.recent_activity && data.recent_activity.length > 0) {
            let html = '';
            data.recent_activity.forEach(activity => {
                let iconClass = '';
                let iconBgClass = '';
                let activityText = '';
                
                if (activity.type === 'borrow') {
                    iconClass = 'fas fa-book';
                    iconBgClass = 'blue';
                    activityText = 'Borrowed book';
                } else if (activity.type === 'reservation') {
                    iconClass = 'fas fa-calendar-check';
                    iconBgClass = 'purple';
                    activityText = 'Reserved book';
                } else if (activity.type === 'fine') {
                    iconClass = 'fas fa-dollar-sign';
                    iconBgClass = 'red';
                    activityText = 'Fine imposed';
                }
                
                let statusClass = activity.status;
                if (activity.status === 'borrowed' || activity.status === 'active') statusClass = 'active';
                if (activity.status === 'paid') statusClass = 'paid';
                let statusDisplay = activity.status.charAt(0).toUpperCase() + activity.status.slice(1);
                
                html += '<div class="activity-item">';
                html += '<div class="activity-icon ' + iconBgClass + '"><i class="' + iconClass + '"></i></div>';
                html += '<div class="activity-details"><h4>' + activityText + '</h4><p>' + activity.title + '</p></div>';
                html += '<span class="activity-status ' + statusClass + '">' + statusDisplay + '</span>';
                html += '</div>';
            });
            activityList.innerHTML = html;
        } else {
            activityList.innerHTML = '<div style="text-align:center;padding:40px;color:#64748B;">No recent activity</div>';
        }
    })
    .catch(() => window.location.href = '../login.jsp');
}

document.addEventListener('DOMContentLoaded', initDashboard);
