# Task 3 — CRUD Task Manager

A simple console-based to-do list manager built in Java! This project shows how to build a real application with **Create, Read, Update, Delete** features (the basics of data management). Perfect for learning how to organize code into clean layers.

---

## How It Works

Here's the flow when you use the app:

1. **See the menu** - You get 5 options to choose from
2. **Pick an action** - Add a task, view them, update, or delete
3. **The app handles it** - All your tasks get managed behind the scenes
4. **Go back to menu** - Keep working or exit when you're done

---

## Project Structure

Don't worry about all the folders—here's what each part does:

```
Task-3-CRUD-Task-Manager/
│
├── src/
│   ├── Main.java              <-> Starts the app (the front door)
│   ├── model/
│   │   |── Task.java          <-> What a task looks like (its shape/data)
│   ├── service/
│   │   |── TaskService.java   <-> The brain that adds, removes, changes tasks
│   ├── menu/
│   │   |── Menu.java          <-> The screen you interact with
│   |── util/
│       |── InputHelper.java   <-> Helper to safely read what you type
│
├── README.md
|── .gitignore
```

---

## Menu Options

What can you do? Here's your menu:

| # | Option      | What Happens                                           |
|---|-------------|--------------------------------------------------------|
| 1 | Add Task    | Create a new to-do item (it starts as PENDING)        |
| 2 | View Tasks  | See all your tasks with details                       |
| 3 | Update Task | Change a task's info or mark it as IN_PROGRESS/DONE   |
| 4 | Delete Task | Remove a task you're done with                        |
| 5 | Exit        | Close the app (it'll ask to confirm)                  |

---

## Task Statuses

Every task has a status that shows where it's at:

- **PENDING** — Just created, waiting to be worked on
- **IN_PROGRESS** — You've started working on it
- **COMPLETED** — All done!

---

## Getting Started

Want to run it? Easy! From your terminal:

```bash
cd Task-3-CRUD-Task-Manager

# Option 1: Simple (compiles everything at once)
javac -d bin src/Main.java model/Task.java service/TaskService.java menu/Menu.java util/InputHelper.java
java -cp bin Main

# Option 2: Even simpler (if you're in the src folder)
javac -d . Main.java model/Task.java service/TaskService.java menu/Menu.java util/InputHelper.java
java Main
```

Then just follow the on-screen menu!

---

## Example Session

Here's what using the app looks like:

```
  |══════════════════════════════|
  |      CRUD Task Manager       |
  |══════════════════════════════|

  |──────────────────────────────|
  │          M A I N  M E N U    │
  ├──────────────────────────────┤
  │  1. Add Task                 │
  │  2. View All Tasks           │
  │  3. Update Task              │
  │  4. Delete Task              │
  │  5. Exit                     │
  └──────────────────────────────┘
  Enter your choice: 1

  ── Add New Task ──────────────────────────
  Title       : Buy groceries
  Description : Milk, eggs, bread

  ✓ Task added successfully!

  |─────────────────────────────────────────
  │ ID          : 1
  │ Title       : Buy groceries
  │ Description : Milk, eggs, bread
  │ Status      : PENDING
  |─────────────────────────────────────────

  (Back to menu...)
```

Pretty straightforward, right? 🎯

