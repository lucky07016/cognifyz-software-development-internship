package service;

import model.Task;
import model.Task.Status;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Service layer that owns the in-memory task list and exposes
 * all CRUD operations:
 *
 *   Create  – addTask()
 *   Read    – getAllTasks() / findById()
 *   Update  – updateTask()
 *   Delete  – deleteTask()
 */
public class TaskService {

    private final List<Task> tasks = new ArrayList<>();

    //CREATE 

    /**
     * Creates and stores a new Task, then returns it.
     */
    public Task addTask(String title, String description) {
        Task task = new Task(title, description);
        tasks.add(task);
        return task;
    }

    //READ 

    /**
     * Returns a copy of all stored tasks.
     */
    public List<Task> getAllTasks() {
        return new ArrayList<>(tasks);
    }

    /**
     * Finds a task by its ID.
     *
     * @return an Optional containing the task, or empty if not found.
     */
    public Optional<Task> findById(int id) {
        return tasks.stream()
                    .filter(t -> t.getId() == id)
                    .findFirst();
    }

    //UPDATE 

    /**
     * Updates the title, description, and/or status of an existing task.
     * Pass null for any field you do not want to change.
     *
     * @return true if the task was found and updated, false otherwise.
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
        return true;
    }

    //DELETE 

    /**
     * Removes the task with the given ID.
     *
     * @return true if the task was found and removed, false otherwise.
     */
    public boolean deleteTask(int id) {
        return tasks.removeIf(t -> t.getId() == id);
    }

    //Utility 

    public boolean isEmpty() {
        return tasks.isEmpty();
    }
}