package bookproject.scraper;

import bookproject.persistence.BookInformation;
import bookproject.scraper.api.BookInformationValue;
import bookproject.service.IsbnService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import static org.apache.commons.lang3.StringUtils.isNotBlank;

@Component
public class BookInformationMapper {

    @Autowired
    private IsbnService isbnService;

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
