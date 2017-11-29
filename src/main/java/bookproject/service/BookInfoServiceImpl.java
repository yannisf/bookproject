package bookproject.service;

import bookproject.repository.BookRepository;
import bookproject.scraper.api.BookInfo;
import bookproject.scraper.api.BookInfoProvider;
import bookproject.scraper.api.Scraper;
import bookproject.scraper.api.ScraperException;
import bookproject.scraper.impl.ScraperResolver;
import bookproject.scraper.provider.ProviderResolver;
import org.apache.commons.validator.routines.ISBNValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BookInfoServiceImpl implements BookInfoService {

    @Autowired
    private ScraperResolver scraperResolver;

    @Autowired
    private ProviderResolver providerResolver;

    @Autowired
    private BookRepository bookRepository;

    @Override
    public BookInfo search(String isbn, String provider, String tool) throws ScraperException {
        String validIsbn = ISBNValidator.getInstance(false).validate(isbn);

        BookInfo bookInfo;
        if (validIsbn != null) {
            BookInfoProvider resolvedProvider = providerResolver.resolve(provider);
            Scraper resolvedScraper = scraperResolver.resolve(tool);
            bookInfo = bookRepository.search(validIsbn, resolvedProvider, resolvedScraper);
        } else {
            throw new ScraperException(String.format("Received invalid ISBN [%s]", isbn));
        }
        return bookInfo;
    }

}
