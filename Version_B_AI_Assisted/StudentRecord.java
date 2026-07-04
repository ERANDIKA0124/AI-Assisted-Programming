public class StudentRecord {
    private final String studentId;
    private String studentName;
    private double assignmentMarks;
    private double examMarks;

    public StudentRecord(String studentId, String studentName, double assignmentMarks, double examMarks) {
        this.studentId = studentId;
        this.studentName = studentName;
        setAssignmentMarks(assignmentMarks);
        setExamMarks(examMarks);
    }

    public String getStudentId() {
        return studentId;
    }

    public String getStudentName() {
        return studentName;
    }

    public void setStudentName(String studentName) {
        if (studentName == null || studentName.trim().isEmpty()) {
            throw new IllegalArgumentException("Student name cannot be empty.");
        }
        this.studentName = studentName.trim();
    }

    public double getAssignmentMarks() {
        return assignmentMarks;
    }

    public void setAssignmentMarks(double assignmentMarks) {
        validateMarks(assignmentMarks);
        this.assignmentMarks = assignmentMarks;
    }

    public double getExamMarks() {
        return examMarks;
    }

    public void setExamMarks(double examMarks) {
        validateMarks(examMarks);
        this.examMarks = examMarks;
    }

    public double calculateWeightedAverage() {
        return (assignmentMarks * 0.4) + (examMarks * 0.6);
    }

    public String calculateGrade() {
        double average = calculateWeightedAverage();
        if (average >= 75) return "A";
        if (average >= 65) return "B";
        if (average >= 50) return "C";
        if (average >= 35) return "D";
        return "F";
    }

    public String toCsvLine() {
        return studentId + "," + escape(studentName) + "," + assignmentMarks + "," + examMarks;
    }

    public static StudentRecord fromCsvLine(String line) {
        String[] parts = line.split(",", -1);
        if (parts.length != 4) {
            throw new IllegalArgumentException("Invalid CSV line: " + line);
        }
        return new StudentRecord(parts[0], unescape(parts[1]), Double.parseDouble(parts[2]), Double.parseDouble(parts[3]));
    }

    private static void validateMarks(double marks) {
        if (marks < 0 || marks > 100) {
            throw new IllegalArgumentException("Marks must be between 0 and 100.");
        }
    }

    private static String escape(String text) {
        return text.replace(",", " ").trim();
    }

    private static String unescape(String text) {
        return text.trim();
    }
}
