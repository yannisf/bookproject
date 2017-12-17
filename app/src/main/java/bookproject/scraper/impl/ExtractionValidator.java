package bookproject.scraper.impl;

import bookproject.scraper.api.ErrorCode;
import bookproject.scraper.api.ScraperException;
import bookproject.service.IsbnService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Validates that the extraction actually returned information on the intended ISBN.
 */
@Component
public class ExtractionValidator {

    @Autowired
    private IsbnService isbnService;

    /**
     * Validates that the submitted ISBN is compatible with the extracted ISBN. Takes into account possible
     * ISBN10 to ISBN13 conversions and vice versa.
     *
     * @param submittedIsbn the submitted ISBN
     * @param extractedIsbn the extracted ISBN
     * @throws ScraperException thrown when ISBN did not match in any way
     */
    void validate(String submittedIsbn, String extractedIsbn) throws ScraperException {
        if (isbnService.isIsbn10(submittedIsbn) && isbnService.isIsbn13(extractedIsbn)) {
            if (!isbnService.convertToIsbn13(submittedIsbn).equals(extractedIsbn)) {
                throw new ScraperException(ErrorCode.BOOK_NOT_EXTRACTED_RELIABLY);
            }
        } else if (isbnService.isIsbn10(extractedIsbn) && isbnService.isIsbn13(submittedIsbn)) {
            if (!isbnService.convertToIsbn13(extractedIsbn).equals(submittedIsbn)) {
                throw new ScraperException(ErrorCode.BOOK_NOT_EXTRACTED_RELIABLY);
            }
        } else {
            if (!submittedIsbn.equals(extractedIsbn)) {
                throw new ScraperException(ErrorCode.BOOK_NOT_EXTRACTED_RELIABLY);
            }
        }
    }

}
