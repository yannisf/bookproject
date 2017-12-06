package bookproject.scraper.api;

/**
 * Thrown when the requested scraper implementation could not be resolved.
 */
public class UnknownToolException extends Exception {

    private static final long serialVersionUID = 8270247519126328989L;

    public UnknownToolException(String message) {
        super(message);
    }

}
