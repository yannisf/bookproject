package bookproject.service;

import bookproject.scraper.api.*;
import bookproject.scraper.impl.ScraperResolver;
import bookproject.scraper.provider.ProviderResolver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;

/**
 * {@inheritDoc}
 */
@Service
public class BookInformationServiceImpl implements BookInformationService {

    @Autowired
    private ScraperResolver scraperResolver;

    @Autowired
    private ProviderResolver providerResolver;

    /**
     * {@inheritDoc}
     */
    @Override
    @Cacheable(value = "books", key = "#isbn + ':' + #provider")
    public @NonNull
    BookInformationValue search(@NonNull String isbn,
                                @NonNull String provider,
                                @NonNull String tool)
            throws ScraperException, UnknownProviderException, UnknownToolException {

        BookInformationProvider resolvedProvider = providerResolver.resolve(provider);
        Scraper resolvedScraper = scraperResolver.resolve(tool);
        return resolvedScraper.scrape(resolvedProvider, isbn);

    }

}
