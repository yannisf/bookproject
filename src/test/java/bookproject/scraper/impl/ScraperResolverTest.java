package bookproject.scraper.impl;

import bookproject.scraper.api.Scraper;
import bookproject.scraper.api.Tool;
import bookproject.scraper.api.UnknownScraperException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.ThrowableAssert.catchThrowable;

@RunWith(MockitoJUnitRunner.class)
public class ScraperResolverTest {

    @Mock
    private Scraper tidy;

    @Mock
    private Scraper htmlUnitScraper;

    @InjectMocks
    private ScraperResolver resolver;

    @Test
    public void test() throws UnknownScraperException {
        assertThat(resolver.resolve(Tool.TIDY.id)).isEqualTo(tidy);
        assertThat(resolver.resolve(Tool.HTML_UNIT.id)).isEqualTo(htmlUnitScraper);

        Throwable thrown = catchThrowable(() -> resolver.resolve("x"));

        assertThat(thrown).isInstanceOf(UnknownScraperException.class);
    }

}
