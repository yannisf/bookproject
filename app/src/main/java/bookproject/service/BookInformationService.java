package bookproject.service;

import bookproject.scraper.api.*;

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
     * @throws ScraperException         thrown when scraping could not success
     * @throws UnknownProviderException thrown then provider could not be resolved
     * @throws UnknownToolException     thrown when scraping tool could not be resolved
     * @throws InvalidIsbnException     thrown when the submitted  ISBN is invalid
     */
    BookInformationValue search(String isbn, String provider, String tool)
            throws ScraperException, UnknownProviderException, UnknownToolException, InvalidIsbnException;

}
