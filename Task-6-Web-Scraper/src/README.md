# Task 6 - Web Scraper

This project is a simple Java web scraper that collects book information from a sample website and displays it in the console. The results are also written to a text file.

## Features

- Connects to the target website
- Extracts title, price, rating, and availability
- Displays the scraped data in a readable table
- Saves the output to a text file

## Project Structure

- src/Main.java - Entry point
- src/model/Book.java - Book data model
- src/service/WebScraper.java - Scraping and parsing logic
- src/util/HttpHelper.java - HTTP connection helper
- output/books.txt - Saved output from the latest run

## How to Run

From the project root:

```bash
cd Task-6-Web-Scraper/src
javac -d ../out Main.java model/Book.java service/WebScraper.java util/HttpHelper.java
java -cp ../out Main
```

## Notes

This project uses only Java's standard library and does not require any external libraries.
