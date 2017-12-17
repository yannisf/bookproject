package bookproject.scraper.api;

/**
 * Error codes for application exceptions
 */
public enum ErrorCode {

    GENERIC_ERROR("Generic error"),
    BOOK_NOT_FOUND("Book not found"),
    BOOK_NOT_EXTRACTED_RELIABLY("Book not extracted reliably"),
    INVALID_TOOL("Invalid tool"),
    INVALID_PROVIDER("Invalid provider"),
    INVALID_ISBN("Invalid ISBN");

    private String errorMessage;

    ErrorCode(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    /**
     * Human readable message for the code
     *
     * @return the message
     */
    public String getErrorMessage() {
        return errorMessage;
    }

}
