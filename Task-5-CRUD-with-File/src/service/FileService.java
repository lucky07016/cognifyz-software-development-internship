package service;

import model.Task;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class FileService {

    private final String filePath;

    /**
     * Creates a {@code FileService} that reads from and writes to the
     * given file path.
     *
     * @param filePath Path to the task data file (e.g. {@code "data/tasks.txt"}).
     */
    public FileService(String filePath) {
        this.filePath = filePath;
    }

    //Startup: check / create / read

    /**
     * Implements the startup branch of the flowchart:
     * <ul>
     *   <li>If the file does <em>not</em> exist → creates it (and any missing
     *       parent directories) then returns an empty list.</li>
     *   <li>If the file <em>does</em> exist → reads it line by line, deserializes
     *       each task, and returns the populated list.</li>
     * </ul>
     *
     * @return A mutable {@link ArrayList} of {@link Task} objects loaded from
     *         the file; never {@code null}, but may be empty.
     */
    public List<Task> loadTasks() {
        Path path = Path.of(filePath);

        if (!Files.exists(path)) {
            createFile(path);
            return new ArrayList<>();
        }

        return readFile(path);
    }

    private void createFile(Path path) {
        try {
            Path parent = path.getParent();
            if (parent != null) {
                Files.createDirectories(parent);
            }
            Files.createFile(path);
            System.out.println("  [FileService] Created task file: " + filePath);
        } catch (IOException e) {
            System.err.println("  [FileService] ERROR: Could not create file: " + e.getMessage());
        }
    }

    private List<Task> readFile(Path path) {
        List<Task> tasks = new ArrayList<>();
        int maxId = 0;

        try {
            List<String> lines = Files.readAllLines(path);
            for (String line : lines) {
                Task task = Task.fromFileString(line);
                if (task != null) {
                    tasks.add(task);
                    if (task.getId() > maxId) {
                        maxId = task.getId();
                    }
                }
            }
        } catch (IOException e) {
            System.err.println("  [FileService] ERROR: Could not read file: " + e.getMessage());
        }

        Task.syncIdCounter(maxId);
        System.out.printf("  [FileService] Loaded %d task(s) from: %s%n", tasks.size(), filePath);
        return tasks;
    }

    //Save

    /**
     * Serializes the entire task list and writes it to the data file,
     * overwriting any previous contents. Called after every Add, Update,
     * or Delete operation to keep the file in sync with the in-memory list.
     *
     * @param tasks The current list of tasks to persist; must not be {@code null}.
     */
    public void saveTasks(List<Task> tasks) {
        Path path = Path.of(filePath);
        List<String> lines = new ArrayList<>();
        for (Task task : tasks) {
            lines.add(task.toFileString());
        }

        try {
            Files.write(path, lines);
        } catch (IOException e) {
            System.err.println("  [FileService] ERROR: Could not save file: " + e.getMessage());
        }
    }
}