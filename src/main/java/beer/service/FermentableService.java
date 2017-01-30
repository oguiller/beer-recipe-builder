package beer.service;

import beer.entity.Fermentable;
import beer.repository.FermentableRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class FermentableService {

    @Autowired
    FermentableRepository fermentableRepository;

    public Fermentable save(Fermentable fermentable) {
        return fermentableRepository.save(fermentable);
    }

    public void delete(long id) {
        fermentableRepository.delete(id);
    }

    public Fermentable findOne(long id) {
        return fermentableRepository.findOne(id);
    }

    public Iterable findAll() {
        return fermentableRepository.findAll();
    }
}
