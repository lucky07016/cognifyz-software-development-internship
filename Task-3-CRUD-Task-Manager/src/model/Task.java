package model;

/**
 * Represents a single task in the Task Manager.
 * Holds all data fields: id, title, description, and status.
 */
public class Task {

    public enum Status {
        PENDING,
        IN_PROGRESS,
        COMPLETED
    }

    private static int idCounter = 1;

    private final int id;
    private String title;
    private String description;
    private Status status;

    public Task(String title, String description) {
        this.id          = idCounter++;
        this.title       = title;
        this.description = description;
        this.status      = Status.PENDING;
    }

    //Getters

    public int getId()            { return id; }
    public String getTitle()      { return title; }
    public String getDescription(){ return description; }
    public Status getStatus()     { return status; }

    //  Setters (used by UpdateTask flow) 

    public void setTitle(String title)            { this.title = title; }
    public void setDescription(String description){ this.description = description; }
    public void setStatus(Status status)          { this.status = status; }

    //  Display helper 

    @Override
    public String toString() {
        return String.format(
            "|─────────────────────────────────────────\n" +
            "│ ID          : %d\n"                         +
            "│ Title       : %s\n"                         +
            "│ Description : %s\n"                         +
            "│ Status      : %s\n"                         +
            "|─────────────────────────────────────────",
            id, title, description, status
        );
    }
}