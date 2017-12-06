package bookproject.service;

import bookproject.scraper.api.*;

public interface BookInfoService {
    BookInformationValue search(String isbn, String provider, String tool)
            throws ScraperException, UnknownProviderException, UnknownScraperException, InvalidIsbnException;
}
