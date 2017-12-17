package bookproject.scraper.provider;

import bookproject.scraper.api.BookInformationProvider;
import bookproject.scraper.api.ErrorCode;
import bookproject.scraper.api.ScraperException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.stream.Stream;

/**
 * Resolves {@link BookInformationProvider} from name.
 */
@Component
public class ProviderResolver {

    private static final Logger LOG = LoggerFactory.getLogger(ProviderResolver.class);

    @Autowired
    BookInformationProvider[] providers;

    /**
     * Resolver method.
     *
     * @param providerName the provider name
     * @return the provider
     * @throws ScraperException thrown when the provider name could not be resolved
     */
    public BookInformationProvider resolve(String providerName) throws ScraperException {
        LOG.debug("Resolving BookInformationProvider [{}]", providerName);
        return Stream.of(providers)
                .filter(p -> p.getName().equals(providerName))
                .findFirst()
                .orElseThrow(() -> new ScraperException(ErrorCode.INVALID_PROVIDER));
    }

}
