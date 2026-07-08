# Task 3 - CRUD Task Manager

This project is a console-based task manager built in Java. It allows a user to create, view, update, and delete tasks through a simple menu.

## Features

- Add new tasks
- View all tasks
- Update existing tasks
- Delete tasks
- Basic task status handling

## Project Structure

- src/Main.java - Starts the application
- src/model/Task.java - Task data model
- src/service/TaskService.java - Business logic for CRUD operations
- src/menu/Menu.java - Console menu and user interaction
- src/util/InputHelper.java - Input handling helper

## How to Run

From the project root:

```bash
cd Task-3-CRUD-Task-Manager/src
javac -d ../out Main.java model/Task.java service/TaskService.java menu/Menu.java util/InputHelper.java
java -cp ../out Main
```

## Notes

The project is organized into separate layers to keep the code easy to follow and maintain.

