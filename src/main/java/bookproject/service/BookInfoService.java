package bookproject.service;

import bookproject.scraper.api.BookInfoValue;
import bookproject.scraper.api.ScraperException;

public interface BookInfoService {
    BookInfoValue search(String isbn, String provider, String tool) throws ScraperException;
}
