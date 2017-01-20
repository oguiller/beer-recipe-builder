package beer.controller;

import beer.entity.Yeast;
import beer.repository.YeastRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/yeast")
public class YeastController {

    @Autowired
    YeastRepository yeastRepository;

    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public
    @ResponseBody
    Map findOne(@PathVariable long id) {
        Map result = new HashMap();
        Yeast yeast = yeastRepository.findOne(id);
        result.put("yeast", yeast);

        return result;
    }
}
