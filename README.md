# Smart Library - Library Management System

[![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg)](LICENSE)

A comprehensive web-based Library Management System built with Java Servlet/JSP technology, designed to streamline library operations for administrators, librarians, faculty, and students.

## 📚 Overview

Smart Library is a full-featured library management system that enables efficient management of books, users, borrowing records, fines, and reservations. The system supports multiple user roles with role-specific dashboards and permissions.

## ✨ Features

### For Administrators
- **Dashboard** with statistics and analytics (user stats, book stats, category stats, borrow trends)
- **User Management** - Add, edit, suspend/activate users
- **Book Management** - Add, update, remove books from collection
- **Borrowing Oversight** - View all active borrows and history
- **Fine Management** - Track and manage overdue fines
- **Reservation Management** - Handle book reservations

### For Librarians
- Assisted management capabilities with limited admin privileges

### For Students & Faculty
- **Personal Dashboard** with borrowing history and status
- **Book Search & Browse** - Search by title, author, category
- **Borrow Books** - Check out available books
- **Reserve Books** - Reserve books that are currently unavailable
- **Renew Books** - Extend borrowing period online
- **Fine Payments** - View and pay outstanding fines
- **Profile Management** - Update personal information

## 🛠️ Technologies Used

- **Backend:** Java Servlets (Jakarta EE / Java EE)
- **Frontend:** JSP (JavaServer Pages), HTML5, CSS3, JavaScript
- **Database:** PostgreSQL
- **Server:** Apache Tomcat 10
- **Build/Dependencies:** Manual JAR management (PostgreSQL JDBC Driver)

## 📁 Project Structure

```
SmartLibrary-Project/
├── src/
│   └── main/
│       └── java/
│           └── com/
│               └── smartlibrary/
│                   ├── controller/          # Servlet classes
│                   │   ├── LoginServlet.java
│                   │   ├── RegisterServlet.java
│                   │   ├── BookServlet.java
│                   │   ├── BorrowBookServlet.java
│                   │   ├── UserServlet.java
│                   │   ├── AdminStatsServlet.java
│                   │   └── ... (other servlets)
│                   └── util/                # Utility classes
│                       ├── DBConnection.java
│                       ├── AdminCreator.java
│                       └── RoleUtils.java
└── webapps/
    └── SmartLibrary/              # Web application root
        ├── admin/                 # Admin panel pages
        │   ├── dashboard.jsp
        │   ├── books.jsp
        │   ├── users.jsp
        │   ├── borrows.jsp
        │   ├── fines.jsp
        │   ├── reservations.jsp
        │   ├── css/              # Admin-specific styles
        │   └── js/               # Admin-specific scripts
        ├── user/                  # User panel pages
        │   ├── dashboard.jsp
        │   ├── books.jsp
        │   ├── borrows.jsp
        │   ├── fines.jsp
        │   ├── reservations.jsp
        │   ├── profile.jsp
        │   ├── help.jsp
        │   ├── css/              # User-specific styles
        │   └── js/               # User-specific scripts
        ├── css/                   # Public styles (login, register, index)
        ├── js/                    # Public scripts
        ├── database/
        │   └── schema.sql        # Database schema
        ├── WEB-INF/
        │   ├── web.xml           # Servlet mappings and config
        │   └── lib/              # JAR dependencies
        │       ├── postgresql-42.7.1.jar
        │       └── checker-qual-3.41.0.jar
        ├── index.jsp             # Landing page
        ├── login.jsp             # Login page
        └── register.jsp          # Registration page
```

## 🚀 Setup and Installation

### Prerequisites
- Java JDK 11 or higher
- Apache Tomcat 10.x
- PostgreSQL 12 or higher
- Git (for cloning the repository)

### Step 1: Clone the Repository
```bash
git clone https://github.com/HexHunt3r99/SmartLibrary-Project.git
cd SmartLibrary-Project
```

### Step 2: Database Setup
1. Create a PostgreSQL database named `smartlibrary` (or your preferred name)
2. Update the database connection in `src/main/java/com/smartlibrary/util/DBConnection.java`:
   ```java
   String url = "jdbc:postgresql://localhost:5432/smartlibrary";
   String user = "your_postgres_username";
   String password = "your_postgres_password";
   ```
