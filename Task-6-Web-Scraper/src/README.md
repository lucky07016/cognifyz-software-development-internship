# Task 6 — Web Scraper

Scrapes the first page of [books.toscrape.com](http://books.toscrape.com) — a
sandbox site built for scraping practice — and pulls out the title, price,
star rating, and availability of every book listed.

No third-party libraries. Pure Java: `HttpURLConnection` for HTTP,
manual string parsing for HTML.

---

## Flow

```
Main.java
    │
    ▼
Create WebScraper
    │
    ▼
HttpHelper connects to books.toscrape.com
    │
    ▼
Download raw HTML
    │
    ▼
WebScraper parses each <article class="product_pod"> block
    │
    ▼
Create Book objects  →  ArrayList<Book>
    │
    ▼
Display table in console  +  save to output/books.txt
    │
    ▼
END
```

---

## File Structure

```
Task-6-Web-Scraper/
│
├── src/
│   ├── model/
│   │   └── Book.java           — title, price, rating, availability
│   ├── service/
│   │   └── WebScraper.java     — downloads HTML, parses it, saves output
│   ├── util/
│   │   └── HttpHelper.java     — HttpURLConnection wrapper
│   └── Main.java               — entry point, prints the table
│
├── output/
│   └── books.txt               — written after every run
│
├── README.md
└── .gitignore
```

---

## What gets scraped

Each book block on the page looks like this in HTML:

```html
<article class="product_pod">
  <h3><a href="..." title="A Light in the Attic">A Light in ...</a></h3>
  <p class="price_color">£51.77</p>
  <p class="star-rating Three"></p>
  <p class="instock availability"> In stock </p>
</article>
```

`WebScraper` finds every `<article>` block, then extracts:

| Field        | Where it comes from              |
|--------------|----------------------------------|
| Title        | `title="..."` attribute on `<a>` |
| Price        | `<p class="price_color">`        |
| Rating       | `class="star-rating <Word>"`     |
| Availability | `instock` vs `outofstock` class  |

---

## How to run

```bash
cd src
javac -d . Main.java model/Book.java util/HttpHelper.java service/WebScraper.java
java Main
```

Run from the `src/` directory so that `output/books.txt` resolves correctly
relative to where you launch the JVM.

---

## Sample output

```
#  TITLE                                                    |  PRICE | RATING      | AVAILABILITY
----------------------------------------------------------------------------------------------------
 1. A Light in the Attic                                    | £51.77 | 3 stars     | In stock
 2. Tipping the Velvet                                      | £53.74 | 1 stars     | In stock
 3. Soumission                                              | £50.10 | 1 stars     | In stock
 4. Sharp Objects                                           | £47.82 | 4 stars     | In stock
...
----------------------------------------------------------------------------------------------------
Total: 20 books scraped from page 1.
```

Results are also written to `output/books.txt` after every run.