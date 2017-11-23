package bookproject.scraper.web;

import bookproject.scraper.api.BookInfo;
import bookproject.scraper.api.ScraperException;
import bookproject.scraper.impl.TidyScraper;
import bookproject.scraper.provider.Politeianet;
import org.apache.commons.validator.routines.ISBNValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/search")
public class SearchController {

    private static final Logger LOG = LoggerFactory.getLogger(SearchController.class);

    @GetMapping()
    public String search(@RequestParam(value = "isbn") String isbn) throws ScraperException {
        BookInfo bookInfo;
        String validIsbn = ISBNValidator.getInstance().validate(isbn);

        if (validIsbn != null) {
            LOG.info("Scrapping for ISBN [{}]", validIsbn);
            Politeianet provider = new Politeianet();
            LOG.debug("Using provider [{}]", provider.getName());
            bookInfo = new TidyScraper().scrape(provider, isbn);
        } else {
            throw new ScraperException(String.format("Received invalid ISBN [%s]", isbn));
        }
        return String.format("%s: %s, %s, %s", bookInfo.getIsbn(), bookInfo.getTitle(), bookInfo.getAuthor(), bookInfo.getPublisher());
    }

}