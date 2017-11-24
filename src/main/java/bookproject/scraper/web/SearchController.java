package bookproject.scraper.web;

import bookproject.scraper.api.BookInfo;
import bookproject.scraper.api.BookInfoProvider;
import bookproject.scraper.api.Scraper;
import bookproject.scraper.api.ScraperException;
import org.apache.commons.validator.routines.ISBNValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/search")
public class SearchController {

    private static final Logger LOG = LoggerFactory.getLogger(SearchController.class);

    @Autowired
    @Qualifier("tidyScraper")
    private Scraper scraper;

    @Autowired
    @Qualifier("politeianet")
    private BookInfoProvider provider;

    @GetMapping()
    public String search(@RequestParam(value = "isbn") String isbn) throws ScraperException {
        BookInfo bookInfo;
        String validIsbn = ISBNValidator.getInstance().validate(isbn);

        if (validIsbn != null) {
            LOG.info("Scrapping for ISBN [{}] using provider [{}]", validIsbn, provider.getName());
            bookInfo = scraper.scrape(provider, isbn);
        } else {
            throw new ScraperException(String.format("Received invalid ISBN [%s]", isbn));
        }
        return String.format("%s: %s, %s, %s", bookInfo.getIsbn(), bookInfo.getTitle(), bookInfo.getAuthor(), bookInfo.getPublisher());
    }

}