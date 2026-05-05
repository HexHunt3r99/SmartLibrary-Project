function initBooks() {
    window.allBooks = [];
    window.borrowedBookIds = [];
    
    Promise.all([
        fetch('/SmartLibrary/BookServlet').then(r => r.json()),
        fetch('/SmartLibrary/UserStats').then(r => r.json())
    ])
    .then(([books, stats]) => {
        window.allBooks = books;
        window.borrowedBookIds = stats.borrowed_book_ids || [];
        displayBooks(books);
    })
    .catch(err => {
        console.error('Error:', err);
        const tbody = document.querySelector('tbody');
        tbody.innerHTML = '<tr><td colspan="5" style="text-align: center; padding: 40px; color: #64748B;">Unable to load books. Please try again later.</td></tr>';
        resultsCount.textContent = 'Unable to load books';
    });
}

function displayBooks(books) {
    const tbody = document.querySelector('tbody');
    tbody.innerHTML = '';
    
        if (books.length === 0) {
        tbody.innerHTML = '<tr><td colspan="5" style="text-align: center; padding: 40px; color: #64748B;">No books available in library</td></tr>';
        resultsCount.textContent = '0 books found';
        return;
    }
    
    books.forEach(book => {
        const alreadyBorrowed = window.borrowedBookIds.includes(book.book_id);
        
        let statusBadge = book.available > 0 ? 
            '<span class="status-badge status-available">Available</span>' :
            '<span class="status-badge status-out">Out of Stock</span>';
        
        let actionBtn = alreadyBorrowed ? 
            '<button class="btn-action btn-warning" disabled>Already Borrowed</button>' :
            (book.available > 0 ? 
                '<button class="btn-action btn-primary" onclick="borrowBook(' + book.book_id + ')"><i class="fas fa-book"></i> Borrow</button>' :
                '<button class="btn-action btn-secondary" onclick="reserveBook(' + book.book_id + ')"><i class="fas fa-calendar-plus"></i> Reserve</button>');
        
        tbody.innerHTML += '<tr>' +
            '<td><div class="book-info"><div class="book-thumb" style="background: linear-gradient(135deg, #4A90D9, #2C5282);"><i class="fas fa-book"></i></div><div class="book-details"><h4>' + book.title + '</h4><span>' + book.author + '</span></div></div></td>' +
            '<td>' + (book.publisher || '-') + '</td>' +
            '<td>' + (book.category || '-') + '</td>' +
            '<td>' + statusBadge + '</td>' +
            '<td>' + actionBtn + '</td></tr>';
    });
    
    resultsCount.textContent = books.length + ' books found';
}

function searchBooks() {
    const term = document.getElementById('searchInput').value.toLowerCase();
    const filtered = window.allBooks.filter(book => 
        book.title.toLowerCase().includes(term) ||
        book.author.toLowerCase().includes(term)
    );
    displayBooks(filtered);
}

function borrowBook(bookId) {
    const days = prompt('How many days do you want to borrow?', '14');
    if (days === null || days === '' || isNaN(days) || parseInt(days) <= 0) {
        return;
    }
    
    if (!confirm('Are you sure you want to borrow this book?')) {
        return;
    }
    
    let formData = new URLSearchParams();
    formData.append('book_id', bookId);
    formData.append('days', days);
    
    fetch('/SmartLibrary/BorrowBook', {
        method: 'POST',
        body: formData
    })
    .then(res => res.json())
    .then(data => {
        if (data.success) {
            location.reload();
        } else {
            alert(data.message);
        }
    });
}

function reserveBook(bookId) {
    if (!confirm('Do you want to reserve this book? You\'ll be notified when it becomes available.')) {
        return;
    }
    
    fetch('/SmartLibrary/ReserveBook?action=reserve&book_id=' + bookId, {
        method: 'POST'
    })
    .then(res => res.json())
    .then(data => {
        if (data.success) {
            alert(data.message);
            location.reload();
        } else {
            alert(data.message);
        }
    })
    .catch(err => alert('Error reserving book'));
}

document.addEventListener('DOMContentLoaded', initBooks);
