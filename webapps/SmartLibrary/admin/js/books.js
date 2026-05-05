let isLoading = false;
window.allBooks = [];
window.reservedBookIds = new Set();

function loadCategories() {
    fetch('/SmartLibrary/CategoryServlet')
    .then(response => response.json())
    .then(categories => {
        const addCategory = document.getElementById('category');
        const editCategory = document.getElementById('editCategory');
        const categoryFilter = document.getElementById('categoryFilter');
        
        addCategory.innerHTML = '<option value="">Select Category</option>';
        editCategory.innerHTML = '<option value="">Select Category</option>';
        categoryFilter.innerHTML = '<option value="">All Categories</option>';
        
        categories.forEach(cat => {
            addCategory.innerHTML += '<option value="' + cat + '">' + cat + '</option>';
            editCategory.innerHTML += '<option value="' + cat + '">' + cat + '</option>';
            categoryFilter.innerHTML += '<option value="' + cat + '">' + cat + '</option>';
        });
    })
    .catch(error => {
        console.error('Error loading categories:', error);
    });
}

document.addEventListener('DOMContentLoaded', function() {
    fetch('/SmartLibrary/AdminStats')
    .then(res => res.json())
    .then(data => {
        if (!data.success) {
            window.location.href = '/SmartLibrary/Logout';
            return;
        }
        document.getElementById('totalBooksStat').textContent = data.total_books;
        document.getElementById('availableStat').textContent = data.available_books;
        document.getElementById('borrowedStat').textContent = data.active_borrows;
        document.getElementById('reservedStat').textContent = data.total_reservations;
    })
    .catch(() => window.location.href = '/SmartLibrary/Logout');
    
    loadCategories();
    document.getElementById('categoryFilter').addEventListener('change', filterBooks);
    document.getElementById('statusFilter').addEventListener('change', filterBooks);
    loadBooks();
});

function openModal() {
    document.getElementById('addBookModal').classList.add('show');
}

function closeModal() {
    document.getElementById('addBookModal').classList.remove('show');
    document.getElementById('addBookForm').reset();
}

function closeEditModal() {
    document.getElementById('editBookModal').classList.remove('show');
}

document.getElementById('addBookForm').addEventListener('submit', function(e) {
    e.preventDefault();
    
    if (isLoading) return;
    isLoading = true;
    
    let formData = new URLSearchParams(new FormData(this));
    let submitBtn = this.querySelector('.btn-submit');
    submitBtn.textContent = 'Adding...';
    submitBtn.disabled = true;
    
    fetch('/SmartLibrary/AddBook', {
        method: 'POST',
        body: formData
    })
    .then(response => response.json())
    .then(data => {
        if (data.success) {
            closeModal();
            loadBooks();
        }
    })
    .catch(error => {
    })
    .finally(() => {
        isLoading = false;
        submitBtn.textContent = 'Add Book';
        submitBtn.disabled = false;
    });
});

document.getElementById('editBookForm').addEventListener('submit', function(e) {
    e.preventDefault();
    
    let formData = new URLSearchParams();
    formData.append('action', 'update');
    formData.append('id', document.getElementById('editBookId').value);
    formData.append('title', document.getElementById('editTitle').value);
    formData.append('author', document.getElementById('editAuthor').value);
    formData.append('publisher', document.getElementById('editPublisher').value);
    formData.append('publish_year', document.getElementById('editPublishYear').value);
    formData.append('category', document.getElementById('editCategory').value);
    formData.append('quantity', document.getElementById('editQuantity').value);
    
    fetch('../BookServlet', {
        method: 'POST',
        body: formData
    })
    .then(response => response.json())
    .then(data => {
        if (data.success) {
            closeEditModal();
            loadBooks();
        }
    });
});

function loadBooks() {
    Promise.all([
        fetch('/SmartLibrary/BookServlet').then(response => response.json()),
        fetch('/SmartLibrary/AdminReservations').then(response => response.json()).catch(() => [])
    ])
    .then(([books, reservations]) => {
        window.allBooks = books;
        window.reservedBookIds = new Set(
            reservations
                .filter(res => ['active', 'pending', 'ready'].includes((res.status || '').toLowerCase()))
                .map(res => res.book_id)
        );
        filterBooks();
    })
    .catch(error => console.error('Error loading books:', error));
}

function searchBooks() {
    filterBooks();
}

