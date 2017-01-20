package beer.controller;

import beer.entity.Fermentable;
import beer.repository.FermentableRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/fermentable")
public class FermentableController {

    @Autowired
    FermentableRepository fermentableRepository;

    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public
    @ResponseBody
    Map findOne(@PathVariable long id) {
        Map result = new HashMap();
        Fermentable fermentable = fermentableRepository.findOne(id);
        result.put("fermentable", fermentable);

        return result;
    }
}
