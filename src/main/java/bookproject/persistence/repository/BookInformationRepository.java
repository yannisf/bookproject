package bookproject.persistence.repository;

import bookproject.persistence.BookInformation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BookInformationRepository extends JpaRepository<BookInformation, Long> {


}
