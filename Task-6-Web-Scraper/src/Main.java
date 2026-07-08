import model.Book;
import service.WebScraper;

import java.io.IOException;
import java.util.List;

public class Main {

    public static void main(String[] args) {
        WebScraper scraper = new WebScraper();

        try {
            List<Book> books = scraper.scrape();

            if (books.isEmpty()) {
                System.out.println("No books found — the site structure may have changed.");
                return;
            }

            // print header
            System.out.printf("%-4s %-55s | %6s | %-11s | %s%n",
                    "#", "TITLE", "PRICE", "RATING", "AVAILABILITY");
            System.out.println("-".repeat(100));

            // print each book
            for (int i = 0; i < books.size(); i++) {
                System.out.printf("%2d. %s%n", i + 1, books.get(i));
            }

            System.out.println("-".repeat(100));
            System.out.println("Total: " + books.size() + " books scraped from page 1.");

        } catch (IOException e) {
            System.err.println("Scraping failed: " + e.getMessage());
        }
    }
}