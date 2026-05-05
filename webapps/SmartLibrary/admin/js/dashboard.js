const borrowCtx = document.getElementById('borrowChart');

if (borrowCtx && Chart.getChart(borrowCtx)) {
    Chart.getChart(borrowCtx).destroy();
}

const categoryCtx = document.getElementById('categoryChart');

if (categoryCtx && Chart.getChart(categoryCtx)) {
    Chart.getChart(categoryCtx).destroy();
}

if (!borrowCtx || !categoryCtx) {
    console.log('Charts not found, skipping initialization');
} else {
    const borrowChartCtx = borrowCtx.getContext('2d');
    let borrowChart;

    function loadBorrowTrends(period) {
        fetch('/SmartLibrary/BorrowTrends?period=' + period)
        .then(response => response.json())
        .then(data => {
            const labels = data.map(item => item.label);
            const borrows = data.map(item => item.borrows);
            const returns = data.map(item => item.returns);
            
            if (borrowChart) {
                borrowChart.data.labels = labels;
                borrowChart.data.datasets[0].data = borrows;
                borrowChart.data.datasets[1].data = returns;
                borrowChart.update();
            } else {
                borrowChart = new Chart(borrowChartCtx, {
                    type: 'line',
                    data: {
                        labels: labels,
                        datasets: [{
                            label: 'Borrows',
                            data: borrows,
                            borderColor: '#4A90D9',
                            backgroundColor: 'rgba(74, 144, 217, 0.1)',
                            fill: true,
                            tension: 0.4,
                            pointRadius: 4,
                            pointBackgroundColor: '#4A90D9'
                        }, {
                            label: 'Returns',
                            data: returns,
                            borderColor: '#10B981',
                            backgroundColor: 'rgba(16, 185, 129, 0.1)',
                            fill: true,
                            tension: 0.4,
                            pointRadius: 4,
                            pointBackgroundColor: '#10B981'
                        }]
                    },
                    options: {
                        responsive: true,
                        maintainAspectRatio: false,
                        plugins: {
                            legend: {
                                position: 'top',
                                align: 'end',
                                labels: {
                                    usePointStyle: true,
                                    padding: 20
                                }
                            }
                        },
                        scales: {
                            y: {
                                beginAtZero: true,
                                grid: {
                                    color: '#E2E8F0'
                                }
                            },
                            x: {
                                grid: {
                                    display: false
                                }
                            }
                        }
                    }
                });
            }
        })
        .catch(err => {
            console.error('Error loading borrow trends:', err);
        });
    }

    loadBorrowTrends('thisMonth');

    const categoryChartCtx = categoryCtx.getContext('2d');
    
    fetch('/SmartLibrary/CategoryStatsServlet')
    .then(response => response.json())
    .then(data => {
        let chartData = [35, 25, 20, 12, 8];
        let chartLabels = ['Programming', 'Science', 'Mathematics', 'History', 'Literature'];
        
        if (data && data.length > 0) {
            chartLabels = data.map(item => item.category);
            chartData = data.map(item => item.count);
        }
        
        new Chart(categoryChartCtx, {
            type: 'doughnut',
            data: {
                labels: chartLabels,
                datasets: [{
                    data: chartData,
                    backgroundColor: [
                        '#4A90D9',
                        '#10B981',
                        '#F59E0B',
                        '#7C3AED',
                        '#EF4444'
                    ],
                    borderWidth: 0,
                    hoverOffset: 10
                }]
            },
            options: {
                responsive: true,
                maintainAspectRatio: false,
                cutout: '60%',
                plugins: {
                    legend: {
                        position: 'right',
                        labels: {
                            usePointStyle: true,
                            padding: 15
                        }
                    }
                }
            }
        });
    })
    .catch(err => {
        new Chart(categoryChartCtx, {
            type: 'doughnut',
            data: {
                labels: ['Programming', 'Science', 'Mathematics', 'History', 'Literature'],
                datasets: [{
                    data: [35, 25, 20, 12, 8],
                    backgroundColor: ['#4A90D9', '#10B981', '#F59E0B', '#7C3AED', '#EF4444'],
                    borderWidth: 0,
                    hoverOffset: 10
                }]
            },
            options: {
                responsive: true,
                maintainAspectRatio: false,
                cutout: '60%',
                plugins: {
                    legend: {
                        position: 'right',
                        labels: {
                            usePointStyle: true,
                            padding: 15
                        }
                    }
                }
            }
        });
    });
}

document.addEventListener('DOMContentLoaded', function() {
    fetch('/SmartLibrary/AdminStats')
    .then(response => response.json())
    .then(data => {
        if (data.success) {
            document.getElementById('totalUsers').textContent = data.total_users;
            document.getElementById('totalBooks').textContent = data.total_books;
            document.getElementById('activeBorrows').textContent = data.active_borrows;
            document.getElementById('totalReservations').textContent = data.total_reservations;
        } else {
            window.location.href = '/SmartLibrary/Logout';
        }
    })
    .catch(err => window.location.href = '/SmartLibrary/Logout');
    
    loadRecentActivity();
});

function loadRecentActivity() {
    fetch('/SmartLibrary/RecentActivity')
    .then(res => res.json())
    .then(activities => {
        const list = document.getElementById('activityList');
        
        if (activities.length === 0) {
            list.innerHTML = '<div style="text-align:center;padding:40px;color:#6B7280;">No recent activity</div>';
            return;
        }
        
        list.innerHTML = '';
        activities.forEach(activity => {
            let iconClass = 'fa-book';
            let iconColor = 'blue';
            
            if (activity.type === 'user') { iconClass = 'fa-user-plus'; iconColor = 'green'; }
            else if (activity.type === 'borrow') { iconClass = 'fa-book'; iconColor = 'blue'; }
            else if (activity.type === 'return') { iconClass = 'fa-check-circle'; iconColor = 'green'; }
            else if (activity.type === 'reservation') { iconClass = 'fa-calendar-check'; iconColor = 'purple'; }
            
            const timeAgo = getTimeAgo(new Date(activity.timestamp));
            const description = activity.type === 'user' ? 
                activity.full_name + ' registered' : 
                '"' + activity.detail + '" by ' + activity.full_name;
            
            const item = document.createElement('div');
            item.className = 'activity-item';
            item.innerHTML = `
                <div class="activity-icon ${iconColor}">
                    <i class="fas ${iconClass}"></i>
                </div>
                <div class="activity-details">
                    <h4>${activity.title}</h4>
                    <p>${description}</p>
                </div>
                <span class="activity-time">${timeAgo}</span>
            `;
            list.appendChild(item);
        });
    })
    .catch(err => console.error('Error loading activity:', err));
}

function getTimeAgo(date) {
    const now = new Date();
    const diffMs = now - date;
    const diffMins = Math.floor(diffMs / 60000);
    const diffHours = Math.floor(diffMs / 3600000);
    const diffDays = Math.floor(diffMs / 86400000);
    
    if (diffMins < 1) return 'Just now';
    if (diffMins < 60) return diffMins + ' mins ago';
    if (diffHours < 24) return diffHours + ' hour' + (diffHours > 1 ? 's' : '') + ' ago';
    return diffDays + ' day' + (diffDays > 1 ? 's' : '') + ' ago';
}
