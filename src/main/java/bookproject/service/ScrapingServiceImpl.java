package bookproject.service;

import bookproject.scraper.api.BookInfoValue;
import bookproject.scraper.api.BookInfoProvider;
import bookproject.scraper.api.Scraper;
import bookproject.scraper.api.ScraperException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

@Repository
public class ScrapingServiceImpl implements ScrapingService {

    private static final Logger LOG = LoggerFactory.getLogger(ScrapingServiceImpl.class);

    @Override
    public BookInfoValue search(String isbn, BookInfoProvider provider, Scraper scraper) throws ScraperException {
        LOG.debug("Looking for ISBN [{}], using provider [{}], using scraper [{}]",
                isbn,
                provider.getName(),
                scraper);
        return scraper.scrape(provider, isbn);
    }

}
