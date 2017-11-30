package bookproject;

import lombok.Builder;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.io.Serializable;

@Entity
@Data
@Builder
public class BookInformation implements Serializable {

    @Id
    @GeneratedValue
    private Long id;

    private String isbn;

    private String isbn13;

    private String title;

    private String author;

    private String publisher;

    private String url;

}
