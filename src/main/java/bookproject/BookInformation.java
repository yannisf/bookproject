package bookproject;

import lombok.Builder;
import lombok.Data;

import javax.persistence.Entity;

@Entity
@Data
@Builder
public class BookInformation {

    private Long id;
    private String isbn;
    private String isbn13;
    private String title;
    private String author;
    private String publisher;
    private String url;

}
