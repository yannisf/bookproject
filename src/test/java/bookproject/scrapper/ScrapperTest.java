package bookproject.scrapper;

import bookproject.scrapper.api.BookInfo;
import bookproject.scrapper.api.Scraper;
import bookproject.scrapper.api.ScraperException;
import bookproject.scrapper.impl.HtmlUnitScraper;
import bookproject.scrapper.impl.TidyScraper;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class ScrapperTest {

    private static final String isbn = "9789600316698";
    private static final String expectedTitle = "Το λάθος";
    private static final String expectedAuthor = "Αντώνης Σαμαράκης";
    private static final String expectedPublisher = "Εκδόσεις Καστανιώτη";

    @Test
    public void tidyTest() throws ScraperException {
        Scraper scraper = new TidyScraper();
        BookInfo bookInfo = scraper.scrape(isbn);
        System.out.println(bookInfo);
        performAssertions(bookInfo);
    }

    @Test
    public void htmlUnitTest() throws ScraperException {
        Scraper scraper = new HtmlUnitScraper();
        BookInfo bookInfo = scraper.scrape(isbn);
        System.out.println(bookInfo);
        performAssertions(bookInfo);
    }

    private void performAssertions(BookInfo bookInfo) {
        assertThat(bookInfo.getIsbn()).isEqualTo(isbn);
        assertThat(bookInfo.getTitle()).isEqualTo(expectedTitle);
        assertThat(bookInfo.getAuthor()).isEqualTo(expectedAuthor);
        assertThat(bookInfo.getPublisher()).isEqualTo(expectedPublisher);
    }

}
