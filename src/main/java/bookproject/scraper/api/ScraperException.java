package bookproject.scraper.api;

/**
 * Thrown when scraping could not be successful.
 */
public class ScraperException extends Exception {

    private static final long serialVersionUID = -2336628489732184895L;

    public ScraperException() {
    }

    public ScraperException(String message) {
        super(message);
    }

    public ScraperException(Exception e) {
        super(e);
    }
}
