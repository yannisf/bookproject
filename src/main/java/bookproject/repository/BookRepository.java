package bookproject.repository;

import bookproject.scraper.api.BookInfo;
import bookproject.scraper.api.BookInfoProvider;
import bookproject.scraper.api.Scraper;
import bookproject.scraper.api.ScraperException;

public interface BookRepository {

    BookInfo search(String isbn, BookInfoProvider provider, Scraper scraper) throws ScraperException;

}
