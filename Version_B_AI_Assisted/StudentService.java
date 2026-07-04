import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

public class StudentService {
    private final List<StudentRecord> records;

    public StudentService(List<StudentRecord> records) {
        this.records = new ArrayList<>(records);
    }

    public void addStudent(StudentRecord record) {
        if (findById(record.getStudentId()).isPresent()) {
            throw new IllegalArgumentException("Student ID already exists.");
        }
        records.add(record);
    }

    public List<StudentRecord> getAllStudents() {
        records.sort(Comparator.comparing(StudentRecord::getStudentId));
        return new ArrayList<>(records);
    }

    public Optional<StudentRecord> findById(String studentId) {
        return records.stream()
                .filter(record -> record.getStudentId().equalsIgnoreCase(studentId))
                .findFirst();
    }

    public void updateMarks(String studentId, double assignmentMarks, double examMarks) {
        StudentRecord record = findById(studentId)
                .orElseThrow(() -> new IllegalArgumentException("Student not found."));
        record.setAssignmentMarks(assignmentMarks);
        record.setExamMarks(examMarks);
    }

    public void deleteStudent(String studentId) {
        boolean removed = records.removeIf(record -> record.getStudentId().equalsIgnoreCase(studentId));
        if (!removed) {
            throw new IllegalArgumentException("Student not found.");
        }
    }

    public StatisticsSummary calculateSummary() {
        if (records.isEmpty()) {
            return new StatisticsSummary(0, 0, 0, 0, 0);
        }

        double total = 0;
        double highest = records.get(0).calculateWeightedAverage();
        double lowest = records.get(0).calculateWeightedAverage();
        int passCount = 0;

        for (StudentRecord record : records) {
            double average = record.calculateWeightedAverage();
            total += average;
            highest = Math.max(highest, average);
            lowest = Math.min(lowest, average);
            if (average >= 50) {
                passCount++;
            }
        }

        double classAverage = total / records.size();
        double passRate = (passCount * 100.0) / records.size();
        return new StatisticsSummary(records.size(), classAverage, highest, lowest, passRate);
    }

    public List<StudentRecord> exportRecords() {
        return new ArrayList<>(records);
    }
}
