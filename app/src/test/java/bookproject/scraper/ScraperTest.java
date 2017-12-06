package bookproject.scraper;

import bookproject.scraper.api.BookInformationValue;
import bookproject.scraper.api.Scraper;
import bookproject.scraper.api.ScraperException;
import bookproject.scraper.impl.HtmlUnitScraper;
import bookproject.scraper.impl.TidyScraper;
import bookproject.scraper.provider.Politeianet;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.text.similarity.JaccardSimilarity;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.assertj.core.api.Assertions.assertThat;


public class ScraperTest {

    private static final Logger LOG = LoggerFactory.getLogger(ScraperTest.class);

    private static final String isbn = "9789600316698";
    private static final String expectedTitle = "Το λάθος";
    private static final String expectedAuthor = "Αντώνης Σαμαράκης";
    private static final String expectedPublisher = "Εκδόσεις Καστανιώτη";

    @Test
    public void tidyTest() throws ScraperException {
        Scraper scraper = new TidyScraper();
        BookInformationValue bookInformationValue = scraper.scrape(new Politeianet(), isbn);
        System.out.println(bookInformationValue);
        performAssertions(bookInformationValue);
    }

    @Test
    public void htmlUnitTest() throws ScraperException {
        LOG.info("Running htmlUnitTest");
        Scraper scraper = new HtmlUnitScraper();
        BookInformationValue bookInformationValue = scraper.scrape(new Politeianet(), isbn);
        System.out.println(bookInformationValue);
        performAssertions(bookInformationValue);
    }

    private void performAssertions(BookInformationValue bookInformationValue) {
        assertThat(jaccardSimilarity(bookInformationValue.getIsbn(), isbn)).isGreaterThan(0.65);
        assertThat(jaccardSimilarity(bookInformationValue.getAuthor(), expectedAuthor)).isGreaterThan(0.65);
        assertThat(jaccardSimilarity(bookInformationValue.getPublisher(), expectedPublisher)).isGreaterThan(0.65);
        assertThat(jaccardSimilarity(bookInformationValue.getTitle(), expectedTitle)).isGreaterThan(0.65);
    }

    private double jaccardSimilarity(String left, String right) {
        JaccardSimilarity jaccardSimilarity = new JaccardSimilarity();
        return jaccardSimilarity.apply(normalize(left), normalize(right));
    }

    private String normalize(String s) {
        return StringUtils.stripAccents(s.toLowerCase());
    }


}
