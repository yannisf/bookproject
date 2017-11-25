package bookproject.scraper.web;

import bookproject.scraper.api.ScraperException;
import org.junit.Test;

public class SearchControllerTest {

    @Test(expected = ScraperException.class)
    public void test() throws ScraperException {
        SearchController searchController = new SearchController();
        searchController.search("X", null, null);
    }

}