package beer.controller;

import beer.entity.Yeast;
import beer.resource.YeastResource;
import beer.service.YeastService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

@RestController
@RequestMapping("/api/yeast")
public class YeastController {

    @Autowired
    YeastService yeastService;

    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public
    @ResponseBody
    HttpEntity<Object> get(@PathVariable long id) {
        Yeast yeast = yeastService.findOne(id);
        YeastResource yeastResource = new YeastResource(yeast);
        yeastResource.add(linkTo(methodOn(YeastController.class).get(((Yeast) yeast).getId())).withSelfRel());
        return new ResponseEntity<>(yeastResource, HttpStatus.OK);
    }
}
