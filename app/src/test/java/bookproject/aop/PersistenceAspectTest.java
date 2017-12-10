package bookproject.aop;

import bookproject.persistence.BookInformation;
import bookproject.persistence.repository.BookInformationRepository;
import bookproject.scraper.BookInformationMapper;
import bookproject.scraper.api.BookInformationProvider;
import bookproject.scraper.api.BookInformationValue;
import bookproject.scraper.api.InvalidIsbnException;
import bookproject.scraper.provider.ProviderResolver;
import bookproject.service.IsbnService;
import bookproject.service.IsbnServiceImpl;
import org.aspectj.lang.ProceedingJoinPoint;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.ThrowableAssert.catchThrowable;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class PersistenceAspectTest {

    @Mock
    private BookInformationRepository bookInformationRepository;

    @Mock
    private ProviderResolver providerResolver;

    @Mock
    private BookInformationMapper bookInformationMapper;

    @Spy
    private IsbnService isbnService = new IsbnServiceImpl();

    @InjectMocks
    private PersistenceAspect persistenceAspect;

    @Test
    public void findInvalidIsbn() throws Throwable {
        ProceedingJoinPoint pjp = mock(ProceedingJoinPoint.class);
        when(pjp.getArgs()).thenReturn(new String[]{"ISBN", "PROVIDER"});
        BookInformationProvider provider = mock(BookInformationProvider.class);
        when(providerResolver.resolve(eq("PROVIDER"))).thenReturn(provider);
        Throwable thrown = catchThrowable(() -> persistenceAspect.find(pjp));
        assertThat(thrown).isInstanceOf(InvalidIsbnException.class);
    }

    @Test
    public void findIsbn10NotExists() throws Throwable {
        ProceedingJoinPoint pjp = mock(ProceedingJoinPoint.class);
        String isbn = "9600342687";
        when(pjp.getArgs()).thenReturn(new String[]{isbn, "PROVIDER"});
        BookInformationProvider provider = mock(BookInformationProvider.class);
        when(providerResolver.resolve(eq("PROVIDER"))).thenReturn(provider);
        when(provider.getName()).thenReturn("PROVIDER_NAME");

        when(bookInformationRepository.findByIsbnAndProvider(eq(isbn), eq("PROVIDER_NAME")))
                .thenReturn(Optional.empty());

        BookInformationValue bookInformationValue = persistenceAspect.find(pjp);
        verify(bookInformationMapper, never()).toValue(any());
        verify(pjp, times(1)).proceed();
    }

    @Test
    public void findIsbn10Exists() throws Throwable {
        ProceedingJoinPoint pjp = mock(ProceedingJoinPoint.class);
        String isbn = "9600342687";
        when(pjp.getArgs()).thenReturn(new String[]{isbn, "PROVIDER"});
        BookInformationProvider provider = mock(BookInformationProvider.class);
        when(providerResolver.resolve(eq("PROVIDER"))).thenReturn(provider);
        when(provider.getName()).thenReturn("PROVIDER_NAME");

        BookInformation bookInformation = BookInformation.builder().isbn(isbn).build();
        when(bookInformationRepository.findByIsbnAndProvider(eq(isbn), eq("PROVIDER_NAME")))
                .thenReturn(Optional.of(bookInformation));

        when(bookInformationMapper.toValue(eq(bookInformation)))
                .thenReturn(BookInformationValue.builder().isbn(isbn).build());

        BookInformationValue bookInformationValue = persistenceAspect.find(pjp);
        verify(bookInformationMapper, times(1)).toValue(eq(bookInformation));
        verify(pjp, never()).proceed();
        assertThat(bookInformationValue.getIsbn()).isEqualTo(isbn);
    }

    @Test
    public void findIsbn13Exists() throws Throwable {
        ProceedingJoinPoint pjp = mock(ProceedingJoinPoint.class);
        String isbn = "9789600316698";
        when(pjp.getArgs()).thenReturn(new String[]{isbn, "PROVIDER"});
        BookInformationProvider provider = mock(BookInformationProvider.class);
        when(providerResolver.resolve(eq("PROVIDER"))).thenReturn(provider);
        when(provider.getName()).thenReturn("PROVIDER_NAME");

        BookInformation bookInformation = BookInformation.builder().isbn13(isbn).build();
        when(bookInformationRepository.findByIsbn13AndProvider(eq(isbn), eq("PROVIDER_NAME")))
                .thenReturn(Optional.of(bookInformation));

        when(bookInformationMapper.toValue(eq(bookInformation)))
                .thenReturn(BookInformationValue.builder().isbn(isbn).build());

        BookInformationValue bookInformationValue = persistenceAspect.find(pjp);
        verify(bookInformationMapper, times(1)).toValue(eq(bookInformation));
        verify(pjp, never()).proceed();
        assertThat(bookInformationValue.getIsbn()).isEqualTo(isbn);
    }

    @Test
    public void storeTest() {
        BookInformationValue bookInformationValue = BookInformationValue.builder().build();
        BookInformation bookInformation = BookInformation.builder().build();
        when(bookInformationMapper.fromValue(eq(bookInformationValue))).thenReturn(bookInformation);
        when(bookInformationRepository.save(eq(bookInformation))).thenReturn(bookInformation);
        persistenceAspect.store(bookInformationValue);
    }
}