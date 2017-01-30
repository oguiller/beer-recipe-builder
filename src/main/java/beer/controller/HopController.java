package beer.controller;

import beer.dto.Error;
import beer.entity.Hop;
import beer.resource.HopResource;
import beer.service.HopService;
import beer.util.ControllerUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Link;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.ConstraintViolationException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

@RequestMapping("/api/hop")
@RestController
public class HopController {

    @Autowired
    private HopService hopService;

    @GetMapping(produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public
    @ResponseBody
    HttpEntity<Object> getAllHops() {
        Iterable hops = hopService.findAll();
        List<HopResource> hopResources = new ArrayList<>();

        hops.forEach(hop -> {
            Link allHopsLink = linkTo(methodOn(HopController.class).getAllHops()).withRel("allHops");
            HopResource hopResource = new HopResource((Hop) hop);
            hopResource.add(linkTo(methodOn(HopController.class).get(((Hop) hop).getId())).withSelfRel());
            hopResource.add(allHopsLink);
            hopResources.add(hopResource);
        });

        return new ResponseEntity<Object>(hopResources, HttpStatus.OK);
    }

    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public
    @ResponseBody
    HttpEntity<Object> get(@PathVariable long id) {
        Hop hop = hopService.findOne(id);
        HopResource hopResource = new HopResource(hop);
        hopResource.add(linkTo(methodOn(HopController.class).get(((Hop) hop).getId())).withSelfRel());
        return new ResponseEntity<Object>(hopResource, HttpStatus.OK);
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_UTF8_VALUE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public
    @ResponseBody
    HttpEntity<Object> save(@RequestBody Hop hop) {
        Map result = new HashMap();
        try {
            Hop savedHop = hopService.save(hop);
            HopResource hopResource = new HopResource(savedHop);
            hopResource.add(linkTo(methodOn(HopController.class).get(((Hop) savedHop).getId())).withSelfRel());
            return new ResponseEntity<Object>(hopResource, HttpStatus.OK);
        } catch (ConstraintViolationException cve) {
            result = ControllerUtils.buildConstraintError(cve);
            return new ResponseEntity<Object>(result, HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            result = ControllerUtils.buildGenericError(e);
            return new ResponseEntity<Object>(result, HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_UTF8_VALUE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public HttpEntity<Object> update(@PathVariable long id, @RequestBody Hop hop) {
        Hop hop1 = hopService.findOne(id);
        Map result = new HashMap();

        if (hop1 == null) {
            beer.dto.Error error = new beer.dto.Error();
            Map errors = new HashMap();
            errors.put("domain", "global");

            List<String> messages = new ArrayList<String>();
            messages.add("Resource not found");

            errors.put("message", messages);
            error.setErrors(errors);
            error.setCode(HttpStatus.NOT_FOUND.value());
            error.setMessage(HttpStatus.NOT_FOUND.getReasonPhrase());
            result.put(Error.ERROR, error);
            return new ResponseEntity<Object>(result, HttpStatus.BAD_REQUEST);
        }

        try {
            BeanUtils.copyProperties(hop, hop1);
            Hop updatedHop = hopService.save(hop1);
            HopResource hopResource = new HopResource(updatedHop);
            hopResource.add(linkTo(methodOn(HopController.class).get(((Hop) updatedHop).getId())).withSelfRel());
            return new ResponseEntity<Object>(hopResource, HttpStatus.BAD_REQUEST);
        } catch (ConstraintViolationException cve) {
            result = ControllerUtils.buildConstraintError(cve);
            return new ResponseEntity<Object>(result, HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            result = ControllerUtils.buildGenericError(e);
            return new ResponseEntity<Object>(result, HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping(value = "/{id}")
    public HttpEntity<Object> delete(@PathVariable long id) {
        Map result = new HashMap();
        hopService.delete(id);
        result.put("code", HttpStatus.OK.value());
        result.put("message", HttpStatus.OK.getReasonPhrase());
        return new ResponseEntity<Object>(result, HttpStatus.OK);
    }
}
