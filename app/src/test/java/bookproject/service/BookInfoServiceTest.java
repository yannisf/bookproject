package bookproject.service;

import bookproject.persistence.repository.BookInformationRepository;
import bookproject.scraper.BookInformationMapper;
import bookproject.scraper.api.*;
import bookproject.scraper.impl.ScraperResolver;
import bookproject.scraper.provider.ProviderResolver;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.ThrowableAssert.catchThrowable;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class BookInfoServiceTest {

    @Spy
    private ScraperResolver scraperResolver = new ScraperResolver();

    @Mock
    private ProviderResolver providerResolver;// = new ProviderResolver();

    @Spy
    private IsbnService isbnService = new IsbnServiceImpl();

    @Spy
    private BookInformationMapper bookInformationMapper = new BookInformationMapper();

    @Mock
    private BookInformationRepository bookInformationRepository;

    @Spy
    @InjectMocks
    private BookInfoService bookInfoService = new BookInfoServiceImpl();

    @Test
    public void testInvalidIsbn() throws UnknownToolException, ScraperException, UnknownProviderException, InvalidIsbnException {
        Throwable invalidIsbn = catchThrowable(() -> bookInfoService.search("X", "Y", "Z"));
        assertThat(invalidIsbn).isInstanceOf(InvalidIsbnException.class).hasMessage("Received invalid ISBN [X]");
    }

    @Test
    public void testSearch() throws ScraperException, UnknownToolException, UnknownProviderException, InvalidIsbnException {
        String isbn = "9600342687";

        BookInfoProvider bookInfoProvider = mock(BookInfoProvider.class);
        when(providerResolver.resolve(anyString())).thenReturn(bookInfoProvider);

        Scraper scraper = mock(Scraper.class);
//        when(scraper.scrape(any(), eq(isbn))).thenReturn(bookInformationValue);

        when(scraperResolver.resolve(anyString())).thenReturn(scraper);

        BookInformationValue result = bookInfoService.search(isbn, "Y", "Z");

        assertThat(result.getIsbn()).isEqualTo("9600342687");
        assertThat(result.getAuthor()).isEqualTo("A");
        assertThat(result.getProvider()).isEqualTo("P");
        assertThat(result.getPublisher()).isEqualTo("T");
    }

}