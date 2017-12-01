package bookproject.web;

import bookproject.scraper.api.*;
import bookproject.scraper.impl.ScraperResolver;
import bookproject.scraper.provider.ProviderResolver;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.ThrowableAssert.catchThrowable;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class SearchControllerTest {

    @Mock
    private ScraperResolver scraperResolver;

    @Mock
    private ProviderResolver providerResolver;

    @InjectMocks
    private SearchController searchController;

    @Test
    public void testInvalidIsbn() throws ScraperException {
        Throwable invalidIsbn = catchThrowable(() -> searchController.searchForString("X", "Y", "Z"));

        assertThat(invalidIsbn)
                .isInstanceOf(ScraperException.class)
                .hasMessageStartingWith("Received invalid ISBN");

    }

    @Test
    public void testInvalidProvider() throws ScraperException {
        when(providerResolver.resolve(anyString())).thenThrow(new UnknownProviderException());
        Throwable invalidIsbn = catchThrowable(() -> searchController.searchForString("960-03-4268-7", "Y", "Z"));

        assertThat(invalidIsbn)
                .isInstanceOf(UnknownProviderException.class);

    }

    @Test
    public void testInvalidScraper() throws ScraperException {
        when(providerResolver.resolve(anyString())).thenReturn(mock(BookInfoProvider.class));
        when(scraperResolver.resolve(anyString())).thenThrow(new UnknownScraperException("Unknown tool"));
        Throwable invalidIsbn = catchThrowable(() -> searchController.searchForString("960-03-4268-7", "Y", "Z"));

        assertThat(invalidIsbn)
                .isInstanceOf(UnknownScraperException.class)
                .hasMessageStartingWith("Unknown tool");
    }

    @Test
    public void testSearch() throws ScraperException {
        String isbn = "9600342687";
        BookInfoValue bookInfoValue = BookInfoValue.builder()
                .isbn(isbn)
                .author("A")
                .title("T")
                .publisher("P")
                .build();

        BookInfoProvider bookInfoProvider = mock(BookInfoProvider.class);
        when(providerResolver.resolve(anyString())).thenReturn(bookInfoProvider);

        Scraper scraper = mock(Scraper.class);
        when(scraper.scrape(any(), eq(isbn))).thenReturn(bookInfoValue);

        when(scraperResolver.resolve(anyString())).thenReturn(scraper);

        String result = searchController.searchForString(isbn, "Y", "Z");

        assertThat(result).isEqualTo("9600342687: T, A, P");
    }

}