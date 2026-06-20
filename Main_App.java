import java.io.*;
import java.util.*;

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

    public String to_file_string() {

        return student_id + "," + name + "," + email + "," + program;

    }

    @Override
    public void display_info() {

        System.out.println("ID: " + student_id + " | NAME: " + name + " | EMAIL: " + email + " | PROGRAM:" + program);
    
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

    public void display_course() {

        System.out.println(course_code + " | " + course_name + " | Credits: " + credit_hours);
    
    }
}

// ======================= FILE MANAGER (TEXT BASED) =======================
class File_Manager {

    public static void append(String filename, String data) {
        try (FileWriter fw = new FileWriter(filename, true)) {
            fw.write(data + "\n");
        } catch (IOException e) {
            System.out.println("Error writing to file");
        }
    }

    public static void read(String filename) {
        File file = new File(filename);

        if (!file.exists()) {
            System.out.println("No data found.");
            return;
        }

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                System.out.println(line.replace(",", " | "));
            }
        } catch (IOException e) {
            System.out.println("Error reading file");
        }
    }
}

// ======================= MAIN APP =======================
public class Main_App {

    static Scanner sc = new Scanner(System.in);

    static final String STUDENT_FILE = "students.txt";
    static final String COURSE_FILE  = "courses.txt";

    public static void main(String[] args) {

        while (true) {
            System.out.println("\n*********************** Menu ***********************\n");
            System.out.println("1. Add Student's record");
            System.out.println("2. View Students records");
            System.out.println("3. Add Course");
            System.out.println("4. View all Courses");
            System.out.println("5. Delete Student's record by name");
            System.out.println("6. Exit\n");
            System.out.print("Your Choice: ");

            int choice = sc.nextInt();
            sc.nextLine(); // clear buffer

            switch (choice) {
                case 1 -> add_student();
                case 2 -> view_students();
                case 3 -> add_course();
                case 4 -> view_courses();
                case 5 -> delete_student_by_name();
                case 6 -> {
                    System.out.println("\n========================= Exiting... =======================\n");
                    System.exit(0);
                }
                default -> System.out.println("Invalid choice!");
            }
        }
    }


    // to delete a student's record from a file
    static void delete_student_by_name() {

    System.out.print("Enter student name to delete: ");
    String nameToDelete = sc.nextLine().toLowerCase();

    File file = new File(STUDENT_FILE);

    if (!file.exists()) {
        System.out.println("No student data found.");
        return;
    }

    List<String> updated_list = new ArrayList<>();
    boolean found = false;

    try (BufferedReader br = new BufferedReader(new FileReader(file))) {
        String line;

        while ((line = br.readLine()) != null) {
            String[] parts = line.split(",");

            if (parts.length >= 2) {
                String studentName = parts[1].toLowerCase();

                if (studentName.equals(nameToDelete)) {
                    found = true;  // Skip adding this line (i.e., delete)
                    continue;
                }
            }
            updated_list.add(line);
        }
    } catch (IOException e) {
        System.out.println("Error reading file.");
        return;
    }

    if (!found) {
        System.out.println("No student found with that name.");
        return;
    }

    // Rewrite file with updated list
    try (PrintWriter pw = new PrintWriter(new FileWriter(file))) {
        for (String s : updated_list) {
            pw.println(s);
        }
    } catch (IOException e) {
        System.out.println("Error updating file.");
        return;
    }

    System.out.println("Student deleted successfully.");
}


// to add a student's record in a file
    static void add_student() {
        System.out.print("Student ID: ");
        String id = sc.nextLine();
        System.out.print("Name: ");
        String name = sc.nextLine();
        System.out.print("Email: ");
        String email = sc.nextLine();
        System.out.print("Program: ");
        String program = sc.nextLine();

        Student s = new Student(id, name, email, program);
        File_Manager.append(STUDENT_FILE, s.to_file_string());

        System.out.println("Student saved successfully.");
    }

    static void view_students() {
        System.out.println("\n################ Students ###############");
        File_Manager.read(STUDENT_FILE);
    }

    static void add_course() {
        System.out.print("Course Code: ");
        String code = sc.nextLine();
        System.out.print("Course Name: ");
        String name = sc.nextLine();
        System.out.print("Credit Hours: ");
        int credits = sc.nextInt();
        sc.nextLine();

        Course c = new Course(code, name, credits);
        File_Manager.append(COURSE_FILE, c.to_file_string());

        System.out.println("Course saved successfully.");
    }

    static void view_courses() {
        System.out.println("\n############### Courses ##################\n");
        File_Manager.read(COURSE_FILE);
    }
}
