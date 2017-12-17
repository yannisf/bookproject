package bookproject.scraper.api;

/**
 * Thrown when scraping could not be successful.
 */
public class ScraperException extends Exception {

    private static final long serialVersionUID = -2336628489732184895L;

    private final ErrorCode errorCode;

    public ScraperException() {
        super();
        this.errorCode = ErrorCode.GENERIC_ERROR;
    }

    public ScraperException(Exception e) {
        super(e);
        this.errorCode = ErrorCode.GENERIC_ERROR;
    }

    public ScraperException(String message) {
        super(message);
        this.errorCode = ErrorCode.GENERIC_ERROR;
    }

    public ScraperException(ErrorCode errorCode) {
        this.errorCode = errorCode;
    }

    public ErrorCode getErrorCode() {
        return errorCode;
    }
}
