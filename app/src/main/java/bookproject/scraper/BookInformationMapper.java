package bookproject.scraper;

import bookproject.persistence.BookInformation;
import bookproject.scraper.api.BookInformationValue;
import bookproject.service.IsbnService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import static org.apache.commons.lang3.StringUtils.isNotBlank;

/**
 * Maps book information from entity to value object and vice versa.
 */
@Component
public class BookInformationMapper {

    @Autowired
    private IsbnService isbnService;

    /**
     * Maps {@link BookInformation} to {@link BookInformationValue}.
     *
     * @param bookInformation the entity
     * @return the mapped value object
     */
    public BookInformationValue toValue(BookInformation bookInformation) {
        return BookInformationValue.builder()
                .author(bookInformation.getAuthor())
                .title(bookInformation.getTitle())
                .provider(bookInformation.getProvider())
                .publisher(bookInformation.getPublisher())
                .sourceUrl(bookInformation.getUrl())
                .isbn(isNotBlank(bookInformation.getIsbn()) ?
                        bookInformation.getIsbn() :
                        bookInformation.getIsbn13())
                .build();
    }

    /**
     * Maps {@link BookInformationValue} to {@link BookInformation}.
     *
     * @param bookInformationValue value object
     * @return the mapped entity
     */
    public BookInformation fromValue(BookInformationValue bookInformationValue) {
        BookInformation bookInformation = BookInformation.builder()
                .author(bookInformationValue.getAuthor())
                .publisher(bookInformationValue.getPublisher())
                .title(bookInformationValue.getTitle())
                .provider(bookInformationValue.getProvider())
                .url(bookInformationValue.getSourceUrl())
                .build();

        if (isbnService.isIsbn10(bookInformationValue.getIsbn())) {
            bookInformation.setIsbn(bookInformationValue.getIsbn());
            bookInformation.setIsbn13(isbnService.convertToIsbn13(bookInformationValue.getIsbn()));
        } else if (isbnService.isIsbn13(bookInformationValue.getIsbn())) {
            bookInformation.setIsbn13(bookInformationValue.getIsbn());
            bookInformation.setIsbn(isbnService.convertToIsbn10(bookInformationValue.getIsbn()));
        }

        return bookInformation;
    }

}
