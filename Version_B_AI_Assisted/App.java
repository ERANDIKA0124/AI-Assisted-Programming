import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;

/*
 * Version B - AI-Assisted Programming Approach
 * ------------------------------------------------------------
 * This version is more modular than Version A. It separates the
 * responsibilities into model, service, repository, input helper,
 * and application classes. This reflects the type of structure that
 * was improved after using AI suggestions for refactoring and error
 * handling.
 */
public class App {
    private static final String FILE_NAME = "students_ai_assisted.csv";

    private final StudentRepository repository;
    private final StudentService service;
    private final InputHelper input;

    public App(StudentRepository repository, StudentService service, InputHelper input) {
        this.repository = repository;
        this.service = service;
        this.input = input;
    }

    public static void main(String[] args) {
        StudentRepository repository = new StudentRepository(FILE_NAME);
        StudentService service = new StudentService(repository.load());
        InputHelper input = new InputHelper(new Scanner(System.in));
        new App(repository, service, input).run();
    }

    public void run() {
        boolean running = true;
        while (running) {
            printMenu();
            int choice = input.readMenuChoice("Enter your choice: ");
            try {
                switch (choice) {
                    case 1 -> addStudent();
                    case 2 -> viewStudents();
                    case 3 -> searchStudent();
                    case 4 -> updateMarks();
                    case 5 -> deleteStudent();
                    case 6 -> showSummary();
                    case 7 -> {
                        saveAndExit();
                        running = false;
                    }
                    default -> System.out.println("Invalid choice. Please select a number from 1 to 7.");
                }
            } catch (IllegalArgumentException e) {
                System.out.println("Operation failed: " + e.getMessage());
            }
        }
    }

    private void printMenu() {
        System.out.println("\n===== Student Performance Management System - AI Assisted Version =====");
        System.out.println("1. Add Student");
        System.out.println("2. View All Students");
        System.out.println("3. Search Student by ID");
        System.out.println("4. Update Student Marks");
        System.out.println("5. Delete Student");
        System.out.println("6. Show Class Summary");
        System.out.println("7. Save and Exit");
    }

    private void addStudent() {
        String id = input.readRequiredText("Student ID: ");
        String name = input.readRequiredText("Student Name: ");
        double assignmentMarks = input.readMark("Assignment Marks (0-100): ");
        double examMarks = input.readMark("Exam Marks (0-100): ");
        service.addStudent(new StudentRecord(id, name, assignmentMarks, examMarks));
        System.out.println("Student added successfully.");
    }

    private void viewStudents() {
        List<StudentRecord> records = service.getAllStudents();
        if (records.isEmpty()) {
            System.out.println("No student records found.");
            return;
        }
        printHeader();
        for (StudentRecord record : records) {
            printRecord(record);
        }
    }

    private void searchStudent() {
        String id = input.readRequiredText("Enter Student ID to search: ");
        Optional<StudentRecord> record = service.findById(id);
        if (record.isPresent()) {
            printHeader();
            printRecord(record.get());
        } else {
            System.out.println("Student not found.");
        }
    }

    private void updateMarks() {
        String id = input.readRequiredText("Enter Student ID to update marks: ");
        double assignmentMarks = input.readMark("New Assignment Marks (0-100): ");
        double examMarks = input.readMark("New Exam Marks (0-100): ");
        service.updateMarks(id, assignmentMarks, examMarks);
        System.out.println("Marks updated successfully.");
    }

    private void deleteStudent() {
        String id = input.readRequiredText("Enter Student ID to delete: ");
        service.deleteStudent(id);
        System.out.println("Student deleted successfully.");
    }

    private void showSummary() {
        StatisticsSummary summary = service.calculateSummary();
        if (summary.getTotalStudents() == 0) {
            System.out.println("No records available for summary.");
            return;
        }
        System.out.println("\n===== Class Summary =====");
        System.out.println("Number of students: " + summary.getTotalStudents());
        System.out.printf("Class average: %.2f%n", summary.getClassAverage());
        System.out.printf("Highest average: %.2f%n", summary.getHighestAverage());
        System.out.printf("Lowest average: %.2f%n", summary.getLowestAverage());
        System.out.printf("Pass rate: %.2f%%%n", summary.getPassRate());
    }

    private void saveAndExit() {
        try {
            repository.save(service.exportRecords());
            System.out.println("Data saved. Program closed.");
        } catch (IOException e) {
            System.out.println("Could not save data: " + e.getMessage());
        }
    }

    private void printHeader() {
        System.out.printf("%-12s %-25s %-12s %-12s %-10s %-8s%n",
                "ID", "Name", "Assignment", "Exam", "Average", "Grade");
        System.out.println("----------------------------------------------------------------------------");
    }

    private void printRecord(StudentRecord record) {
        System.out.printf("%-12s %-25s %-12.2f %-12.2f %-10.2f %-8s%n",
                record.getStudentId(), record.getStudentName(), record.getAssignmentMarks(), record.getExamMarks(),
                record.calculateWeightedAverage(), record.calculateGrade());
    }
}
