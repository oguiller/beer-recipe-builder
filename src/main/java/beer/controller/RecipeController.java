package beer.controller;

import beer.dto.Error;
import beer.entity.Recipe;
import beer.repository.RecipeRepository;
import beer.service.RecipeService;
import beer.util.ControllerUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.util.*;

@RequestMapping("/api/recipe")
@RestController
public class RecipeController {

    @Autowired
    private RecipeService recipeService;

    @GetMapping(produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public @ResponseBody Map findAll(){
        Map result = new HashMap();
        Iterable recipes = recipeService.findAll();
        result.put("recipes", recipes);

        return result;
    }

    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public @ResponseBody Map findOne(@PathVariable long id){
        Map result = new HashMap();
        Recipe recipe = recipeService.findOne(id);
        result.put("recipe", recipe);

        return result;
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_UTF8_VALUE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public
    @ResponseBody
    Map save(@RequestBody Recipe recipe) {
        Map result = new HashMap();
        try {
            Recipe savedRecipe = recipeService.save(recipe);
            result.put("recipe", savedRecipe);
        } catch (ConstraintViolationException cve) {
            ControllerUtils.buildConstraintError(result, cve);
        } catch (Exception e) {
            ControllerUtils.buildGenericError(result, e);
        } finally {
            return result;
        }
    }

    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_UTF8_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public Map update(@PathVariable long id, @RequestBody Recipe recipe) {
        Recipe recipe1 = recipeService.findOne(id);
        Map result = new HashMap();

        if (recipe1 == null) {
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
            BeanUtils.copyProperties(recipe, recipe1);
            Recipe updatedRecipe = recipeService.save(recipe1);
            result.put("recipe", updatedRecipe);
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
        recipeService.delete(id);
        result.put("code", HttpStatus.OK.value());
        result.put("message", HttpStatus.OK.getReasonPhrase());
        return result;
    }

}
