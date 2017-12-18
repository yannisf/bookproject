package bookproject.scraper;

import bookproject.scraper.api.BookInformationValue;
import bookproject.scraper.api.Scraper;
import bookproject.scraper.api.ScraperException;
import bookproject.scraper.impl.ExtractionValidator;
import bookproject.scraper.impl.HtmlUnitScraper;
import bookproject.scraper.impl.TidyScraper;
import bookproject.scraper.provider.Biblionet;
import bookproject.service.IsbnService;
import bookproject.service.IsbnServiceImpl;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.text.similarity.JaccardSimilarity;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.assertj.core.api.Assertions.assertThat;


@RunWith(MockitoJUnitRunner.class)
public class ScraperTest {

    private static final Logger LOG = LoggerFactory.getLogger(ScraperTest.class);

    @Spy
    private IsbnService isbnService = new IsbnServiceImpl();

    @Mock
    private ExtractionValidator extractionValidator;

    @InjectMocks
    private Scraper tidyScraper = new TidyScraper();

    @InjectMocks
    private Scraper htmlUnitScraper = new HtmlUnitScraper();


    private static final String isbn = "9789600316698";
    private static final String expectedTitle = "Το λάθος";
    private static final String expectedAuthor = "Αντώνης Σαμαράκης";
    private static final String expectedPublisher = "Εκδόσεις Καστανιώτη";

    @Test
    public void tidyTest() throws ScraperException {
        BookInformationValue bookInformationValue = tidyScraper.scrape(new Biblionet(), isbn);
        performAssertions(bookInformationValue);
    }

    @Test
    public void htmlUnitTest() throws ScraperException {
        BookInformationValue bookInformationValue = htmlUnitScraper.scrape(new Biblionet(), isbn);
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
