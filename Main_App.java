import java.io.*;
import java.util.*;
import java.time.*;

// ======================= PERSON =======================
abstract class Person {
    protected String name;
    protected String email;

    public Person(String name, String email) {
        this.name = name;
        this.email = email;
    }

    public String getName() { return name; }
    public String getEmail() { return email; }

    public abstract void display_info();
}

// ======================= STUDENT =======================
class Student extends Person {

    private String student_id;
    private String program;
    private String last_modified;

    public Student(String student_id, String name, String email, String program, String last_modified) {
        super(name, email);
        this.student_id = student_id;
        this.program = program;
        this.last_modified = last_modified;
    }

    public String getId() { return student_id; }
    public String getProgram() { return program; }
    public String getLastModified() { return last_modified; }

    public String to_file_string() {
        return student_id + "|" + name + "|" + email + "|" + program + "|" + last_modified;
    }

    // Parses a line written by to_file_string() back into a Student object.
    // Returns null if the line is malformed.
    public static Student from_file_string(String line) {
        String[] parts = line.split("\\|", -1);
        if (parts.length < 5) return null;
        return new Student(parts[0], parts[1], parts[2], parts[3], parts[4]);
    }

    @Override
    public void display_info() {
        System.out.println("ID: " + student_id + " | NAME: " + name +
                " | EMAIL: " + email + " | PROGRAM: " + program +
                " | LAST MODIFIED: " + last_modified);
    }
}

// ======================= COURSE =======================
class Course {

    private String course_code;
    private String course_name;
    private int credit_hours;
    private String last_modified;

    public Course(String course_code, String course_name, int credit_hours, String last_modified) {
        this.course_code = course_code;
        this.course_name = course_name;
        this.credit_hours = credit_hours;
        this.last_modified = last_modified;
    }

    public String getCode() { return course_code; }
    public String getName() { return course_name; }
    public int getCredits() { return credit_hours; }

    public String to_file_string() {
        return course_code + "|" + course_name + "|" + credit_hours + "|" + last_modified;
    }

    public static Course from_file_string(String line) {
        String[] parts = line.split("\\|", -1);
        if (parts.length < 4) return null;
        try {
            return new Course(parts[0], parts[1], Integer.parseInt(parts[2]), parts[3]);
        } catch (NumberFormatException e) {
            return null;
        }
    }

    public void display_info() {
        System.out.println("CODE: " + course_code + " | NAME: " + course_name +
                " | CREDITS: " + credit_hours + " | LAST MODIFIED: " + last_modified);
    }
}

// ======================= ENROLLMENT =======================
class Enrollment {

    private String student_id;
    private String course_code;
    private String semester;
    private String grade; // "N/A" until assigned

    public Enrollment(String student_id, String course_code, String semester, String grade) {
        this.student_id = student_id;
        this.course_code = course_code;
        this.semester = semester;
        this.grade = grade;
    }

    public String getStudentId() { return student_id; }
    public String getCourseCode() { return course_code; }
    public String getSemester() { return semester; }
    public String getGrade() { return grade; }

    public String to_file_string() {
        return student_id + "|" + course_code + "|" + semester + "|" + grade;
    }

    public static Enrollment from_file_string(String line) {
        String[] parts = line.split("\\|", -1);
        if (parts.length < 4) return null;
        return new Enrollment(parts[0], parts[1], parts[2], parts[3]);
    }

    public void display_info() {
        System.out.println("STUDENT: " + student_id + " | COURSE: " + course_code +
                " | SEMESTER: " + semester + " | GRADE: " + grade);
    }
}

// ======================= FILE MANAGER =======================
class File_Manager {

    public static void append(String filename, String data) {
        try (FileWriter fw = new FileWriter(filename, true)) {
            fw.write(data + "\n");
        } catch (IOException e) {
            System.out.println("Error writing file: " + e.getMessage());
        }
    }

    public static List<String> read_all(String filename) {
        List<String> lines = new ArrayList<>();
        File file = new File(filename);

        if (!file.exists()) return lines;

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (!line.isBlank()) lines.add(line);
            }
        } catch (IOException e) {
            System.out.println("Error reading file: " + e.getMessage());
        }

        return lines;
    }

    public static void write_all(String filename, List<String> data) {
        try (PrintWriter pw = new PrintWriter(new FileWriter(filename))) {
            for (String s : data) {
                pw.println(s);
            }
        } catch (IOException e) {
            System.out.println("Error updating file: " + e.getMessage());
        }
    }
}

