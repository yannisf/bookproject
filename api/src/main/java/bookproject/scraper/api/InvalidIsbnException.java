package bookproject.scraper.api;

/**
 * Thrown when an invalid ISBN is encountered.
 */
public class InvalidIsbnException extends Exception {
    private static final long serialVersionUID = 5607117086260058549L;

    public InvalidIsbnException(String s) {
        super(s);
    }
}