function filterBooks() {
    const searchTerm = document.getElementById('searchInput').value.toLowerCase();
    const selectedCategory = document.getElementById('categoryFilter').value;
    const selectedStatus = document.getElementById('statusFilter').value;
    const tbody = document.querySelector('tbody');
    tbody.innerHTML = '';
    
    if (!window.allBooks || window.allBooks.length === 0) {
        tbody.innerHTML = '<tr><td colspan="6" style="text-align: center; padding: 40px; color: #64748B;">No books found in library</td></tr>';
        return;
    }
    
    const filtered = window.allBooks.filter(book => {
        const matchesSearch =
            book.title.toLowerCase().includes(searchTerm) ||
            book.author.toLowerCase().includes(searchTerm) ||
            (book.category && book.category.toLowerCase().includes(searchTerm));
        const matchesCategory = !selectedCategory || book.category === selectedCategory;
        const bookStatus = getBookStatus(book);
        const matchesStatus = !selectedStatus || bookStatus === selectedStatus;

        return matchesSearch && matchesCategory && matchesStatus;
    });
    
    if (filtered.length === 0) {
        tbody.innerHTML = '<tr><td colspan="6" style="text-align: center; padding: 40px; color: #64748B;">No books match your search</td></tr>';
        return;
    }
    
    filtered.forEach(book => {
        const bookStatus = getBookStatus(book);
        let statusBadge = '<span class="status-badge status-available">Available</span>';
        if (bookStatus === 'Borrowed') {
            statusBadge = '<span class="status-badge status-borrowed">Borrowed</span>';
        } else if (bookStatus === 'Reserved') {
            statusBadge = '<span class="status-badge status-reserved">Reserved</span>';
        }
        
        let categoryClass = 'cat-programming';
        if (book.category === 'Science') categoryClass = 'cat-science';
        else if (book.category === 'History') categoryClass = 'cat-history';
        else if (book.category === 'Mathematics') categoryClass = 'cat-math';
        
        let row = '<tr>' +
            '<td><div class="book-cell">' +
            '<div class="book-cover" style="background: linear-gradient(135deg, #4A90D9, #2C5282);">' +
            '<i class="fas fa-book"></i></div>' +
            '<div class="book-info"><h4>' + book.title + '</h4></div></div></td>' +
            '<td><span class="category-badge ' + categoryClass + '">' + (book.category || '-') + '</span></td>' +
            '<td>' + book.author + '</td>' +
            '<td>' + book.available + '/' + book.quantity + '</td>' +
            '<td>' + statusBadge + '</td>' +
            '<td>' +
                '<button class="action-btn edit" onclick="editBook(' + book.book_id + ')"><i class="fas fa-edit"></i></button>' +
            '<button class="action-btn delete" onclick="deleteBook(' + book.book_id + ')"><i class="fas fa-trash"></i></button>' +
            '</td></tr>';
        tbody.innerHTML += row;
    });
}

function deleteBook(bookId) {
    if (confirm('Are you sure you want to delete this book?')) {
        let formData = new URLSearchParams();
        formData.append('action', 'delete');
        formData.append('id', bookId);
        
        fetch('/SmartLibrary/BookServlet', {
            method: 'POST',
            body: formData
        })
        .then(response => response.json())
        .then(data => {
            loadBooks();
        });
    }
}

window.onclick = function(event) {
    if (event.target.classList.contains('modal')) {
        closeModal();
    }
}

function editBook(bookId) {
    fetch('/SmartLibrary/BookServlet?id=' + bookId)
    .then(response => response.json())
    .then(data => {
        if (data && data.book) {
            const book = data.book;
            document.getElementById('editBookId').value = book.book_id;
            document.getElementById('editTitle').value = book.title;
            document.getElementById('editAuthor').value = book.author;
            document.getElementById('editPublisher').value = book.publisher;
            document.getElementById('editPublishYear').value = book.publish_year;
            document.getElementById('editCategory').value = book.category;
            document.getElementById('editQuantity').value = book.quantity;
            document.getElementById('editBookModal').classList.add('show');
        }
    });
}

function openAddModal() {
    document.getElementById('addBookModal').classList.add('show');
}

function getBookStatus(book) {
    if (window.reservedBookIds && window.reservedBookIds.has(book.book_id)) {
        return 'Reserved';
    }
    if (book.available > 0) {
        return 'Available';
    }
    return 'Borrowed';
}