// ======================= MAIN APP =======================
public class Main_App {

    static Scanner sc = new Scanner(System.in);

    static final String STUDENT_FILE = "students.txt";
    static final String COURSE_FILE = "courses.txt";
    static final String ENROLLMENT_FILE = "enrollments.txt";

    static String getTime() {
        return LocalDateTime.now().toString();
    }

    public static void main(String[] args) {

        while (true) {

            System.out.println("\n========== STUDENT SYSTEM ==========");
            System.out.println("1.  Add Student");
            System.out.println("2.  View Students");
            System.out.println("3.  Search Student by ID");
            System.out.println("4.  Update Student");
            System.out.println("5.  Delete Student by ID");
            System.out.println("6.  Add Course");
            System.out.println("7.  View Courses");
            System.out.println("8.  Search Course by Code");
            System.out.println("9.  Update Course");
            System.out.println("10. Delete Course by Code");
            System.out.println("11. Enroll Student in Course");
            System.out.println("12. View a Student's Enrolled Courses");
            System.out.println("13. View a Course's Roster");
            System.out.println("14. Statistics");
            System.out.println("15. Exit");

            int choice = read_int("Choice: ");

            switch (choice) {
                case 1 -> add_student();
                case 2 -> view_students();
                case 3 -> search_student();
                case 4 -> update_student();
                case 5 -> delete_student();
                case 6 -> add_course();
                case 7 -> view_courses();
                case 8 -> search_course();
                case 9 -> update_course();
                case 10 -> delete_course();
                case 11 -> enroll_student();
                case 12 -> view_student_courses();
                case 13 -> view_course_roster();
                case 14 -> show_stats();
                case 15 -> {
                    System.out.println("Goodbye!");
                    System.exit(0);
                }
                default -> System.out.println("Invalid choice!");
            }
        }
    }

    // ================= INPUT HELPERS =================

    // Reads an integer safely; reprompts on invalid input instead of crashing.
    static int read_int(String prompt) {
        while (true) {
            System.out.print(prompt);
            String input = sc.nextLine().trim();
            try {
                return Integer.parseInt(input);
            } catch (NumberFormatException e) {
                System.out.println("Please enter a valid number.");
            }
        }
    }

    // Reads a non-blank line of text; reprompts on empty input.
    static String read_required(String prompt) {
        while (true) {
            System.out.print(prompt);
            String input = sc.nextLine().trim();
            if (!input.isEmpty()) return input;
            System.out.println("This field cannot be empty.");
        }
    }

    // ================= ADD STUDENT =================
    static void add_student() {

        List<String> students = File_Manager.read_all(STUDENT_FILE);

        String id = read_required("Student ID: ");

        for (String s : students) {
            if (s.startsWith(id + "|")) {
                System.out.println("Student ID already exists!");
                return;
            }
        }

        String name = read_required("Name: ");
        String email = read_required("Email: ");
        String program = read_required("Program: ");

        Student student = new Student(id, name, email, program, getTime());
        File_Manager.append(STUDENT_FILE, student.to_file_string());

        System.out.println("Student added successfully at " + getTime());
    }

    // ================= VIEW STUDENTS =================
    static void view_students() {
        List<String> lines = File_Manager.read_all(STUDENT_FILE);

        if (lines.isEmpty()) {
            System.out.println("No students found.");
            return;
        }

        for (String line : lines) {
            Student student = Student.from_file_string(line);
            if (student != null) student.display_info();
        }
    }

    // ================= SEARCH STUDENT =================
    static Student find_student(String id) {
        List<String> students = File_Manager.read_all(STUDENT_FILE);
        for (String s : students) {
            if (s.startsWith(id + "|")) {
                return Student.from_file_string(s);
            }
        }
        return null;
    }

    static void search_student() {
        String id = read_required("Enter ID: ");
        Student student = find_student(id);

        if (student == null) {
            System.out.println("Student not found.");
            return;
        }
        student.display_info();
    }

