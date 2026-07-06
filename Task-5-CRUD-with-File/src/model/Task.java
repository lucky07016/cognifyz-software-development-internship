package model;

/**
 * Represents a single task entity.
 *
 * <p>Holds all data fields: id, title, description, and status.
 * Also provides serialization helpers ({@link #toFileString()} and
 * {@link #fromFileString(String)}) so tasks can be persisted to and
 * restored from a plain-text file line by line.</p>
 *
 * <p>File format (one task per line):</p>
 * <pre>id|title|description|status</pre>
 */
public class Task {

    /** Pipe character used to separate fields in the file format. */
    public static final String DELIMITER = "|";

    /** Regex-safe version of DELIMITER for use with {@code String.split()}. */
    private static final String DELIMITER_REGEX = "\\|";

    /** Number of fields expected in each serialized line. */
    private static final int FIELD_COUNT = 4;

    // Status enumeration

    public enum Status {
        PENDING,
        IN_PROGRESS,
        COMPLETED
    }

    //ID counter 

    private static int idCounter = 1;

    /**
     * Advances the ID counter so that the next auto-assigned ID will be
     * greater than any ID already loaded from the file.
     *
     * @param maxExistingId The highest ID found in the loaded task list.
     */
    public static void syncIdCounter(int maxExistingId) {
        if (maxExistingId >= idCounter) {
            idCounter = maxExistingId + 1;
        }
    }

    // Fields 

    private final int id;
    private String title;
    private String description;
    private Status status;

    //Constructors 

    /**
     * Creates a new task, auto-assigning the next available ID.
     * Status defaults to {@link Status#PENDING}.
     *
     * @param title       Short name for the task.
     * @param description Detailed description of the task.
     */
    public Task(String title, String description) {
        this.id          = idCounter++;
        this.title       = title;
        this.description = description;
        this.status      = Status.PENDING;
    }

    /**
     * Private constructor used by {@link #fromFileString(String)} to
     * restore a task from its persisted representation with a known ID.
     *
     * @param id          The task's persisted ID.
     * @param title       The task's persisted title.
     * @param description The task's persisted description.
     * @param status      The task's persisted status.
     */
    private Task(int id, String title, String description, Status status) {
        this.id          = id;
        this.title       = title;
        this.description = description;
        this.status      = status;
    }

    //Getters

    /** @return The unique numeric identifier of this task. */
    public int getId()             { return id; }

    /** @return The short title of this task. */
    public String getTitle()       { return title; }

    /** @return The detailed description of this task. */
    public String getDescription() { return description; }

    /** @return The current {@link Status} of this task. */
    public Status getStatus()      { return status; }

    // Setters 

    /**
     * Updates the title of this task.
     *
     * @param title The new title; must not be blank.
     */
    public void setTitle(String title)             { this.title = title; }

    /**
     * Updates the description of this task.
     *
     * @param description The new description; must not be blank.
     */
    public void setDescription(String description) { this.description = description; }

    /**
     * Updates the status of this task.
     *
     * @param status The new {@link Status} value.
     */
    public void setStatus(Status status)           { this.status = status; }

    // File serialization

    /**
     * Serializes this task into a single pipe-delimited line suitable for
     * writing to {@code tasks.txt}.
     *
     * <p>Example output: {@code 3|Buy groceries|Milk and eggs|PENDING}</p>
     *
     * @return A non-null, non-empty {@code String} representing this task.
     */
    public String toFileString() {
        return id + DELIMITER + title + DELIMITER + description + DELIMITER + status.name();
    }

    /**
     * Deserializes a task from a single pipe-delimited line read from
     * {@code tasks.txt}.
     *
     * <p>Expected format: {@code id|title|description|status}</p>
     *
     * @param line A non-null line from the task file.
     * @return The reconstructed {@link Task}, or {@code null} if the line
     *         is malformed or contains an unrecognised status value.
     */
    public static Task fromFileString(String line) {
        if (line == null || line.isBlank()) {
            return null;
        }
        String[] parts = line.split(DELIMITER_REGEX, FIELD_COUNT);
        if (parts.length < FIELD_COUNT) {
            return null;
        }
        try {
            int id = Integer.parseInt(parts[0].trim());
            String title = parts[1].trim();
            String description = parts[2].trim();
            Status status = Status.valueOf(parts[3].trim());
            return new Task(id, title, description, status);
        } catch (IllegalArgumentException e) {
            return null;
        }
    }

    // Display

    @Override
    public String toString() {
        return String.format(
            "|-----------------------------------------\n" +
            "│ ID          : %d\n"                         +
            "│ Title       : %s\n"                         +
            "│ Description : %s\n"                         +
            "│ Status      : %s\n"                         +
            "|-----------------------------------------",
            id, title, description, status
        );
    }
}