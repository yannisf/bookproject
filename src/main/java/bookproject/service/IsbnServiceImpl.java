package bookproject.service;

import org.apache.commons.validator.routines.ISBNValidator;
import org.springframework.stereotype.Service;

/** {@inheritDoc} */
@Service
public class IsbnServiceImpl implements IsbnService {

    private ISBNValidator isbnValidator = ISBNValidator.getInstance(false);

    /** {@inheritDoc} */
    @Override
    public boolean isValid(String isbn) {
        return isbnValidator.isValid(isbn);
    }

    /** {@inheritDoc} */
    @Override
    public boolean isIsbn10(String isbn) {
        return isbnValidator.isValidISBN10(isbn);
    }

    /** {@inheritDoc} */
    @Override
    public boolean isIsbn13(String isbn) {
        return isbnValidator.isValidISBN13(isbn);
    }

    /** {@inheritDoc} */
    @Override
    public String cleanIsbn(String isbn) {
        return isbnValidator.validate(isbn);
    }

    /** {@inheritDoc} */
    @Override
    public String convertToIsbn13(String isbn) {
        return isbnValidator.convertToISBN13(isbn);
    }

}
