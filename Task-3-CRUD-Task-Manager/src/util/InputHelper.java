package util;

import java.util.Scanner;

/**
 * Utility class for reading and validating console input.
 * Provides a single shared Scanner and convenience methods so that
 * every other class gets clean, validated input without boilerplate.
 */
public class InputHelper {

    private static final Scanner scanner = new Scanner(System.in);

    private InputHelper() { /* utility — not instantiated */ }

    /**
     * Reads a non-blank line of text, re-prompting until one is entered.
     *
     * @param prompt Message shown to the user before reading.
     * @return Trimmed, non-blank string.
     */
    public static String readNonBlankString(String prompt) {
        String input;
        while (true) {
            System.out.print(prompt);
            input = scanner.nextLine().trim();
            if (!input.isEmpty()) {
                return input;
            }
            System.out.println("  ✗ Input cannot be blank. Please try again.");
        }
    }

    /**
     * Reads an optional line of text (blank is allowed — returns empty string).
     *
     * @param prompt Message shown to the user before reading.
     * @return Trimmed string (may be empty).
     */
    public static String readOptionalString(String prompt) {
        System.out.print(prompt);
        return scanner.nextLine().trim();
    }

    /**
     * Reads a positive integer, re-prompting on invalid input.
     *
     * @param prompt Message shown to the user before reading.
     * @return Valid positive integer.
     */
    public static int readPositiveInt(String prompt) {
        while (true) {
            System.out.print(prompt);
            String line = scanner.nextLine().trim();
            try {
                int value = Integer.parseInt(line);
                if (value > 0) {
                    return value;
                }
                System.out.println("  ✗ Please enter a number greater than 0.");
            } catch (NumberFormatException e) {
                System.out.println("  ✗ Invalid number. Please try again.");
            }
        }
    }

    /**
     * Reads an integer within [min, max], re-prompting on invalid input.
     *
     * @param prompt Message shown to the user before reading.
     * @param min    Minimum allowed value (inclusive).
     * @param max    Maximum allowed value (inclusive).
     * @return Valid integer within the given range.
     */
    public static int readIntInRange(String prompt, int min, int max) {
        while (true) {
            System.out.print(prompt);
            String line = scanner.nextLine().trim();
            try {
                int value = Integer.parseInt(line);
                if (value >= min && value <= max) {
                    return value;
                }
                System.out.printf("  ✗ Please enter a number between %d and %d.%n", min, max);
            } catch (NumberFormatException e) {
                System.out.println("  ✗ Invalid input. Please enter a number.");
            }
        }
    }

    /**
     * Reads a yes/no answer.
     *
     * @param prompt Message shown to the user before reading.
     * @return true if the user answered "y" / "yes", false otherwise.
     */
    public static boolean readYesNo(String prompt) {
        while (true) {
            System.out.print(prompt + " (y/n): ");
            String answer = scanner.nextLine().trim().toLowerCase();
            if (answer.equals("y") || answer.equals("yes")) { return true;  }
            if (answer.equals("n") || answer.equals("no"))  { return false; }
            System.out.println("  ✗ Please answer 'y' or 'n'.");
        }
    }

    public static void close() {
        scanner.close();
    }
}