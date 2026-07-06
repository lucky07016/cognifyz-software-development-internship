import menu.Menu;
import service.FileService;
import service.TaskService;

/**
 * Entry point for the CRUD Task Manager with File Persistence.
 *
 * <p>Wires the application together following the startup flowchart:</p>
 * <pre>
 *   Main.java Starts
 *          │
 *   Create FileService
 *          │
 *   Does tasks.txt exist?
 *       │          │
 *      No         Yes
 *       │          │
 *  Create File  Read File
 *       │          │
 *       |----------|
 *            |
 *      Load ArrayList          < handled inside TaskService constructor
 *            │
 *      Display Menu            < handed off to Menu.run()
 * </pre>
 */
public class Main {

    private static final String DATA_FILE = "data/tasks.txt";

    public static void main(String[] args) {
        FileService fileService = new FileService(DATA_FILE);
        TaskService taskService = new TaskService(fileService);
        new Menu(taskService).run();
    }
}