3. Run the database schema script:
   ```bash
   psql -U postgres -d smartlibrary -f webapps/SmartLibrary/database/schema.sql
   ```
   Or use a GUI tool like pgAdmin to execute the SQL file.

### Step 3: Compile Java Sources
Navigate to the project directory and compile the Java files:
```bash
# Windows
javac -cp "webapps/SmartLibrary/WEB-INF/lib/*" -d webapps/SmartLibrary/WEB-INF/classes src/main/java/com/smartlibrary/**/*.java

# Create WEB-INF/classes directory if it doesn't exist
mkdir webapps/SmartLibrary/WEB-INF/classes
```

### Step 4: Deploy to Tomcat
1. Copy the entire `SmartLibrary` folder to Tomcat's `webapps` directory:
   ```bash
   # Windows
   copy /E /I webapps\SmartLibrary C:\apache-tomcat-10.x\webapps\SmartLibrary
   ```

2. Or create a WAR file and deploy:
   ```bash
   # Navigate to webapps/SmartLibrary directory
   cd webapps/SmartLibrary
   jar -cvf SmartLibrary.war *
   # Copy WAR to Tomcat webapps
   copy SmartLibrary.war C:\apache-tomcat-10.x\webapps\
   ```

### Step 5: Start Tomcat
1. Navigate to Tomcat's bin directory
2. Run startup script:
   ```bash
   # Windows
   C:\apache-tomcat-10.x\bin\startup.bat
   ```

### Step 6: Access the Application
Open your browser and navigate to:
```
http://localhost:8080/SmartLibrary/
```

### Default Login Credentials
After running the schema.sql, you can use these credentials:
- **Admin:** username: `admin`, password: `admin123`
- **Librarian:** username: `librarian`, password: `lib123`
- **Student:** username: `john.student`, password: `pass123`
- **Faculty:** username: `prof.smith`, password: `prof123`

## 📸 Screenshots

### Landing Page
![Landing Page](screenshots/landing.png)

### Admin Dashboard
![Admin Dashboard](screenshots/admin-dashboard.png)

### User Dashboard
![User Dashboard](screenshots/user-dashboard.png)

### Book Management
![Book Management](screenshots/books.png)


## 🔧 Configuration

### Database Configuration
Edit `src/main/java/com/smartlibrary/util/DBConnection.java` to modify database settings:
- Database URL
- Username
- Password
- Connection pool settings (if implementing)

### Tomcat Configuration
- Default port: 8080
- To change port, edit `conf/server.xml` in your Tomcat installation

## 👥 User Roles

| Role | Description | Permissions |
|------|-------------|-------------|
| `admin` | System Administrator | Full access to all features |
| `librarian` | Library Staff | Assisted management capabilities |
| `faculty` | Faculty Member | Extended borrowing privileges |
| `student` | Student User | Standard borrowing and reservation |
| `user` | Generic User | Basic access (default role) |


## 📝 Notes

- Passwords in the sample data are plain text for demo purposes. In production, implement password hashing (BCrypt recommended).
- The system uses session-based authentication.
- Book availability is automatically updated when books are borrowed/returned.
- Fines are calculated based on overdue days (customize logic in FineServlet).

## 🚀 Future Enhancements

- [ ] Password encryption with BCrypt
- [ ] Email notifications for due dates
- [ ] QR code generation for book labels
- [ ] Export reports to PDF/Excel
- [ ] Multi-language support
- [ ] Mobile responsive design improvements
- [ ] RESTful API for mobile app integration

## 🤝 Contributing

1. Fork the repository
2. Create a feature branch (`git checkout -b feature/AmazingFeature`)
3. Commit your changes (`git commit -m 'Add some AmazingFeature'`)
4. Push to the branch (`git push origin feature/AmazingFeature`)
5. Open a Pull Request

## 📄 License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

## 📧 Contact

For questions or support, please contact:
- Project Link: [https://github.com/HexHunt3r99/SmartLibrary-Project](https://github.com/HexHunt3r99/SmartLibrary-Project)

---

⭐ If you found this project helpful, please give it a star on GitHub!
