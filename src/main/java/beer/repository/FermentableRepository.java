package beer.repository;

import beer.entity.Fermentable;
import org.springframework.data.repository.CrudRepository;

public interface FermentableRepository extends CrudRepository<Fermentable, Long> {
}
