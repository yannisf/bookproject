package bookproject.service;

import org.apache.commons.validator.routines.ISBNValidator;
import org.apache.commons.validator.routines.checkdigit.CheckDigitException;
import org.apache.commons.validator.routines.checkdigit.ISBN10CheckDigit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 * {@inheritDoc}
 */
@Service
public class IsbnServiceImpl implements IsbnService {

    private static final Logger LOG = LoggerFactory.getLogger(IsbnServiceImpl.class);

    private ISBNValidator isbnValidator = ISBNValidator.getInstance(false);

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isValid(String isbn) {
        return isbnValidator.isValid(isbn);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isIsbn10(String isbn) {
        return isbnValidator.isValidISBN10(isbn);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isIsbn13(String isbn) {
        return isbnValidator.isValidISBN13(isbn);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String clean(String isbn) {
        return isbnValidator.validate(isbn);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String convertToIsbn10(String isbn) {
        String isbn10 = "N/A";

        if (isIsbn13(isbn) && isbn.startsWith("978")) {
            String base = isbn.substring(3, isbn.length() - 1);
            try {
                return base + ISBN10CheckDigit.ISBN10_CHECK_DIGIT.calculate(base);
            } catch (CheckDigitException e) {
                LOG.warn("Could not convert to ISBN10: [{}]", e.getMessage());
            }
        } else if (isIsbn10(isbn)) {
            isbn10 = isbn;
        } else {
            LOG.warn("Invalid ISBN: [{}]", isbn);
            isbn10 = null;
        }

        return isbn10;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String convertToIsbn13(String isbn) {
        return isbnValidator.convertToISBN13(isbn);
    }

}
