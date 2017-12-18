package bookproject.persistence.specifications;

import bookproject.persistence.BookInformation;
import bookproject.persistence.BookInformation_;
import org.springframework.data.jpa.domain.Specification;

public class BookInformationSpecs {

    public static Specification<BookInformation> isbn10(String isbn) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get(BookInformation_.isbn), isbn);
    }

    public static Specification<BookInformation> isbn13(String isbn) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get(BookInformation_.isbn13), isbn);
    }

    public static Specification<BookInformation> provider(String provider) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get(BookInformation_.provider), provider);
    }

}
