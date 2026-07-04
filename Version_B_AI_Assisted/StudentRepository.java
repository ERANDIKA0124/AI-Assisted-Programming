import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class StudentRepository {
    private final String fileName;

    public StudentRepository(String fileName) {
        this.fileName = fileName;
    }

    public List<StudentRecord> load() {
        List<StudentRecord> records = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (!line.trim().isEmpty()) {
                    records.add(StudentRecord.fromCsvLine(line));
                }
            }
        } catch (IOException e) {
            // No saved file on the first run is acceptable.
        } catch (RuntimeException e) {
            System.out.println("Warning: Some saved records could not be loaded: " + e.getMessage());
        }
        return records;
    }

    public void save(List<StudentRecord> records) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName))) {
            for (StudentRecord record : records) {
                writer.write(record.toCsvLine());
                writer.newLine();
            }
        }
    }
}
