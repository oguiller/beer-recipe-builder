package beer.controller;

import beer.dto.Error;
import beer.entity.Hop;
import beer.repository.HopRepository;
import beer.util.ControllerUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.validation.ConstraintViolationException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RequestMapping("/hop")
@RestController
public class HopController {

    @Autowired
    private HopRepository hopRepository;

    @GetMapping(produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public @ResponseBody
    Map findAll(){
        Map result = new HashMap();
        Iterable hops = hopRepository.findAll();
        result.put("hops", hops);

        return result;
    }

    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public @ResponseBody Map findOne(@PathVariable long id){
        Map result = new HashMap();
        Hop hop = hopRepository.findOne(id);
        result.put("hop", hop);

        return result;
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_UTF8_VALUE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public
    @ResponseBody
    Map save(@RequestBody Hop hop) {
        Map result = new HashMap();
        try {
            Hop savedHop = hopRepository.save(hop);
            result.put("hop", savedHop);
        } catch (ConstraintViolationException cve) {
            ControllerUtils.buildConstraintError(result, cve);
        } catch (Exception e) {
            ControllerUtils.buildGenericError(result, e);
        } finally {
            return result;
        }
    }

    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_UTF8_VALUE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public Map update(@PathVariable long id, @RequestBody Hop hop) {
        Hop hop1 = hopRepository.findOne(id);
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
            return result;
        }

        try {
            BeanUtils.copyProperties(hop, hop1);
            Hop updatedHop = hopRepository.save(hop1);
            result.put("hop", updatedHop);
            result.put("code", HttpStatus.OK);
            result.put("message", HttpStatus.OK.getReasonPhrase());
        } catch (ConstraintViolationException cve) {
            ControllerUtils.buildConstraintError(result, cve);
        } catch (Exception e) {
            ControllerUtils.buildGenericError(result, e);
        } finally {
            return result;
        }

    }

    @DeleteMapping(value = "/{id}")
    public Map delete(@PathVariable long id) {
        Map result = new HashMap();
        hopRepository.delete(id);
        result.put("code", HttpStatus.OK.value());
        result.put("message", HttpStatus.OK.getReasonPhrase());
        return result;
    }
}
