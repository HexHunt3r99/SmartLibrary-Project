document.addEventListener('DOMContentLoaded', function() {
    fetch('/SmartLibrary/UserStats')
        .then(res => res.json())
        .then(data => {
            if (!data.success) window.location.href = '/SmartLibrary/Logout';
        })
        .catch(() => window.location.href = '/SmartLibrary/Logout');
});