    // ================= UPDATE STUDENT =================
    static void update_student() {

        String id = read_required("Enter ID to update: ");

        List<String> students = File_Manager.read_all(STUDENT_FILE);
        List<String> updated = new ArrayList<>();

        boolean found = false;

        for (String s : students) {
            if (s.startsWith(id + "|")) {
                found = true;

                String name = read_required("New Name: ");
                String email = read_required("New Email: ");
                String program = read_required("New Program: ");

                Student student = new Student(id, name, email, program, getTime());
                updated.add(student.to_file_string());

            } else {
                updated.add(s);
            }
        }

        if (!found) {
            System.out.println("Student not found.");
            return;
        }

        File_Manager.write_all(STUDENT_FILE, updated);
        System.out.println("Student updated at " + getTime());
    }

    // ================= DELETE STUDENT =================
    static void delete_student() {

        String id = read_required("Enter ID to delete: ");

        List<String> students = File_Manager.read_all(STUDENT_FILE);
        List<String> updated = new ArrayList<>();

        boolean found = false;

        for (String s : students) {
            if (s.startsWith(id + "|")) {
                found = true;
                continue;
            }
            updated.add(s);
        }

        if (!found) {
            System.out.println("Student not found.");
            return;
        }

        File_Manager.write_all(STUDENT_FILE, updated);

        // Keep enrollment records consistent - remove this student's enrollments too.
        List<String> enrollments = File_Manager.read_all(ENROLLMENT_FILE);
        List<String> remaining_enrollments = new ArrayList<>();
        for (String e : enrollments) {
            if (!e.startsWith(id + "|")) {
                remaining_enrollments.add(e);
            }
        }
        File_Manager.write_all(ENROLLMENT_FILE, remaining_enrollments);

        System.out.println("Student deleted at " + getTime());
    }

    // ================= ADD COURSE =================
    static void add_course() {

        List<String> courses = File_Manager.read_all(COURSE_FILE);

        String code = read_required("Course Code: ");

        for (String c : courses) {
            if (c.startsWith(code + "|")) {
                System.out.println("Course code already exists!");
                return;
            }
        }

        String name = read_required("Course Name: ");
        int credits = read_int("Credit Hours: ");

        Course course = new Course(code, name, credits, getTime());
        File_Manager.append(COURSE_FILE, course.to_file_string());

        System.out.println("Course added at " + getTime());
    }

    // ================= VIEW COURSES =================
    static void view_courses() {
        List<String> lines = File_Manager.read_all(COURSE_FILE);

        if (lines.isEmpty()) {
            System.out.println("No courses found.");
            return;
        }

        for (String line : lines) {
            Course course = Course.from_file_string(line);
            if (course != null) course.display_info();
        }
    }

    // ================= SEARCH COURSE =================
    static Course find_course(String code) {
        List<String> courses = File_Manager.read_all(COURSE_FILE);
        for (String c : courses) {
            if (c.startsWith(code + "|")) {
                return Course.from_file_string(c);
            }
        }
        return null;
    }

    static void search_course() {
        String code = read_required("Enter Course Code: ");
        Course course = find_course(code);

        if (course == null) {
            System.out.println("Course not found.");
            return;
        }
        course.display_info();
    }

    // ================= UPDATE COURSE =================
    static void update_course() {

        String code = read_required("Enter Course Code to update: ");

        List<String> courses = File_Manager.read_all(COURSE_FILE);
        List<String> updated = new ArrayList<>();

        boolean found = false;

        for (String c : courses) {
            if (c.startsWith(code + "|")) {
                found = true;

                String name = read_required("New Course Name: ");
                int credits = read_int("New Credit Hours: ");

                Course course = new Course(code, name, credits, getTime());
                updated.add(course.to_file_string());

            } else {
                updated.add(c);
            }
        }

        if (!found) {
            System.out.println("Course not found.");
            return;
        }

        File_Manager.write_all(COURSE_FILE, updated);
        System.out.println("Course updated at " + getTime());
    }

