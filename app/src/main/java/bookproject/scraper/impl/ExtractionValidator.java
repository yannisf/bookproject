package bookproject.scraper.impl;

import bookproject.scraper.api.ScraperException;
import bookproject.service.IsbnService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ExtractionValidator {

    @Autowired
    private IsbnService isbnService;

    void validate(String submittedIsbn, String extractedIsbn) throws ScraperException {
        if (isbnService.isIsbn10(submittedIsbn) && isbnService.isIsbn13(extractedIsbn)) {
            if (!isbnService.convertToIsbn13(submittedIsbn).equals(extractedIsbn)) {
                throw new ScraperException("Book information could not be extracted reliably.");
            }
        } else if (isbnService.isIsbn10(extractedIsbn) && isbnService.isIsbn13(submittedIsbn)) {
            if (!isbnService.convertToIsbn13(extractedIsbn).equals(submittedIsbn)) {
                throw new ScraperException("Book information could not be extracted reliably.");
            }
        } else {
            if (!submittedIsbn.equals(extractedIsbn)) {
                throw new ScraperException("Book information could not be extracted reliably.");
            }
        }
    }

}
