import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;


public class StudentPerformanceTraditional {
    private static final String FILE_NAME = "students_traditional.csv";
    private static final Scanner scanner = new Scanner(System.in);
    private static final List<Student> students = new ArrayList<>();

    public static void main(String[] args) {
        loadFromFile();

        boolean running = true;
        while (running) {
            printMenu();
            int choice = readInt("Enter your choice: ");

            switch (choice) {
                case 1:
                    addStudent();
                    break;
                case 2:
                    viewStudents();
                    break;
                case 3:
                    searchStudent();
                    break;
                case 4:
                    updateMarks();
                    break;
                case 5:
                    deleteStudent();
                    break;
                case 6:
                    showSummary();
                    break;
                case 7:
                    saveToFile();
                    running = false;
                    System.out.println("Data saved. Program closed.");
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    private static void printMenu() {
        System.out.println("\n===== Student Performance Management System =====");
        System.out.println("1. Add Student");
        System.out.println("2. View All Students");
        System.out.println("3. Search Student by ID");
        System.out.println("4. Update Student Marks");
        System.out.println("5. Delete Student");
        System.out.println("6. Show Class Summary");
        System.out.println("7. Save and Exit");
    }

    private static void addStudent() {
        String id = readText("Student ID: ");
        if (findStudentById(id) != null) {
            System.out.println("A student with this ID already exists.");
            return;
        }

        String name = readText("Student Name: ");
        double assignment = readMark("Assignment Marks (0-100): ");
        double exam = readMark("Exam Marks (0-100): ");

        students.add(new Student(id, name, assignment, exam));
        System.out.println("Student added successfully.");
    }

    private static void viewStudents() {
        if (students.isEmpty()) {
            System.out.println("No student records found.");
            return;
        }

        System.out.printf("%-12s %-25s %-12s %-12s %-10s %-8s%n",
                "ID", "Name", "Assignment", "Exam", "Average", "Grade");
        System.out.println("----------------------------------------------------------------------------");
        for (Student student : students) {
            printStudent(student);
        }
    }

    private static void searchStudent() {
        String id = readText("Enter Student ID to search: ");
        Student student = findStudentById(id);
        if (student == null) {
            System.out.println("Student not found.");
        } else {
            System.out.printf("%-12s %-25s %-12s %-12s %-10s %-8s%n",
                    "ID", "Name", "Assignment", "Exam", "Average", "Grade");
            System.out.println("----------------------------------------------------------------------------");
            printStudent(student);
        }
    }

    private static void updateMarks() {
        String id = readText("Enter Student ID to update marks: ");
        Student student = findStudentById(id);
        if (student == null) {
            System.out.println("Student not found.");
            return;
        }

        student.assignmentMarks = readMark("New Assignment Marks (0-100): ");
        student.examMarks = readMark("New Exam Marks (0-100): ");
        System.out.println("Marks updated successfully.");
    }

    private static void deleteStudent() {
        String id = readText("Enter Student ID to delete: ");
        Student student = findStudentById(id);
        if (student == null) {
            System.out.println("Student not found.");
            return;
        }

        students.remove(student);
        System.out.println("Student deleted successfully.");
    }

    private static void showSummary() {
        if (students.isEmpty()) {
            System.out.println("No records available for summary.");
            return;
        }

        double total = 0;
        double highest = students.get(0).getAverage();
        double lowest = students.get(0).getAverage();

        for (Student student : students) {
            double average = student.getAverage();
            total += average;
            if (average > highest) {
                highest = average;
            }
            if (average < lowest) {
                lowest = average;
            }
        }

        System.out.println("\n===== Class Summary =====");
        System.out.println("Number of students: " + students.size());
        System.out.printf("Class average: %.2f%n", total / students.size());
        System.out.printf("Highest average: %.2f%n", highest);
        System.out.printf("Lowest average: %.2f%n", lowest);
    }

    private static Student findStudentById(String id) {
        for (Student student : students) {
            if (student.id.equalsIgnoreCase(id)) {
                return student;
            }
        }
        return null;
    }

    private static void printStudent(Student student) {
        System.out.printf("%-12s %-25s %-12.2f %-12.2f %-10.2f %-8s%n",
                student.id, student.name, student.assignmentMarks, student.examMarks,
                student.getAverage(), student.getGrade());
    }

    private static int readInt(String message) {
        while (true) {
            try {
                System.out.print(message);
                return Integer.parseInt(scanner.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.println("Please enter a valid number.");
            }
        }
    }

    private static double readMark(String message) {
        while (true) {
            try {
                System.out.print(message);
                double mark = Double.parseDouble(scanner.nextLine().trim());
                if (mark >= 0 && mark <= 100) {
                    return mark;
                }
                System.out.println("Marks must be between 0 and 100.");
            } catch (NumberFormatException e) {
                System.out.println("Please enter a valid mark.");
            }
        }
    }

    private static String readText(String message) {
        while (true) {
            System.out.print(message);
            String value = scanner.nextLine().trim();
            if (!value.isEmpty()) {
                return value;
            }
            System.out.println("This field cannot be empty.");
        }
    }

    private static void saveToFile() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_NAME))) {
            for (Student student : students) {
                writer.write(student.id + "," + student.name + "," + student.assignmentMarks + "," + student.examMarks);
                writer.newLine();
            }
        } catch (IOException e) {
            System.out.println("Error while saving data: " + e.getMessage());
        }
    }

    private static void loadFromFile() {
        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_NAME))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 4) {
                    students.add(new Student(parts[0], parts[1], Double.parseDouble(parts[2]), Double.parseDouble(parts[3])));
                }
            }
        } catch (IOException e) {
            // It is normal to reach here the first time because the CSV file may not exist yet.
        } catch (NumberFormatException e) {
            System.out.println("Some saved marks were not valid. Please check the CSV file.");
        }
    }

    static class Student {
        String id;
        String name;
        double assignmentMarks;
        double examMarks;

        Student(String id, String name, double assignmentMarks, double examMarks) {
            this.id = id;
            this.name = name;
            this.assignmentMarks = assignmentMarks;
            this.examMarks = examMarks;
        }

        double getAverage() {
            return (assignmentMarks * 0.4) + (examMarks * 0.6);
        }

        String getGrade() {
            double average = getAverage();
            if (average >= 75) {
                return "A";
            } else if (average >= 65) {
                return "B";
            } else if (average >= 50) {
                return "C";
            } else if (average >= 35) {
                return "D";
            } else {
                return "F";
            }
        }
    }
}
