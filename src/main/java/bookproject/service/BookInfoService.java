package bookproject.service;

import bookproject.scraper.api.BookInfo;
import bookproject.scraper.api.ScraperException;

public interface BookInfoService {
    BookInfo search(String isbn, String provider, String tool) throws ScraperException;
}
