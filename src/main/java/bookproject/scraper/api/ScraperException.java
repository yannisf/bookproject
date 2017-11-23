package bookproject.scraper.api;

/**
 * Thrown when scraping could not be successful.
 */
public class ScraperException extends Exception {
    public ScraperException(String message) {
        super(message);
    }

    public ScraperException(Exception e) {
        super(e);
    }
}
