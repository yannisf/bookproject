package bookproject.service;

import bookproject.persistence.BookInformation;
import bookproject.persistence.repository.BookInformationRepository;
import bookproject.scraper.BookInformationMapper;
import bookproject.scraper.api.*;
import bookproject.scraper.impl.ScraperResolver;
import bookproject.scraper.provider.ProviderResolver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class BookInfoServiceImpl implements BookInfoService {

    @Autowired
    private ScraperResolver scraperResolver;

    @Autowired
    private ProviderResolver providerResolver;

    @Autowired
    private IsbnService isbnService;

    @Autowired
    private BookInformationMapper bookInformationMapper;

    @Autowired
    private BookInformationRepository bookInformationRepository;

    @Override
    @Cacheable(value = "books", key = "#isbn + ':' + #provider")
    public @NonNull
    BookInformationValue search(@NonNull String isbn,
                                @NonNull String provider,
                                @NonNull String tool)
            throws ScraperException, UnknownProviderException, UnknownToolException, InvalidIsbnException {

        String validIsbn = isbnService.clean(isbn);

        if (validIsbn != null) {
            BookInfoProvider resolvedProvider = providerResolver.resolve(provider);
            Optional<BookInformation> optionalBookInformation = find(validIsbn, resolvedProvider.getName());

            if (optionalBookInformation.isPresent()) {
                return bookInformationMapper.toValue(optionalBookInformation.get());
            } else {
                Scraper resolvedScraper = scraperResolver.resolve(tool);
                BookInformationValue bookInformationValue = resolvedScraper.scrape(resolvedProvider, validIsbn);
                //TODO: This should be done in an aspect
                store(bookInformationValue);
                return bookInformationValue;
            }
        } else {
            throw new InvalidIsbnException(String.format("Received invalid ISBN [%s]", isbn));
        }
    }

    private Optional<BookInformation> find(String validIsbn, String provider) throws InvalidIsbnException {
        if (isbnService.isIsbn10(validIsbn)) {
            return bookInformationRepository.findByIsbnAndProvider(validIsbn, provider);
        } else if (isbnService.isIsbn13(validIsbn)) {
            return bookInformationRepository.findByIsbn13AndProvider(validIsbn, provider);
        } else {
            throw new InvalidIsbnException("ISBN can only be a valid ISBN10 or ISBN13");
        }
    }

    private void store(BookInformationValue bookInformationValue) {
        BookInformation bookInformation = bookInformationMapper.fromValue(bookInformationValue);
        bookInformationRepository.save(bookInformation);
    }

}
