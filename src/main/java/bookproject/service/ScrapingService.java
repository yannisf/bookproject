package bookproject.service;

import bookproject.scraper.api.BookInfoValue;
import bookproject.scraper.api.BookInfoProvider;
import bookproject.scraper.api.Scraper;
import bookproject.scraper.api.ScraperException;

public interface ScrapingService {

    BookInfoValue search(String isbn, BookInfoProvider provider, Scraper scraper) throws ScraperException;

}
