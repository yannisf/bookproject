package bookproject.scraper.api;

/**
 * Interface to be implemented by scraper implementations.
 */
public interface Scraper {


    /**
     * Scrapes book information provider for ISBN.
     * @param provider the book information provider
     * @param isbn the ISBN to retrieve
     * @return book information
     * @throws ScraperException exception while scraping
     */
    BookInformationValue scrape(BookInfoProvider provider, String isbn) throws ScraperException;

}
