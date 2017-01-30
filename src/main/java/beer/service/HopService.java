package beer.service;

import beer.entity.Hop;
import beer.repository.HopRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class HopService {

    @Autowired
    HopRepository hopRepository;

    public Hop save(Hop hop) {
        return hopRepository.save(hop);
    }

    public void delete(long id) {
        hopRepository.delete(id);
    }

    public Hop findOne(long id) {
        return hopRepository.findOne(id);
    }

    public Iterable findAll() {
        return hopRepository.findAll();
    }
}
