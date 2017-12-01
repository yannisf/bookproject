package bookproject.persistence;

import lombok.Builder;
import lombok.Data;

import javax.persistence.Entity;

@Entity
@Data
@Builder
public class BookInformation extends BaseEntity {

    private String isbn;

    private String isbn13;

    private String title;

    private String author;

    private String publisher;

    private String provider;

    private String url;

}
