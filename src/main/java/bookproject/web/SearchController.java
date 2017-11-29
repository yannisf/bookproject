package bookproject.web;

import bookproject.repository.BookRepository;
import bookproject.scraper.api.BookInfo;
import bookproject.scraper.api.BookInfoProvider;
import bookproject.scraper.api.Scraper;
import bookproject.scraper.api.ScraperException;
import bookproject.scraper.impl.ScraperResolver;
import bookproject.scraper.provider.ProviderResolver;
import org.apache.commons.validator.routines.ISBNValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * REST controller to provide scraping facilities.
 */
@RestController
@RequestMapping("/search")
public class SearchController {

    private static final Logger LOG = LoggerFactory.getLogger(SearchController.class);

    @Autowired
    private ScraperResolver scraperResolver;

    @Autowired
    private ProviderResolver providerResolver;

    @Autowired
    private BookRepository bookRepository;

    /**
     * Searches for book information.
     *
     * @param isbn     the isbn
     * @param provider the id of the source of the book information
     * @param scraper  the id of the scraper tool to use
     * @return book information
     * @throws ScraperException thrown when scraping could not succeed
     */
    @GetMapping(produces = "application/json")
    public BookInfo searchForJson(@RequestParam("isbn") String isbn,
                                  @RequestParam(value = "provider", defaultValue = "politeianet") String provider,
                                  @RequestParam(value = "scraper", defaultValue = "tidy") String scraper)
            throws ScraperException {
        LOG.debug("Request parameters: isbn[{}], provider[{}], scraper[{}]", isbn, provider, scraper);

        String validIsbn = ISBNValidator.getInstance(false).validate(isbn);

        BookInfo bookInfo;
        if (validIsbn != null) {
            BookInfoProvider resolvedProvider = providerResolver.resolve(provider);
            Scraper resolvedScraper = scraperResolver.resolve(scraper);
            bookInfo = bookRepository.search(validIsbn, resolvedProvider, resolvedScraper);
        } else {
            throw new ScraperException(String.format("Received invalid ISBN [%s]", isbn));
        }
        return bookInfo;
    }

    /**
     * Searches for book information.
     *
     * @param isbn     the isbn
     * @param provider the id of the source of the book information
     * @param scraper  the id of the scraper tool to use
     * @return a string respensentation of a book information
     * @throws ScraperException thrown when scraping could not succeed
     */
    @GetMapping(produces = "text/plain")
    public String searchForString(@RequestParam("isbn") String isbn,
                                  @RequestParam(value = "provider", defaultValue = "politeianet") String provider,
                                  @RequestParam(value = "scraper", defaultValue = "tidy") String scraper)
            throws ScraperException {
        BookInfo bookInfo = this.searchForJson(isbn, provider, scraper);
        return String.format("%s: %s, %s, %s",
                bookInfo.getIsbn(),
                bookInfo.getTitle(),
                bookInfo.getAuthor(),
                bookInfo.getPublisher());
    }


}
