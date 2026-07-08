# Task 5 - CRUD with File Storage

This project extends the task manager by saving tasks to a text file. The data remains available even after closing and reopening the program.

## Features

- Add, view, update, and delete tasks
- Persistent storage in a text file
- Task status management
- Simple console-based interface

## Project Structure

- src/Main.java - Starts the application
- src/model/Task.java - Task data model
- src/service/FileService.java - Handles file reading and writing
- src/service/TaskService.java - Manages task operations and persistence
- src/menu/Menu.java - Console menu
- src/util/InputHelper.java - Input handling helper
- data/tasks.txt - Stored task data

## How to Run

From the project root:

```bash
cd Task-5-CRUD-with-File/src
javac -d ../out Main.java model/Task.java service/FileService.java service/TaskService.java menu/Menu.java util/InputHelper.java
java -cp ../out Main
```

## Notes

The task list is saved automatically after create, update, and delete operations.
