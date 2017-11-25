package bookproject.scraper.api;

/**
 * Thrown when the requested scraper implementation could not be resolved.
 */
public class UnknownScraperException extends ScraperException {

    private static final long serialVersionUID = 8270247519126328989L;

    public UnknownScraperException(String message) {
        super(message);
    }

}
