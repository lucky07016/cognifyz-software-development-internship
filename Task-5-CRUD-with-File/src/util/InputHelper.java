package util;

import java.util.Scanner;

/**
 * Utility class for reading and validating console input.
 *
 * <p>Holds a single shared {@link Scanner} over {@link System#in} and
 * exposes convenience methods so every other class receives clean,
 * validated input without duplicating try/catch or loop boilerplate.</p>
 *
 * <p>This is a static utility class and cannot be instantiated.</p>
 */
public class InputHelper {

    private static final Scanner scanner = new Scanner(System.in);

    private InputHelper() { /* utility — not instantiated */ }

    //String input

    /**
     * Reads a non-blank line of text from standard input, re-prompting
     * the user until they enter at least one non-whitespace character.
     *
     * @param prompt The message displayed to the user before each read attempt.
     *               Typically ends with {@code ": "} so the cursor appears right after it.
     * @return A trimmed, non-blank {@code String} guaranteed to have length &gt; 0.
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
     * Reads a single line of text from standard input without enforcing any
     * content constraint — a blank entry is perfectly valid.
     *
     * <p>Use this for optional update fields where pressing Enter means
     * "keep the current value".</p>
     *
     * @param prompt The message displayed to the user before the read.
     *               Pass an empty {@code String} ({@code ""}) to skip printing a prompt.
     * @return A trimmed {@code String} that may be empty ({@code ""}) if the
     *         user pressed Enter without typing anything.
     */
    public static String readOptionalString(String prompt) {
        System.out.print(prompt);
        return scanner.nextLine().trim();
    }

    //Integer input

    /**
     * Reads a positive integer (strictly &gt; 0) from standard input,
     * re-prompting the user on every invalid attempt until a valid value
     * is entered. Handles non-numeric input gracefully without throwing.
     *
     * @param prompt The message displayed to the user before each read attempt.
     *               Typically used to ask for an ID, e.g. {@code "Enter Task ID: "}.
     * @return A valid {@code int} value that is greater than zero.
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
     * Reads an integer that falls within the closed range [{@code min}, {@code max}]
     * from standard input. Re-prompts the user on every attempt that produces
     * a non-numeric value or a number outside the allowed range.
     *
     * <p>Primarily used to validate main-menu selections.</p>
     *
     * @param prompt The message displayed to the user before each read attempt.
     *               Should hint at the valid range, e.g. {@code "Enter your choice: "}.
     * @param min    The minimum accepted value (inclusive). Must be &lt;= {@code max}.
     * @param max    The maximum accepted value (inclusive). Must be &gt;= {@code min}.
     * @return A valid {@code int} value satisfying {@code min <= value <= max}.
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

    //Boolean input

    /**
     * Reads a yes/no confirmation from standard input, appending {@code " (y/n): "}
     * to the prompt automatically. Accepts {@code "y"} or {@code "yes"} as affirmative
     * and {@code "n"} or {@code "no"} as negative (case-insensitive).
     * Re-prompts the user on any other input.
     *
     * @param prompt The question to display before the {@code " (y/n): "} suffix,
     *               e.g. {@code "Are you sure you want to delete this task?"}.
     * @return {@code true} if the user confirmed with {@code "y"} or {@code "yes"};
     *         {@code false} if the user declined with {@code "n"} or {@code "no"}.
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

    //Lifecycle

    /**
     * Closes the shared {@link Scanner} and releases the underlying
     * {@link System#in} stream. Should be called exactly once when the
     * application is about to exit to avoid resource leaks.
     *
     * <p>After this call, any further use of {@code InputHelper} read
     * methods will throw an {@link IllegalStateException}.</p>
     */
    public static void close() {
        scanner.close();
    }
}