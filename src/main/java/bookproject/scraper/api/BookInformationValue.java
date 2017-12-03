package bookproject.scraper.api;


import lombok.Builder;
import lombok.Value;

/**
 * Encapsulates the scraped book information.
 */
@Value
@Builder
public class BookInformationValue {

    private String isbn;
    private String title;
    private String author;
    private String publisher;
    private String provider;
    private String sourceUrl;

}
