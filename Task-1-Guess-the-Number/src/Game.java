import java.util.Scanner;

/**
 * Contains the core "Guess the Number" game logic, following the
 * pseudocode:
 *
 * START
 * Generate a random number
 * Set attempts = 0
 * Repeat
 *     Ask user for a guess
 *     attempts++
 *     If guess < number -> "Too Low"
 *     Else if guess > number -> "Too High"
 *     Else -> "Congratulations" + attempts
 * Ask if user wants to play again
 * If yes -> restart
 * Else -> exit
 * END
 */
public class Game {

    private static final int MIN_NUMBER = 1;
    private static final int MAX_NUMBER = 100;

    private final Scanner scanner;
    private final RandomNumberGenerator generator;

    public Game() {
        this.scanner = new Scanner(System.in);
        this.generator = new RandomNumberGenerator(MIN_NUMBER, MAX_NUMBER);
    }

    /**
     * Runs the game, including the "play again" loop.
     */
    public void start() {
        boolean playAgain = true;

        System.out.println("=== Guess the Number ===");

        while (playAgain) {
            playRound();
            playAgain = askPlayAgain();
        }

        System.out.println("Thanks for playing! Goodbye.");
        scanner.close();
    }

    /**
     * Plays a single round of the game: generates a number and
     * repeatedly asks the user to guess it until they succeed.
     */
    private void playRound() {
        int numberToGuess = generator.generate();
        int attempts = 0;

        System.out.println("\nI'm thinking of a number between "
                + generator.getMin() + " and " + generator.getMax() + ".");

        while (true) {
            int guess = readGuess();
            attempts++;

            if (guess < numberToGuess) {
                System.out.println("Too Low");
            } else if (guess > numberToGuess) {
                System.out.println("Too High");
            } else {
                System.out.println("Congratulations");
                System.out.println("Attempts: " + attempts);
                break;
            }
        }
    }

    /**
     * Reads a valid integer guess from the user, re-prompting on
     * invalid input.
     */
    private int readGuess() {
        while (true) {
            System.out.print("Enter your guess: ");
            String input = scanner.nextLine().trim();
            try {
                return Integer.parseInt(input);
            } catch (NumberFormatException e) {
                System.out.println("Please enter a valid whole number.");
            }
        }
    }

    /**
     * Asks the user if they want to play again.
     *
     * @return true if the user wants to play again, false otherwise.
     */
    private boolean askPlayAgain() {
        while (true) {
            System.out.print("\nDo you want to play again? (y/n): ");
            String answer = scanner.nextLine().trim().toLowerCase();

            if (answer.equals("y") || answer.equals("yes")) {
                return true;
            } else if (answer.equals("n") || answer.equals("no")) {
                return false;
            } else {
                System.out.println("Please answer 'y' or 'n'.");
            }
        }
    }
}