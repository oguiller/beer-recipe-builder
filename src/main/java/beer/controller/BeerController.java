package beer.controller;

import java.util.concurrent.atomic.AtomicLong;

import beer.entity.Type;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import beer.entity.Beer;

@RestController
@RequestMapping("/api/beer")
public class BeerController {

    private static final String template = "Beer Type is: %s!";
    private final AtomicLong counter = new AtomicLong();

    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public Beer beer(@RequestParam(value="type", defaultValue="I.P.A") String type) {
        Beer beer = new Beer();
        beer.setType(Type.ALL_GRAIN);
        return beer;
    }
}
