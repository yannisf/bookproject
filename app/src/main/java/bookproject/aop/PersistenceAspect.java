package bookproject.aop;

import bookproject.persistence.BookInformation;
import bookproject.persistence.repository.BookInformationRepository;
import bookproject.scraper.BookInformationMapper;
import bookproject.scraper.api.BookInformationProvider;
import bookproject.scraper.api.BookInformationValue;
import bookproject.scraper.api.ErrorCode;
import bookproject.scraper.api.ScraperException;
import bookproject.scraper.provider.ProviderResolver;
import bookproject.service.IsbnService;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;

/**
 * Persistence cross cutting concerns.
 */
@Aspect
@Component
public class PersistenceAspect {

    private static final Logger LOG = LoggerFactory.getLogger(PersistenceAspect.class);

    @Autowired
    private BookInformationRepository bookInformationRepository;

    @Autowired
    private ProviderResolver providerResolver;

    @Autowired
    private BookInformationMapper bookInformationMapper;

    @Autowired
    private IsbnService isbnService;

    @Pointcut("execution(public * bookproject.service.BookInformationService.search(String, String, String))")
    private void search() {
        //Pointcut definition
    }

    @Pointcut("execution(public * bookproject.scraper.api.Scraper.scrape(..))")
    private void scrape() {
        //Pointcut definition
    }

    /**
     * Intercepts searches, and checks first if they are already cached  in the database.
     *
     * @param pjp the joinpoint
     * @return if found, the respective value object, the proceeding value
     * @throws Throwable possible throwable
     */
    @Around(value = "search()")
    public BookInformationValue find(ProceedingJoinPoint pjp) throws Throwable {
        String provider = (String) pjp.getArgs()[1];
        BookInformationProvider resolvedProvider = providerResolver.resolve(provider);

        String isbn = (String) pjp.getArgs()[0];
        Optional<BookInformation> optionalBookInformation = findOptional(isbn, resolvedProvider.getName());

        if (optionalBookInformation.isPresent()) {
            LOG.debug("Book information [{}] provided by [{}] found in the database", isbn, provider);
            return bookInformationMapper.toValue(optionalBookInformation.get());
        } else {
            return (BookInformationValue) pjp.proceed();
        }
    }

    private Optional<BookInformation> findOptional(String validIsbn, String provider) throws ScraperException {
        if (isbnService.isIsbn10(validIsbn)) {
            return bookInformationRepository.findByIsbnAndProvider(validIsbn, provider);
        } else if (isbnService.isIsbn13(validIsbn)) {
            return bookInformationRepository.findByIsbn13AndProvider(validIsbn, provider);
        } else {
            throw new ScraperException(ErrorCode.INVALID_ISBN);
        }
    }

    /**
     * Intercepts scapings to cache their {@link BookInformationValue} result in the database.
     *
     * @param bookInformationValue the value to be cached in the database
     */
    @AfterReturning(value = "scrape()", returning = "bookInformationValue")
    public void store(BookInformationValue bookInformationValue) {
        LOG.info("Storing [{}]", bookInformationValue);
        BookInformation bookInformation = bookInformationMapper.fromValue(bookInformationValue);
        bookInformationRepository.save(bookInformation);
    }

}
