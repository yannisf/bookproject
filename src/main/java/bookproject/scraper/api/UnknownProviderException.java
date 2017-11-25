package bookproject.scraper.api;

/**
 * Thrown when the requested book information provider cannot be resolved.
 */
public class UnknownProviderException extends ScraperException {

    private static final long serialVersionUID = -7715733005724576512L;

    public UnknownProviderException() {
        //Used as flag exception
    }

}
