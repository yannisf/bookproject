package bookproject.service;

import bookproject.scraper.api.BookInformationValue;
import bookproject.scraper.api.ScraperException;

/**
 * Book information service.
 */
public interface BookInformationService {

    /**
     * Searches for book information.
     *
     * @param isbn     the isbn
     * @param provider the provider
     * @param tool     the scraping tool
     * @return the book information
     * @throws ScraperException thrown when scraping could not succeed
     */
    BookInformationValue search(String isbn, String provider, String tool) throws ScraperException;

}
