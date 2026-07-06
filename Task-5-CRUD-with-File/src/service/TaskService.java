package service;

import model.Task;
import model.Task.Status;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Manages the in-memory {@link ArrayList} of tasks and exposes
 * all CRUD operations.
 *
 * <p>Every operation that mutates the list (Add, Update, Delete)
 * immediately calls {@link FileService#saveTasks(List)} to persist
 * the change to {@code tasks.txt}, following the flowchart:</p>
 *
 * <pre>
 *   Add / Update / Delete
 *          │
 *       ArrayList
 *          │
 *     Save to File
 *          │
 *     Back to Menu
 * </pre>
 *
 * <p>Read (View) operations only query the list — no file write occurs.</p>
 */
public class TaskService {

    private final List<Task>    tasks;
    private final FileService   fileService;

    /**
     * Creates a {@code TaskService}, loading the initial task list from
     * disk via the provided {@link FileService}.
     *
     * @param fileService The file I/O handler used both to load tasks on
     *                    startup and to persist changes after every mutation.
     */
    public TaskService(FileService fileService) {
        this.fileService = fileService;
        this.tasks       = fileService.loadTasks();
    }

    //CREATE

    /**
     * Creates a new {@link Task}, adds it to the ArrayList, and immediately
     * saves the updated list to the file.
     *
     * @param title       The title for the new task; must not be blank.
     * @param description The description for the new task; must not be blank.
     * @return The newly created {@link Task} with its auto-assigned ID.
     */
    public Task addTask(String title, String description) {
        Task task = new Task(title, description);
        tasks.add(task);
        fileService.saveTasks(tasks);
        return task;
    }

    //READ

    /**
     * Returns a snapshot copy of all tasks currently in the ArrayList.
     * Does not trigger a file save.
     *
     * @return A new {@link ArrayList} containing all current tasks; never {@code null}.
     */
    public List<Task> getAllTasks() {
        return new ArrayList<>(tasks);
    }

    /**
     * Searches the ArrayList for a task with the given ID.
     * Does not trigger a file save.
     *
     * @param id The numeric ID to search for.
     * @return An {@link Optional} containing the matching {@link Task},
     *         or {@link Optional#empty()} if no task has that ID.
     */
    public Optional<Task> findById(int id) {
        return tasks.stream()
                    .filter(t -> t.getId() == id)
                    .findFirst();
    }

    //UPDATE

    /**
     * Updates the title, description, and/or status of an existing task,
     * then saves the modified list to the file.
     *
     * <p>Pass {@code null} (or a blank string for text fields) for any
     * parameter you do not wish to change.</p>
     *
     * @param id             The ID of the task to update.
     * @param newTitle       Replacement title, or {@code null} / blank to keep current.
     * @param newDescription Replacement description, or {@code null} / blank to keep current.
     * @param newStatus      Replacement {@link Status}, or {@code null} to keep current.
     * @return {@code true} if the task was found and updated;
     *         {@code false} if no task with the given ID exists.
     */
    public boolean updateTask(int id, String newTitle, String newDescription, Status newStatus) {
        Optional<Task> opt = findById(id);
        if (opt.isEmpty()) {
            return false;
        }
        Task task = opt.get();
        if (newTitle       != null && !newTitle.isBlank())       { task.setTitle(newTitle); }
        if (newDescription != null && !newDescription.isBlank()) { task.setDescription(newDescription); }
        if (newStatus      != null)                              { task.setStatus(newStatus); }

        fileService.saveTasks(tasks);
        return true;
    }

    //DELETE

    /**
     * Removes the task with the given ID from the ArrayList, then saves
     * the updated list to the file.
     *
     * @param id The ID of the task to remove.
     * @return {@code true} if a task with that ID was found and removed;
     *         {@code false} if no such task exists.
     */
    public boolean deleteTask(int id) {
        boolean removed = tasks.removeIf(t -> t.getId() == id);
        if (removed) {
            fileService.saveTasks(tasks);
        }
        return removed;
    }

    //Utility

    /**
     * Checks whether the task list is empty.
     *
     * @return {@code true} if there are no tasks; {@code false} otherwise.
     */
    public boolean isEmpty() {
        return tasks.isEmpty();
    }
}