# Task 5 — CRUD Task Manager with File Persistence

A console-based CRUD Task Manager in Java that persists all tasks to a
plain-text file (`data/tasks.txt`), so data survives between program runs.

---

## Startup Flowchart

```
START
  │
Main.java Starts
  │
Create FileService
  │
Does tasks.txt exist?
    │            │
   No           Yes
    │            │
Create File   Read File
    │            │
    └─────┬──────┘
    Load ArrayList
          │
    Display Menu
          │
  Read User Choice
          │
 ┌────┬───┴──┬────────┬────────┐
Add  View  Update  Delete    Exit
 │           │        │
ArrayList  ArrayList ArrayList
 │                    │
 |--------------------|
            |
      Save to File
            │
      Back to Menu
```

---

## Project Structure

```
Task-5-CRUD-with-File/
│
├── src/
│   ├-- Main.java                     # Entry point — wires FileService → TaskService → Menu
│   │
│   ├-- model/
│   │   |── Task.java                 # Entity: id, title, description, Status enum
│   │                                 # + toFileString() / fromFileString() serialization
│   │
│   ├--service/
│   │   ├-- FileService.java          # File I/O: loadTasks(), saveTasks()
│   │   |-- TaskService.java          # CRUD logic over ArrayList; auto-saves on mutation
│   │
│   ├-- menu/
│   │   |-- Menu.java                 # Console UI: display menu, dispatch choices
│   │
│   |-- util/
│       |-- InputHelper.java          # Validated console input helpers
│
├-- data/
│   |--tasks.txt                     # Persistent task storage (pipe-delimited)
│
├-- README.md
|--.gitignore
```

---

## File Format

Each task is stored as a single pipe-delimited line in `tasks.txt`:

```
id|title|description|status
```

**Example:**
```
1|Buy groceries|Milk, eggs, bread|PENDING
2|Read Java book|Chapter 8 – Generics|IN_PROGRESS
3|Push to GitHub|Tag release v1.0|COMPLETED
```

- Malformed lines are silently skipped on load.
- The file is fully rewritten on every Add, Update, or Delete.

---

## Menu Options

| # | Action      | Saves to file?|
|---|-------------|---------------|
| 1 | Add Task    | ✓ Yes         |
| 2 | View Tasks  | ✗ No          |
| 3 | Update Task | ✓ Yes         |
| 4 | Delete Task | ✓ Yes         |
| 5 | Exit        | —             |

---

## Task Statuses

| Status      | Meaning            |
|-------------|--------------------|
| PENDING     | Not started (default) |
| IN_PROGRESS | Work has started   |
| COMPLETED   | Done               |

---

## How to Compile and Run

From the project root:

```bash
cd src
javac -d . Main.java model/Task.java service/FileService.java service/TaskService.java util/InputHelper.java menu/Menu.java
java Main
```

> **Note:** Run from the `src/` directory so the relative path `data/tasks.txt`
> resolves correctly. The `data/` folder and file are created automatically if
> they do not exist.

---

## Example Session

```
  [FileService] Loaded 2 task(s) from: data/tasks.txt

  |---------------------------------|
  |    CRUD Task Manager  +  File   |
  |---------------------------------|

  |---------------------------------|
  │          M A I N   M E N U      │
  |---------------------------------|
  │  1. Add Task                     │
  │  2. View All Tasks               │
  │  3. Update Task                  │
  │  4. Delete Task                  │
  │  5. Exit                         │
  |----------------------------------|
  Enter your choice: 1

  :Add New Task
  Title       : Fix login bug
  Description : Null pointer on empty password

  ✓ Task added and saved to file!

  |----------------------------------------------
  │ ID          : 3
  │ Title       : Fix login bug
  │ Description : Null pointer on empty password
  │ Status      : PENDING
  |-----------------------------------------------
```