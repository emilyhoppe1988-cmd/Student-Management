# 🎓 Student & Course Management System (Java)

A console-based **Student, Course, and Enrollment Management System** built in Java.
This project demonstrates core Object-Oriented Programming (OOP) principles alongside file-based persistence, with referential integrity between students, courses, and enrollments.

---

## 📌 Features

### 👨‍🎓 Student Management
- Add new student records (with duplicate ID prevention)
- View all students
- Search student by ID
- Update student details
- Delete student by ID *(automatically removes their enrollment records)*
- Stores: ID, Name, Email, Program, Last Modified timestamp

### 📚 Course Management
- Add new courses (with duplicate course code prevention)
- View all courses
- Search course by code
- Update course details
- Delete course by code *(automatically removes related enrollment records)*
- Stores: Course Code, Course Name, Credit Hours, Last Modified timestamp

### 📝 Enrollment Management
- Enroll a student in a course (validates that both exist, prevents duplicate enrollment)
- View a student's enrolled courses
- View a course's full roster
- Stores: Student ID, Course Code, Semester, Grade

### 📊 Statistics
- Total students, courses, and enrollments
- Student count grouped by program
- Last updated timestamp

### 💾 File Storage
- Student data saved in `students.txt`
- Course data saved in `courses.txt`
- Enrollment data saved in `enrollments.txt`
- Pipe-delimited (`|`) format to safely store fields that may contain commas
- Data persists across program restarts

### 🛡️ Input Validation & Reliability
- Safe integer parsing — invalid menu input reprompts instead of crashing
- Required-field validation — blank names, emails, etc. are rejected
- Cascading deletes keep enrollment records consistent when a student or course is removed

---

## 🧠 OOP Concepts Used
- **Abstract Class** — `Person`
- **Inheritance** — `Student extends Person`
- **Encapsulation** — private fields with controlled access via getters
- **Method Overriding** — `display_info()`
- **Polymorphism** — abstract method resolved differently per subclass
- **Manual Object Serialization** — `to_file_string()` / `from_file_string()` for converting objects to and from flat-file records

---

## 🏗️ Project Structure

```
Main_App.java
├── Person          (abstract base class)
├── Student          extends Person
├── Course
├── Enrollment
├── File_Manager     (generic read/write/append for flat files)
└── Main_App         (entry point, menu, business logic)
```

---

## ▶️ How to Run

1. Compile the program:
   `javac Main_App.java`
2. Run it:
   `java Main_App`

The program will create `students.txt`, `courses.txt`, and `enrollments.txt` automatically in the working directory on first use.

---

## 📋 Menu Reference

| # | Action |
|---|--------|
| 1 | Add Student |
| 2 | View Students |
| 3 | Search Student by ID |
| 4 | Update Student |
| 5 | Delete Student by ID |
| 6 | Add Course |
| 7 | View Courses |
| 8 | Search Course by Code |
| 9 | Update Course |
| 10 | Delete Course by Code |
| 11 | Enroll Student in Course |
| 12 | View a Student's Enrolled Courses |
| 13 | View a Course's Roster |
| 14 | Statistics |
| 15 | Exit |

---

## 🚀 Possible Future Enhancements
- Grades & GPA tracking
- Admin login / authentication
- GUI (JavaFX or Swing)
- Database integration (SQLite/MySQL) instead of flat files
- Unit tests with JUnit

---

## 📄 License
This project is open-source and available for educational use.
