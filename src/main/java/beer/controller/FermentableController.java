package beer.controller;

import beer.entity.Fermentable;
import beer.resource.FermentableResource;
import beer.service.FermentableService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

@RestController
@RequestMapping("/api/fermentable")
public class FermentableController {

    @Autowired
    FermentableService fermentableService;

    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public
    @ResponseBody
    HttpEntity<Object> get(@PathVariable long id) {
        Fermentable fermentable = fermentableService.findOne(id);
        FermentableResource fermentableResource = new FermentableResource(fermentable);
        fermentableResource.add(linkTo(methodOn(FermentableController.class).get(((Fermentable) fermentable).getId())).withSelfRel());
        return new ResponseEntity<Object>(fermentableResource, HttpStatus.OK);
    }
}
