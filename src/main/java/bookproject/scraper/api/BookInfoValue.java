package bookproject.scraper.api;


import lombok.Builder;
import lombok.Data;
import lombok.Value;

/**
 * Encapsulates the scraped book information.
 */
@Value
@Builder
public class BookInfoValue {

    private String isbn;
    private String title;
    private String author;
    private String publisher;
    private String sourceUrl;

}
