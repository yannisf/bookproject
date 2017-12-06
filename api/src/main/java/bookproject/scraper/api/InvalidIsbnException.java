package bookproject.scraper.api;

public class InvalidIsbnException extends Exception {
    private static final long serialVersionUID = 5607117086260058549L;

    public InvalidIsbnException(String s) {
        super(s);
    }
}
