package menu;

import model.Task;
import model.Task.Status;
import service.TaskService;
import util.InputHelper;

import java.util.List;
import java.util.Optional;

/**
 * Drives the console UI following the flowchart:
 *
 *   Start → Display Menu → Read User Choice
 *        → Add | View | Update | Delete → TaskService
 *        → Back to Menu  →  Exit
 */
public class Menu {

    private static final int OPTION_ADD    = 1;
    private static final int OPTION_VIEW   = 2;
    private static final int OPTION_UPDATE = 3;
    private static final int OPTION_DELETE = 4;
    private static final int OPTION_EXIT   = 5;

    private final TaskService taskService;

    public Menu(TaskService taskService) {
        this.taskService = taskService;
    }
    /**
     * Starts the menu loop — displays the menu, reads the choice, and
     * dispatches to the appropriate handler until the user exits.
     */
    public void run() {
        printBanner();
        boolean running = true;
        while (running) {
            displayMenu();
            int choice = InputHelper.readIntInRange("  Enter your choice: ", OPTION_ADD, OPTION_EXIT);
            System.out.println();
            switch (choice) {
                case OPTION_ADD    -> handleAdd();
                case OPTION_VIEW   -> handleView();
                case OPTION_UPDATE -> handleUpdate();
                case OPTION_DELETE -> handleDelete();
                case OPTION_EXIT   -> running = confirmExit();
            }
        }
        InputHelper.close();
        System.out.println("\n  Goodbye! 👋\n");
    }

    //Menu display 

    private void printBanner() {
        System.out.println();
        System.out.println("  |═════════════════════════════|");
        System.out.println("  |     CRUD Task Manager       |");
        System.out.println("  |═════════════════════════════|");
    }

    private void displayMenu() {
        System.out.println();
        System.out.println("  |──────────────────────────────|");
        System.out.println("  |          M A I N  M E N U   |");
        System.out.println("  |──────────────────────────────|");
        System.out.printf ("  |  %d. Add Task                 |%n", OPTION_ADD);
        System.out.printf ("  |  %d. View All Tasks           |%n", OPTION_VIEW);
        System.out.printf ("  |  %d. Update Task              |%n", OPTION_UPDATE);
        System.out.printf ("  │  %d. Delete Task              │%n", OPTION_DELETE);
        System.out.printf ("  │  %d. Exit                     │%n", OPTION_EXIT);
        System.out.println("  └──────────────────────────────┘");
    }

    //CREATE

    private void handleAdd() {
        System.out.println("  ── Add New Task ──────────────────────────");
        String title       = InputHelper.readNonBlankString("  Title       : ");
        String description = InputHelper.readNonBlankString("  Description : ");

        Task created = taskService.addTask(title, description);
        System.out.println("\n  ✓ Task added successfully!\n");
        System.out.println(created);
        pauseForUser();
    }

    //READ 

    private void handleView() {
        System.out.println("  ── All Tasks ─────────────────────────────");
        List<Task> tasks = taskService.getAllTasks();

        if (tasks.isEmpty()) {
            System.out.println("  (No tasks found. Add one first!)");
        } else {
            tasks.forEach(t -> { System.out.println(); System.out.println(t); });
            System.out.printf("%n  Total: %d task(s)%n", tasks.size());
        }
        pauseForUser();
    }

    //UPDATE

    private void handleUpdate() {
        System.out.println("  ── Update Task ───────────────────────────");

        if (taskService.isEmpty()) {
            System.out.println("  (No tasks available to update.)");
            pauseForUser();
            return;
        }

        int id = InputHelper.readPositiveInt("  Enter Task ID to update: ");
        Optional<Task> opt = taskService.findById(id);

        if (opt.isEmpty()) {
            System.out.printf("  ✗ No task found with ID %d.%n", id);
            pauseForUser();
            return;
        }

        System.out.println("\n  Current task:");
        System.out.println(opt.get());

        System.out.println("\n  Leave a field blank to keep the current value.");
        String newTitle = InputHelper.readOptionalString("  New Title       : ");
        String newDesc  = InputHelper.readOptionalString("  New Description : ");
        Status newStatus = readStatusChoice();

        boolean updated = taskService.updateTask(
            id,
            newTitle.isEmpty()  ? null : newTitle,
            newDesc.isEmpty()   ? null : newDesc,
            newStatus
        );

        if (updated) {
            System.out.println("\n  ✓ Task updated successfully!\n");
            taskService.findById(id).ifPresent(System.out::println);
        } else {
            System.out.println("  ✗ Update failed unexpectedly.");
        }
        pauseForUser();
    }

    /**
     * Shows the available statuses and lets the user pick one,
     * or press Enter to skip.
     *
     * @return the chosen Status, or null if the user skipped.
     */
    private Status readStatusChoice() {
        Status[] values = Status.values();
        System.out.println("  New Status (press Enter to keep current):");
        for (int i = 0; i < values.length; i++) {
            System.out.printf("    %d. %s%n", i + 1, values[i]);
        }
        System.out.print("  Choice [1-" + values.length + "]: ");

        // Reuse InputHelper's optional read via scanner trick:
        String line = util.InputHelper.readOptionalString("");
        if (line.isEmpty()) {
            return null;
        }
        try {
            int index = Integer.parseInt(line) - 1;
            if (index >= 0 && index < values.length) {
                return values[index];
            }
        } catch (NumberFormatException ignored) { }
        System.out.println("  ✗ Invalid choice — status unchanged.");
        return null;
    }

    // DELETE

    private void handleDelete() {
        System.out.println("  ── Delete Task ───────────────────────────");

        if (taskService.isEmpty()) {
            System.out.println("  (No tasks available to delete.)");
            pauseForUser();
            return;
        }

        int id = InputHelper.readPositiveInt("  Enter Task ID to delete: ");
        Optional<Task> opt = taskService.findById(id);

        if (opt.isEmpty()) {
            System.out.printf("  ✗ No task found with ID %d.%n", id);
            pauseForUser();
            return;
        }

        System.out.println("\n  Task to delete:");
        System.out.println(opt.get());

        boolean confirmed = InputHelper.readYesNo("\n  Are you sure you want to delete this task?");
        if (confirmed) {
            taskService.deleteTask(id);
            System.out.printf("  ✓ Task %d deleted successfully.%n", id);
        } else {
            System.out.println("  Deletion cancelled.");
        }
        pauseForUser();
    }

    // Exit

    /**
     * Confirms exit intent.
     *
     * @return false (stop the loop) if the user confirms, true (keep running) otherwise.
     */
    private boolean confirmExit() {
        boolean yes = InputHelper.readYesNo("  Are you sure you want to exit?");
        return !yes; // returning false stops the loop
    }

    //Utilities

    private void pauseForUser() {
        System.out.print("\n  Press Enter to return to the menu...");
        InputHelper.readOptionalString("");
    }
}