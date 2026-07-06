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
 * <pre>
 *   Display Menu → Read User Choice
 *        │
 *   |────|────|────────|────────|
 *  Add  View  Update  Delete   Exit
 *   │         │        │
 * ArrayList < mutate > Save to File
 *                       │
 *                  Back to Menu
 * </pre>
 *
 * <p>The Menu itself never touches the file — it calls {@link TaskService},
 * which automatically persists every Add / Update / Delete to disk.</p>
 */
public class Menu {

    private static final int OPTION_ADD    = 1;
    private static final int OPTION_VIEW   = 2;
    private static final int OPTION_UPDATE = 3;
    private static final int OPTION_DELETE = 4;
    private static final int OPTION_EXIT   = 5;

    private final TaskService taskService;

    /**
     * Creates a new {@code Menu} backed by the given {@link TaskService}.
     *
     * @param taskService The service layer used for all CRUD operations.
     */
    public Menu(TaskService taskService) {
        this.taskService = taskService;
    }

    public void run() {
        printBanner();
        boolean running = true;

        while (running) {
            displayMenu();
            int choice = InputHelper.readIntInRange("  Enter your choice: ", OPTION_ADD, OPTION_EXIT);
            System.out.println();

            switch (choice) {
                case OPTION_ADD -> handleAdd();
                case OPTION_VIEW -> handleView();
                case OPTION_UPDATE -> handleUpdate();
                case OPTION_DELETE -> handleDelete();
                case OPTION_EXIT -> running = !InputHelper.readYesNo("  Are you sure you want to exit?");
            }
        }

        InputHelper.close();
        System.out.println("\n  Goodbye! 👋\n");
    }

    private void printBanner() {
        System.out.println();
        System.out.println("  +----------------------------------+");
        System.out.println("  |   CRUD Task Manager with File    |");
        System.out.println("  +----------------------------------+");
    }

    private void displayMenu() {
        System.out.println();
        System.out.println("  1) Add Task");
        System.out.println("  2) View All Tasks");
        System.out.println("  3) Update Task");
        System.out.println("  4) Delete Task");
        System.out.println("  5) Exit");
    }

    //ADD 

    private void handleAdd() {
        System.out.println("  -- Add New Task ------------------------");
        String title       = InputHelper.readNonBlankString("  Title       : ");
        String description = InputHelper.readNonBlankString("  Description : ");

        Task created = taskService.addTask(title, description);
        System.out.println("\n  ✓ Task added and saved to file!\n");
        System.out.println(created);
        pause();
    }

    //VIEW 

    private void handleView() {
        System.out.println("  -- All Tasks ---------------------------");
        List<Task> tasks = taskService.getAllTasks();

        if (tasks.isEmpty()) {
            System.out.println("  (No tasks found. Add one first!)");
        } else {
            tasks.forEach(t -> { System.out.println(); System.out.println(t); });
            System.out.printf("%n  Total: %d task(s)%n", tasks.size());
        }
        pause();
    }

    //UPDATE 

    private void handleUpdate() {
        System.out.println("  -- Update Task --");

        if (taskService.isEmpty()) {
            System.out.println("  (No tasks available to update.)");
            pause();
            return;
        }

        int id = InputHelper.readPositiveInt("  Enter task ID to update: ");
        Optional<Task> taskOpt = taskService.findById(id);

        if (taskOpt.isEmpty()) {
            System.out.printf("  ✗ No task found with ID %d.%n", id);
            pause();
            return;
        }

        Task task = taskOpt.get();
        System.out.println("\n  Current task:");
        System.out.println(task);
        System.out.println("\n  Leave a field blank to keep the current value.");

        String newTitle = InputHelper.readOptionalString("  New title       : ");
        String newDescription = InputHelper.readOptionalString("  New description : ");
        Status newStatus = readStatusChoice();

        boolean updated = taskService.updateTask(
            id,
            newTitle.isEmpty() ? null : newTitle,
            newDescription.isEmpty() ? null : newDescription,
            newStatus
        );

        if (updated) {
            System.out.println("\n  ✓ Task updated and saved to file!\n");
            taskService.findById(id).ifPresent(System.out::println);
        } else {
            System.out.println("  ✗ Update failed.");
        }
        pause();
    }

    /**
     * Presents the available {@link Status} values and returns the user's
     * selection, or {@code null} if the user presses Enter to skip.
     *
     * @return The chosen {@link Status}, or {@code null} to leave it unchanged.
     */
    private Status readStatusChoice() {
        Status[] values = Status.values();
        System.out.println("  New status (press Enter to keep current):");
        for (int i = 0; i < values.length; i++) {
            System.out.printf("    %d) %s%n", i + 1, values[i]);
        }
        String line = InputHelper.readOptionalString("  Choice [1-" + values.length + "]: ");
        if (line.isEmpty()) {
            return null;
        }
        try {
            int index = Integer.parseInt(line) - 1;
            if (index >= 0 && index < values.length) {
                return values[index];
            }
        } catch (NumberFormatException ignored) { }
        System.out.println("  ✗ Invalid status selection. Keeping current status.");
        return null;
    }

    //DELETE 

    private void handleDelete() {
        System.out.println("  -- Delete Task --");

        if (taskService.isEmpty()) {
            System.out.println("  (No tasks available to delete.)");
            pause();
            return;
        }

        int id = InputHelper.readPositiveInt("  Enter task ID to delete: ");
        Optional<Task> taskOpt = taskService.findById(id);

        if (taskOpt.isEmpty()) {
            System.out.printf("  ✗ No task found with ID %d.%n", id);
            pause();
            return;
        }

        Task task = taskOpt.get();
        System.out.println("\n  Task to delete:");
        System.out.println(task);

        if (InputHelper.readYesNo("\n  Are you sure you want to delete this task?")) {
            taskService.deleteTask(id);
            System.out.printf("  ✓ Task %d deleted and file updated.%n", id);
        } else {
            System.out.println("  Deletion cancelled.");
        }
        pause();
    }

    //Exit 

    /**
     * Asks the user to confirm exit.
     *
     * @return {@code false} to stop the menu loop if confirmed;
     *         {@code true} to continue running if the user cancels.
     */
    private boolean confirmExit() {
        return !InputHelper.readYesNo("  Are you sure you want to exit?");
    }

    //Utility

    private void pause() {
        System.out.print("\n  Press Enter to return to the menu...");
        InputHelper.readOptionalString("");
    }
}