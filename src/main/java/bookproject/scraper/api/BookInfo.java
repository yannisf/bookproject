package bookproject.scraper.api;


import lombok.Builder;
import lombok.Data;

/**
 * Encapsulates the scraped book information.
 */
@Data
@Builder
public class BookInfo {

    private String isbn;
    private String title;
    private String author;
    private String publisher;
    private String sourceUrl;

}