    // ================= DELETE COURSE =================
    static void delete_course() {

        String code = read_required("Enter Course Code to delete: ");

        List<String> courses = File_Manager.read_all(COURSE_FILE);
        List<String> updated = new ArrayList<>();

        boolean found = false;

        for (String c : courses) {
            if (c.startsWith(code + "|")) {
                found = true;
                continue;
            }
            updated.add(c);
        }

        if (!found) {
            System.out.println("Course not found.");
            return;
        }

        File_Manager.write_all(COURSE_FILE, updated);

        // Keep enrollment records consistent - remove enrollments tied to this course.
        List<String> enrollments = File_Manager.read_all(ENROLLMENT_FILE);
        List<String> remaining_enrollments = new ArrayList<>();
        for (String e : enrollments) {
            Enrollment en = Enrollment.from_file_string(e);
            if (en == null || !en.getCourseCode().equals(code)) {
                remaining_enrollments.add(e);
            }
        }
        File_Manager.write_all(ENROLLMENT_FILE, remaining_enrollments);

        System.out.println("Course deleted at " + getTime());
    }

    // ================= ENROLLMENT =================
    static void enroll_student() {

        String student_id = read_required("Student ID: ");
        if (find_student(student_id) == null) {
            System.out.println("No student with that ID exists. Add the student first.");
            return;
        }

        String course_code = read_required("Course Code: ");
        if (find_course(course_code) == null) {
            System.out.println("No course with that code exists. Add the course first.");
            return;
        }

        List<String> enrollments = File_Manager.read_all(ENROLLMENT_FILE);
        for (String e : enrollments) {
            Enrollment en = Enrollment.from_file_string(e);
            if (en != null && en.getStudentId().equals(student_id) && en.getCourseCode().equals(course_code)) {
                System.out.println("Student is already enrolled in this course.");
                return;
            }
        }

        String semester = read_required("Semester (e.g. Fall 2026): ");

        Enrollment enrollment = new Enrollment(student_id, course_code, semester, "N/A");
        File_Manager.append(ENROLLMENT_FILE, enrollment.to_file_string());

        System.out.println("Enrollment recorded at " + getTime());
    }

    static void view_student_courses() {

        String student_id = read_required("Student ID: ");

        if (find_student(student_id) == null) {
            System.out.println("No student with that ID exists.");
            return;
        }

        List<String> enrollments = File_Manager.read_all(ENROLLMENT_FILE);
        boolean any = false;

        for (String e : enrollments) {
            Enrollment en = Enrollment.from_file_string(e);
            if (en != null && en.getStudentId().equals(student_id)) {
                en.display_info();
                any = true;
            }
        }

        if (!any) System.out.println("This student isn't enrolled in any courses.");
    }

    static void view_course_roster() {

        String course_code = read_required("Course Code: ");

        if (find_course(course_code) == null) {
            System.out.println("No course with that code exists.");
            return;
        }

        List<String> enrollments = File_Manager.read_all(ENROLLMENT_FILE);
        boolean any = false;

        for (String e : enrollments) {
            Enrollment en = Enrollment.from_file_string(e);
            if (en != null && en.getCourseCode().equals(course_code)) {
                en.display_info();
                any = true;
            }
        }

        if (!any) System.out.println("No students are enrolled in this course yet.");
    }

    // ================= STATS =================
    static void show_stats() {

        List<String> students = File_Manager.read_all(STUDENT_FILE);
        List<String> courses = File_Manager.read_all(COURSE_FILE);
        List<String> enrollments = File_Manager.read_all(ENROLLMENT_FILE);

        System.out.println("Total Students: " + students.size());
        System.out.println("Total Courses: " + courses.size());
        System.out.println("Total Enrollments: " + enrollments.size());

        // Students grouped by program
        Map<String, Integer> by_program = new TreeMap<>();
        for (String s : students) {
            Student student = Student.from_file_string(s);
            if (student != null) {
                by_program.merge(student.getProgram(), 1, Integer::sum);
            }
        }
        if (!by_program.isEmpty()) {
            System.out.println("Students per Program:");
            for (Map.Entry<String, Integer> entry : by_program.entrySet()) {
                System.out.println("  " + entry.getKey() + ": " + entry.getValue());
            }
        }

        System.out.println("Last Updated: " + getTime());
    }
}
