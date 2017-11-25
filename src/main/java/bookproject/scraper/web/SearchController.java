package bookproject.scraper.web;

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

@RestController
@RequestMapping("/search")
public class SearchController {

    private static final Logger LOG = LoggerFactory.getLogger(SearchController.class);

    @Autowired
    private ScraperResolver scraperResolver;

    @Autowired
    private ProviderResolver providerResolver;

    @GetMapping()
    public String search(@RequestParam("isbn") String isbn,
                         @RequestParam(value = "provider", defaultValue = "politeianet") String provider,
                         @RequestParam(value = "scraper", defaultValue = "tidy") String scraper)
            throws ScraperException {

        BookInfo bookInfo;

        String validIsbn = ISBNValidator.getInstance().validate(isbn);
        BookInfoProvider resolvedProvider = providerResolver.getProvider(provider);
        Scraper resolvedScraper = scraperResolver.resolve(scraper);

        if (validIsbn != null) {
            LOG.info("Scrapping for ISBN [{}] using provider [{}]", validIsbn, resolvedProvider.getName());
            bookInfo = resolvedScraper.scrape(resolvedProvider, validIsbn);
        } else {
            throw new ScraperException(String.format("Received invalid ISBN [%s]", isbn));
        }
        return String.format("%s: %s, %s, %s",
                bookInfo.getIsbn(),
                bookInfo.getTitle(),
                bookInfo.getAuthor(),
                bookInfo.getPublisher());
    }

}
