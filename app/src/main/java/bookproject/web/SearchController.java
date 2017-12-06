package bookproject.web;

import bookproject.scraper.api.*;
import bookproject.service.BookInfoService;
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
    private BookInfoService bookInfoService;

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
    public BookInformationValue searchForJson(@RequestParam("isbn") String isbn,
                                              @RequestParam(value = "provider", defaultValue = "politeianet") String provider,
                                              @RequestParam(value = "scraper", defaultValue = "tidy") String scraper)
            throws ScraperException, UnknownToolException, UnknownProviderException, InvalidIsbnException {

        LOG.debug("Request parameters: isbn[{}], provider[{}], scraper[{}]", isbn, provider, scraper);
        return bookInfoService.search(isbn, provider, scraper);
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
            throws ScraperException, UnknownToolException, UnknownProviderException, InvalidIsbnException {
        BookInformationValue bookInformationValue = this.searchForJson(isbn, provider, scraper);
        return String.format("%s: %s, %s, %s",
                bookInformationValue.getIsbn(),
                bookInformationValue.getTitle(),
                bookInformationValue.getAuthor(),
                bookInformationValue.getPublisher());
    }


}
