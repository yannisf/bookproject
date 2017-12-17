package bookproject.scraper.provider;

import bookproject.scraper.api.BookInformationProvider;
import bookproject.scraper.api.ScraperException;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.ThrowableAssert.catchThrowable;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ProviderResolverTest {

    @Test
    public void resolveTest() throws ScraperException {
        BookInformationProvider provider1 = mock(BookInformationProvider.class);
        when(provider1.getName()).thenReturn("provider1");

        BookInformationProvider provider2 = mock(BookInformationProvider.class);
        when(provider2.getName()).thenReturn("provider2");

        ProviderResolver providerResolver = new ProviderResolver();
        providerResolver.providers = new BookInformationProvider[]{provider1, provider2};

        assertThat(providerResolver.resolve("provider1")).isEqualTo(provider1);
        assertThat(providerResolver.resolve("provider2")).isEqualTo(provider2);

        Throwable thrown = catchThrowable(() -> providerResolver.resolve("provider3"));

        assertThat(thrown).isInstanceOf(ScraperException.class);
    }

}
