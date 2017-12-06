package bookproject.persistence.repository;

import bookproject.persistence.BookInformation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BookInformationRepository extends JpaRepository<BookInformation, Long> {

    Optional<BookInformation> findByIsbnAndProvider(String isbn, String provider);

    Optional<BookInformation> findByIsbn13AndProvider(String isbn, String provider);

}
