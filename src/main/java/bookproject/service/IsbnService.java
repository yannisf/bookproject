package bookproject.service;

public interface IsbnService {

    /**
     * Checks if provided ISBN is a valid ISBN or ISBN13.
     * @param isbn the provided ISBN
     * @return true if valid
     */
    boolean isValid(String isbn);

    /**
     * Checks if provided ISBN is a valid ISBN (not ISBN13).
     * @param isbn the provided ISBN
     * @return true if ISBN
     */
    boolean isIsbn10(String isbn);

    /**
     * Checks if provided ISBN is a valid ISBN13.
     * @param isbn the provided ISBN
     * @return true if ISBN13
     */
    boolean isIsbn13(String isbn);

    /**
     * Cleans provided ISBN.
     * @param isbn the provided ISBN
     * @return the cleaned ISBN
     */
    String clean(String isbn);

    /**
     * Converts provided ISBN to ISBN13.
     * @param isbn the provided ISBN
     * @return the converted ISBN13
     */
    String convertToIsbn13(String isbn);

    /**
     * Converts provided ISBN13 to ISBN.
     *
     * @param isbn the provided ISBN
     * @return the converted ISBN
     */
    String convertToIsbn10(String isbn);

}
