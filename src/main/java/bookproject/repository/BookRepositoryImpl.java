package bookproject.repository;

import bookproject.scraper.api.BookInfo;
import bookproject.scraper.api.BookInfoProvider;
import bookproject.scraper.api.Scraper;
import bookproject.scraper.api.ScraperException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Repository;

@Repository
public class BookRepositoryImpl implements BookRepository {

    private static final Logger LOG = LoggerFactory.getLogger(BookRepositoryImpl.class);

    @Override
    @Cacheable("books")
    public BookInfo search(String isbn, BookInfoProvider provider, Scraper scraper) throws ScraperException {
        LOG.debug("Looking for ISBN [{}], using provider [{}], using scraper [{}]",
                isbn,
                provider.getName(),
                scraper);
        return scraper.scrape(provider, isbn);
    }

}
