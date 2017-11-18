package bookproject.scrapper.api;

/**
 * Thrown when scraping could not be successful.
 */
public class ScraperException extends Exception {
    public ScraperException(Exception e) {
        super(e);
    }
}
