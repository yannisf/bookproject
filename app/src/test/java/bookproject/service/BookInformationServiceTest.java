package bookproject.service;

import bookproject.scraper.api.BookInformationProvider;
import bookproject.scraper.api.BookInformationValue;
import bookproject.scraper.api.Scraper;
import bookproject.scraper.api.ScraperException;
import bookproject.scraper.impl.ScraperResolver;
import bookproject.scraper.provider.ProviderResolver;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class BookInformationServiceTest {

    @Mock
    private ScraperResolver scraperResolver;

    @Mock
    private ProviderResolver providerResolver;

    @InjectMocks
    private BookInformationService bookInformationService = new BookInformationServiceImpl();

    @Test
    public void testSearch() throws ScraperException {

        String isbn = "9600342687";

        BookInformationProvider bookInformationProvider = mock(BookInformationProvider.class);
        when(providerResolver.resolve(eq("Y"))).thenReturn(bookInformationProvider);

        Scraper scraper = mock(Scraper.class);
        when(scraper.scrape(eq(bookInformationProvider), eq(isbn))).thenReturn(
                BookInformationValue.builder()
                        .isbn(isbn)
                        .author("A")
                        .title("T")
                        .publisher("P")
                        .provider("PR")
                        .sourceUrl("U")
                        .build()
        );
        when(scraperResolver.resolve(eq("Z"))).thenReturn(scraper);

        BookInformationValue result = bookInformationService.search(isbn, "Y", "Z");

        assertThat(result.getIsbn()).isEqualTo("9600342687");
        assertThat(result.getAuthor()).isEqualTo("A");
        assertThat(result.getTitle()).isEqualTo("T");
        assertThat(result.getPublisher()).isEqualTo("P");
        assertThat(result.getProvider()).isEqualTo("PR");
        assertThat(result.getSourceUrl()).isEqualTo("U");
    }

}