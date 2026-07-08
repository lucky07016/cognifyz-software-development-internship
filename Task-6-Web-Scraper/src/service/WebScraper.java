package service;

import model.Book;
import util.HttpHelper;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class WebScraper {

    // scraping books.toscrape.com — it's a sandbox site made for practice
    private static final String BASE_URL   = "http://books.toscrape.com/catalogue/";
    private static final String START_PAGE = "http://books.toscrape.com/catalogue/page-1.html";

    private static final String OUTPUT_FILE = "output/books.txt";

    private final HttpHelper httpHelper;

    public WebScraper() {
        this.httpHelper = new HttpHelper();
    }

    // -------------------------------------------------------------------------

    public List<Book> scrape() throws IOException {
        System.out.println("Connecting to: " + START_PAGE);
        String html = httpHelper.fetchHTML(START_PAGE);
        System.out.println("HTML downloaded — parsing...\n");

        List<Book> books = parseBooks(html);

        // save a copy to file as well
        saveToFile(books);

        return books;
    }

    // -------------------------------------------------------------------------
    // Parsing
    // -------------------------------------------------------------------------

    private List<Book> parseBooks(String html) {
        List<Book> books = new ArrayList<>();

        // every book sits inside <article class="product_pod"> ... </article>
        String articleTag = "<article class=\"product_pod\">";
        int    cursor     = 0;

        while (true) {
            int start = html.indexOf(articleTag, cursor);
            if (start == -1) break;

            int end = html.indexOf("</article>", start);
            if (end == -1) break;

            String block = html.substring(start, end + "</article>".length());

            String title        = extractTitle(block);
            String price        = extractPrice(block);
            String rating       = extractRating(block);
            String availability = extractAvailability(block);

            if (title != null) {
                books.add(new Book(title, price, rating, availability));
            }

            cursor = end + 1;
        }

        return books;
    }

    // title is in: <a href="..." title="The Book Title">
    private String extractTitle(String block) {
        String marker = "title=\"";
        int i = block.indexOf(marker);
        if (i == -1) return null;

        int start = i + marker.length();
        int end   = block.indexOf("\"", start);
        if (end == -1) return null;

        return decodeHtmlEntities(block.substring(start, end).trim());
    }

    // price is inside: <p class="price_color">£19.99</p>
    private String extractPrice(String block) {
        String marker = "class=\"price_color\">";
        int i = block.indexOf(marker);
        if (i == -1) return "N/A";

        int start = i + marker.length();
        int end   = block.indexOf("<", start);
        if (end == -1) return "N/A";

        // strip any weird pound/currency unicode the site sometimes sends
        String raw = block.substring(start, end).trim();
        return raw.replaceAll("[^\\x20-\\x7E]", "").trim(); // keep only printable ASCII
    }

    // rating word is in: <p class="star-rating Three">
    // map the word to a number
    private String extractRating(String block) {
        String marker = "class=\"star-rating ";
        int i = block.indexOf(marker);
        if (i == -1) return "0";

        int start = i + marker.length();
        int end   = block.indexOf("\"", start);
        if (end == -1) return "0";

        String word = block.substring(start, end).trim();
        switch (word) {
            case "One":   return "1";
            case "Two":   return "2";
            case "Three": return "3";
            case "Four":  return "4";
            case "Five":  return "5";
            default:      return "0";
        }
    }

    // availability is in: <p class="instock availability"> In stock </p>
    private String extractAvailability(String block) {
        String marker = "class=\"instock availability\">";
        int i = block.indexOf(marker);
        if (i != -1) return "In stock";

        marker = "class=\"outofstock availability\">";
        i = block.indexOf(marker);
        if (i != -1) return "Out of stock";

        return "Unknown";
    }

    // the site HTML-encodes apostrophes and a few other chars
    private String decodeHtmlEntities(String text) {
        return text
            .replace("&#39;",  "'")
            .replace("&amp;",  "&")
            .replace("&quot;", "\"")
            .replace("&lt;",   "<")
            .replace("&gt;",   ">");
    }

    // -------------------------------------------------------------------------
    // File output
    // -------------------------------------------------------------------------

    private void saveToFile(List<Book> books) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(OUTPUT_FILE))) {
            writer.write(buildHeader());
            writer.newLine();

            for (int i = 0; i < books.size(); i++) {
                writer.write(String.format("%2d. %s", i + 1, books.get(i).toString()));
                writer.newLine();
            }

            writer.newLine();
            writer.write("Total books scraped: " + books.size());
            writer.newLine();

            System.out.println("Results saved to: " + OUTPUT_FILE);
        } catch (IOException e) {
            // not fatal — we still display results in console
            System.err.println("Warning: could not write to file — " + e.getMessage());
        }
    }

    private String buildHeader() {
        return String.format("%-58s | %6s | %-11s | %s",
                "TITLE", "PRICE", "RATING", "AVAILABILITY");
    }
}