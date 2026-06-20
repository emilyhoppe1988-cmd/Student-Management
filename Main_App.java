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

    public abstract void display_info();
}

// ======================= STUDENT =======================
class Student extends Person {

    private String student_id;
    private String program;

    public Student(String student_id, String name, String email, String program) {
        super(name, email);
        this.student_id = student_id;
        this.program = program;
    }

    public String getId() {
        return student_id;
    }

    public String to_file_string() {
        return student_id + "," + name + "," + email + "," + program;
    }

    @Override
    public void display_info() {
        System.out.println("ID: " + student_id + " | NAME: " + name +
                " | EMAIL: " + email + " | PROGRAM: " + program);
    }
}

// ======================= COURSE =======================
class Course {

    private String course_code;
    private String course_name;
    private int credit_hours;

    public Course(String course_code, String course_name, int credit_hours) {
        this.course_code = course_code;
        this.course_name = course_name;
        this.credit_hours = credit_hours;
    }

    public String to_file_string() {
        return course_code + "," + course_name + "," + credit_hours;
    }
}

// ======================= FILE MANAGER =======================
class File_Manager {

    public static void append(String filename, String data) {
        try (FileWriter fw = new FileWriter(filename, true)) {
            fw.write(data + "\n");
        } catch (IOException e) {
            System.out.println("Error writing file");
        }
    }

    public static List<String> read_all(String filename) {
        List<String> lines = new ArrayList<>();
        File file = new File(filename);

        if (!file.exists()) return lines;

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                lines.add(line);
            }
        } catch (IOException e) {
            System.out.println("Error reading file");
        }

        return lines;
    }

    public static void write_all(String filename, List<String> data) {
        try (PrintWriter pw = new PrintWriter(new FileWriter(filename))) {
            for (String s : data) {
                pw.println(s);
            }
        } catch (IOException e) {
            System.out.println("Error updating file");
        }
    }
}

// ======================= MAIN APP =======================
public class Main_App {

    static Scanner sc = new Scanner(System.in);

    static final String STUDENT_FILE = "students.txt";
    static final String COURSE_FILE = "courses.txt";

    // ⏱️ TIME FUNCTION
    static String getTime() {
        return LocalDateTime.now().toString();
    }

    public static void main(String[] args) {

        while (true) {

            System.out.println("\n========== STUDENT SYSTEM ==========");
            System.out.println("1. Add Student");
            System.out.println("2. View Students");
            System.out.println("3. Search Student by ID");
            System.out.println("4. Update Student");
            System.out.println("5. Delete Student by ID");
            System.out.println("6. Add Course");
            System.out.println("7. View Courses");
            System.out.println("8. Statistics");
            System.out.println("9. Exit");

            System.out.print("Choice: ");
            int choice = sc.nextInt();
            sc.nextLine();

            switch (choice) {
                case 1 -> add_student();
                case 2 -> view_students();
                case 3 -> search_student();
                case 4 -> update_student();
                case 5 -> delete_student();
                case 6 -> add_course();
                case 7 -> view_courses();
                case 8 -> show_stats();
                case 9 -> System.exit(0);
                default -> System.out.println("Invalid choice!");
            }
        }
    }

    // ================= ADD STUDENT =================
    static void add_student() {

        List<String> students = File_Manager.read_all(STUDENT_FILE);

        System.out.print("Student ID: ");
        String id = sc.nextLine();

        for (String s : students) {
            if (s.startsWith(id + ",")) {
                System.out.println("Student ID already exists!");
                return;
            }
        }

        System.out.print("Name: ");
        String name = sc.nextLine();
        System.out.print("Email: ");
        String email = sc.nextLine();
        System.out.print("Program: ");
        String program = sc.nextLine();

        String record = id + "," + name + "," + email + "," + program + ",CREATED_AT=" + getTime();

        File_Manager.append(STUDENT_FILE, record);

        System.out.println("Student added successfully at " + getTime());
    }

    // ================= VIEW STUDENTS =================
    static void view_students() {
        List<String> students = File_Manager.read_all(STUDENT_FILE);

        if (students.isEmpty()) {
            System.out.println("No students found.");
            return;
        }

        for (String s : students) {
            System.out.println(s.replace(",", " | "));
        }
    }

    // ================= SEARCH STUDENT =================
    static void search_student() {

        System.out.print("Enter ID: ");
        String id = sc.nextLine();

        List<String> students = File_Manager.read_all(STUDENT_FILE);

        for (String s : students) {
            if (s.startsWith(id + ",")) {
                System.out.println("Found: " + s.replace(",", " | "));
                return;
            }
        }

        System.out.println("Student not found.");
    }

    // ================= UPDATE STUDENT =================
    static void update_student() {

        System.out.print("Enter ID to update: ");
        String id = sc.nextLine();

        List<String> students = File_Manager.read_all(STUDENT_FILE);
        List<String> updated = new ArrayList<>();

        boolean found = false;

        for (String s : students) {
            if (s.startsWith(id + ",")) {

                found = true;

                System.out.print("New Name: ");
                String name = sc.nextLine();
                System.out.print("New Email: ");
                String email = sc.nextLine();
                System.out.print("New Program: ");
                String program = sc.nextLine();

                String record = id + "," + name + "," + email + "," + program +
                        ",UPDATED_AT=" + getTime();

                updated.add(record);

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

        System.out.print("Enter ID to delete: ");
        String id = sc.nextLine();

        List<String> students = File_Manager.read_all(STUDENT_FILE);
        List<String> updated = new ArrayList<>();

        boolean found = false;

        for (String s : students) {
            if (s.startsWith(id + ",")) {
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
        System.out.println("Student deleted at " + getTime());
    }

    // ================= COURSE =================
    static void add_course() {

        System.out.print("Course Code: ");
        String code = sc.nextLine();
        System.out.print("Course Name: ");
        String name = sc.nextLine();
        System.out.print("Credit Hours: ");
        int credits = sc.nextInt();
        sc.nextLine();

        String record = code + "," + name + "," + credits + ",CREATED_AT=" + getTime();

        File_Manager.append(COURSE_FILE, record);

        System.out.println("Course added at " + getTime());
    }

    static void view_courses() {
        List<String> courses = File_Manager.read_all(COURSE_FILE);

        for (String c : courses) {
            System.out.println(c.replace(",", " | "));
        }
    }

    // ================= STATS =================
    static void show_stats() {

        List<String> students = File_Manager.read_all(STUDENT_FILE);
        List<String> courses = File_Manager.read_all(COURSE_FILE);

        System.out.println("Total Students: " + students.size());
        System.out.println("Total Courses: " + courses.size());
        System.out.println("Last Updated: " + getTime());
    }
}
