import java.util.Scanner;

public class InputHelper {
    private final Scanner scanner;

    public InputHelper(Scanner scanner) {
        this.scanner = scanner;
    }

    public String readRequiredText(String message) {
        while (true) {
            System.out.print(message);
            String value = scanner.nextLine().trim();
            if (!value.isEmpty()) {
                return value;
            }
            System.out.println("This value cannot be empty.");
        }
    }

    public int readMenuChoice(String message) {
        while (true) {
            try {
                System.out.print(message);
                return Integer.parseInt(scanner.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.println("Please enter a valid menu number.");
            }
        }
    }

    public double readMark(String message) {
        while (true) {
            try {
                System.out.print(message);
                double mark = Double.parseDouble(scanner.nextLine().trim());
                if (mark >= 0 && mark <= 100) {
                    return mark;
                }
                System.out.println("Marks must be between 0 and 100.");
            } catch (NumberFormatException e) {
                System.out.println("Please enter a valid numeric mark.");
            }
        }
    }
}
