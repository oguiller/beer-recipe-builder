package beer.repository;

import beer.entity.Fermentable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FermentableRepository extends CrudRepository<Fermentable, Long> {
}
