package bookproject.scraper.provider;

import bookproject.scraper.api.BookInfoProvider;
import bookproject.scraper.api.UnknownProviderException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.stream.Stream;

/**
 * Resolves {@link BookInfoProvider} from name;
 */
@Component
public class ProviderResolver {

    private static final Logger LOG = LoggerFactory.getLogger(ProviderResolver.class);

    @Autowired
    private BookInfoProvider[] providers;


    public BookInfoProvider getProvider(String provider) throws UnknownProviderException {
        LOG.info("Trying to resolve BookInfoProvider [{}]", provider);
        return Stream.of(providers)
                .filter(p -> p.getName().equals(provider))
                .findFirst()
                .orElseThrow(UnknownProviderException::new);
    }

}
