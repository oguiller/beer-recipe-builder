package beer.service;

import beer.entity.Yeast;
import beer.repository.YeastRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class YeastService {

    @Autowired
    YeastRepository yeastRepository;

    public Yeast save(Yeast yeast) {
        return yeastRepository.save(yeast);
    }

    public void delete(long id) {
        yeastRepository.delete(id);
    }

    public Yeast findOne(long id) {
        return yeastRepository.findOne(id);
    }

    public Iterable findAll() {
        return yeastRepository.findAll();
    }
}