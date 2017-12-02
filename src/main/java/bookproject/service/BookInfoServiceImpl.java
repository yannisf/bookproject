package bookproject.service;

import bookproject.persistence.BookInformation;
import bookproject.persistence.repository.BookInformationRepository;
import bookproject.scraper.api.BookInfoProvider;
import bookproject.scraper.api.BookInfoValue;
import bookproject.scraper.api.Scraper;
import bookproject.scraper.api.ScraperException;
import bookproject.scraper.impl.ScraperResolver;
import bookproject.scraper.provider.ProviderResolver;
import org.apache.commons.validator.routines.ISBNValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

@Service
public class BookInfoServiceImpl implements BookInfoService {

    @Autowired
    private ScraperResolver scraperResolver;

    @Autowired
    private ProviderResolver providerResolver;

    @Autowired
    private ScrapingService scrapingService;

    @Autowired
    private BookInformationRepository bookInformationRepository;

    @Override
    @Cacheable(value = "books", key = "#isbn + ':' + #provider")
    public BookInfoValue search(String isbn, String provider, String tool) throws ScraperException {
        String validIsbn = ISBNValidator.getInstance(false).validate(isbn);

        BookInfoValue bookInfoValue;
        if (validIsbn != null) {
            BookInfoProvider resolvedProvider = providerResolver.resolve(provider);
            Scraper resolvedScraper = scraperResolver.resolve(tool);
            bookInfoValue = scrapingService.search(validIsbn, resolvedProvider, resolvedScraper);
        } else {
            throw new ScraperException(String.format("Received invalid ISBN [%s]", isbn));
        }

        BookInformation bookInformation = BookInformation.builder()
                .isbn(bookInfoValue.getIsbn())
                .author(bookInfoValue.getAuthor())
                .publisher(bookInfoValue.getPublisher())
                .title(bookInfoValue.getTitle())
                .provider(provider)
                .build();

        bookInformationRepository.save(bookInformation);

        return bookInfoValue;
    }

}
