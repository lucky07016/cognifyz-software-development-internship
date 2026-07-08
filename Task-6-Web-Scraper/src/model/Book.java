package model;

public class Book {

    private String title;
    private String price;
    private String rating;
    private String availability;

    public Book(String title, String price, String rating, String availability) {
        this.title        = title;
        this.price        = price;
        this.rating       = rating;
        this.availability = availability;
    }

    public String getTitle()        { return title; }
    public String getPrice()        { return price; }
    public String getRating()       { return rating; }
    public String getAvailability() { return availability; }

    @Override
    public String toString() {
        return String.format("%-55s | %6s | %-11s | %s",
                title.length() > 52 ? title.substring(0, 52) + "..." : title,
                price,
                rating + " stars",
                availability);
    }
}