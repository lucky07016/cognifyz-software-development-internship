import menu.Menu;
import service.TaskService;

/**
 * Entry point for the CRUD Task Manager.
 *
 * Wires together the two top-level collaborators:
 *   TaskService  – owns and manages the task data
 *   Menu – drives the console UI and delegates to TaskService
 */
public class Main {
    public static void main(String[] args) {
        TaskService taskService = new TaskService(null);
        Menu        menu        = new Menu(taskService);
        menu.run();
    }
}
