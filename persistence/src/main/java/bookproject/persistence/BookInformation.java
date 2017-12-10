package bookproject.persistence;

import lombok.*;

import javax.persistence.Entity;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@EqualsAndHashCode(callSuper = true)
public class BookInformation extends BaseEntity {

    private static final long serialVersionUID = 3134094330449577628L;

    private String isbn;
    private String isbn13;
    private String title;
    private String author;
    private String publisher;
    private String provider;
    private String url;

}
