-- USERS TABLE
CREATE TABLE users (
    user_id SERIAL PRIMARY KEY,
    username VARCHAR(50) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    email VARCHAR(100) UNIQUE NOT NULL,
    full_name VARCHAR(100) NOT NULL,
    role VARCHAR(20) DEFAULT 'user',
    status VARCHAR(20) DEFAULT 'active',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Sample login credentials (for testing purposes)
INSERT INTO users (username, password, email, full_name, role) VALUES
('admin', 'admin123', 'admin@smartlibrary.com', 'Administrator', 'admin'),
('librarian', 'lib123', 'librarian@smartlibrary.com', 'Ms. Sarah Johnson', 'librarian'),
('john.student', 'pass123', 'john@student.edu', 'John Doe', 'student'),
('prof.smith', 'prof123', 'smith@faculty.edu', 'Dr. Robert Smith', 'faculty');


-- BOOKS TABLE
CREATE TABLE books (
    book_id SERIAL PRIMARY KEY,
    title VARCHAR(200) NOT NULL,
    author VARCHAR(100) NOT NULL,
    publisher VARCHAR(100),
    publish_year INTEGER,
    category VARCHAR(50),
    quantity INTEGER DEFAULT 1,
    available INTEGER DEFAULT 1,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);


-- BORROWS TABLE
CREATE TABLE borrows (
    borrow_id SERIAL PRIMARY KEY,
    user_id INTEGER REFERENCES users(user_id) ON DELETE CASCADE,
    book_id INTEGER REFERENCES books(book_id) ON DELETE CASCADE,
    borrow_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    due_date TIMESTAMP NOT NULL,
    return_date TIMESTAMP,
    status VARCHAR(20) DEFAULT 'borrowed'
);


-- RESERVATIONS TABLE
CREATE TABLE reservations (
    reservation_id SERIAL PRIMARY KEY,
    user_id INTEGER REFERENCES users(user_id) ON DELETE CASCADE,
    book_id INTEGER REFERENCES books(book_id) ON DELETE CASCADE,
    reservation_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    expiration_date TIMESTAMP NOT NULL,
    status VARCHAR(20) DEFAULT 'pending',
    queue_position INTEGER DEFAULT 1
);


-- FINES TABLE
CREATE TABLE fines (
    fine_id SERIAL PRIMARY KEY,
    user_id INTEGER REFERENCES users(user_id) ON DELETE CASCADE,
    borrow_id INTEGER REFERENCES borrows(borrow_id) ON DELETE CASCADE,
    book_id INTEGER REFERENCES books(book_id) ON DELETE SET NULL,
    amount DECIMAL(10,2) NOT NULL,
    reason VARCHAR(255),
    status VARCHAR(20) DEFAULT 'unpaid',
    paid_date TIMESTAMP,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);


-- STATS TABLE
CREATE TABLE stats (
    stat_id SERIAL PRIMARY KEY,
    total_books INTEGER DEFAULT 0,
    total_users INTEGER DEFAULT 0,
    total_borrows INTEGER DEFAULT 0,
    total_fines DECIMAL(10,2) DEFAULT 0
);
