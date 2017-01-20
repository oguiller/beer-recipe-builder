package beer.repository;


import beer.entity.Yeast;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface YeastRepository extends CrudRepository<Yeast, Long> {
}
