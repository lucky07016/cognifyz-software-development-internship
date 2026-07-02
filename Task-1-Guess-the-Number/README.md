# Task 1 — Guess the Number

A simple console-based "Guess the Number" game written in Java.

## How it works

1. The program generates a random number between **1 and 100**.
2. The player repeatedly enters guesses.
3. After each guess, the program tells the player whether their guess was:
   - **Too Low**
   - **Too High**
   - Correct — in which case it prints **Congratulations** along with the
     number of attempts taken.
4. After a round ends, the player is asked if they want to play again.
   - Answering `y` / `yes` starts a new round with a freshly generated number.
   - Answering `n` / `no` exits the program.

## Project Structure

```
Task-1-Guess-the-Number/
│
├── src/
│   ├── Main.java                 # Entry point
│   ├── Game.java                 # Core game loop and logic
│   └── RandomNumberGenerator.java# Random number generation utility
│
├── README.md
└── .gitignore
```

## How to Run

From the project root:

```bash
cd src
javac *.java
java Main
```

Then follow the on-screen prompts to enter your guesses.

## Example

```
=== Guess the Number ===

I'm thinking of a number between 1 and 100.
Enter your guess: 50
Too High
Enter your guess: 25
Too Low
Enter your guess: 37
Congratulations
Attempts: 3

Do you want to play again? (y/n): n
Thanks for playing! Goodbye.
```