package util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class HttpHelper {

    // some sites block Java's default user-agent, so we fake a browser
    private static final String USER_AGENT =
        "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 " +
        "(KHTML, like Gecko) Chrome/120.0.0.0 Safari/537.36";

    private static final int TIMEOUT_MS = 10_000;

    /**
     * Downloads the HTML at the given URL and returns it as a single string.
     *
     * @param urlString the page to fetch
     * @return raw HTML content
     * @throws IOException if the connection fails or returns a non-200 status
     */
    public String fetchHTML(String urlString) throws IOException {
        HttpURLConnection conn = null;

        try {
            URL url = new URL(urlString);
            conn = (HttpURLConnection) url.openConnection();

            conn.setRequestMethod("GET");
            conn.setRequestProperty("User-Agent", USER_AGENT);
            conn.setConnectTimeout(TIMEOUT_MS);
            conn.setReadTimeout(TIMEOUT_MS);
            conn.setInstanceFollowRedirects(true);

            int status = conn.getResponseCode();
            if (status != HttpURLConnection.HTTP_OK) {
                throw new IOException("Server returned HTTP " + status + " for URL: " + urlString);
            }

            // read the response — books.toscrape.com uses UTF-8
            BufferedReader reader = new BufferedReader(
                new InputStreamReader(conn.getInputStream(), "UTF-8")
            );

            StringBuilder html = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                html.append(line).append("\n");
            }
            reader.close();

            return html.toString();

        } finally {
            if (conn != null) {
                conn.disconnect();
            }
        }
    }
}