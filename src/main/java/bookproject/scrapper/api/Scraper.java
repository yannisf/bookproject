package bookproject.scrapper.api;

/**
 * Interface to be implemented by scraper implementations.
 */
public interface Scraper {

    String BOOK_LINK_FROM_RESULT_EXPRESSION = "string(//a[@class=\"booklink\"][1]/@href)";
    String TITLE_EXPRESSION = "string(//h1[@class=\"book_title\"])";
    String AUTHOR_EXPRESSION = "string(//a[@class=\"booklink\" and starts-with(@href,\"/author/\")][1])";
    String PUBLISHER_EXPRESSION = "string(//a[@class=\"booklink\" and starts-with(@href,\"/com/\")][1])";
    String BASE_URL = "http://www.biblionet.gr";
    String SEARCH_FORMAT = BASE_URL + "/main.asp?page=results&isbn=%s";

    /**
     * Scrapes.
     * @param isbn the ISBN to retrieve
     * @return book information
     * @throws ScraperException
     */
    BookInfo scrape(String isbn) throws ScraperException;

}
