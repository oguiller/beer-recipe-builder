package beer.controller;

import beer.dto.Error;
import beer.entity.Fermentable;
import beer.entity.Recipe;
import beer.resource.FermentableResource;
import beer.resource.HopResource;
import beer.resource.RecipeResource;
import beer.resource.YeastResource;
import beer.service.RecipeService;
import beer.util.ControllerUtils;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.Resources;
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
import java.util.stream.Collectors;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

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
    public
    @ResponseBody
    HttpEntity<Object> findOne(@PathVariable long id) {
        Map result = new HashMap();
        Recipe recipe = recipeService.findOne(id);
        result.put("recipe", recipe);

        RecipeResource recipeResource = new RecipeResource(recipe);
        recipeResource.add(linkTo(methodOn(RecipeController.class).findOne(recipe.getId())).withSelfRel());

        if (!CollectionUtils.isEmpty(recipe.getHops())) {
            Link link = new Link("/api/recipe/" + recipe.getId() + "/hops/");
            List list = recipe.getHops().stream().map(hop -> {
                Link link1 = linkTo(methodOn(HopController.class).findOne(hop.getId())).withSelfRel();
                HopResource hopResource = new HopResource(hop);
                hopResource.add(link1);
                return hopResource;
            }).collect(Collectors.toList());

            recipeResource.embedResource("hops", new Resources<HopResource>(list, link));
        }

        if (!CollectionUtils.isEmpty(recipe.getFermentables())) {
            Link link = new Link("/api/recipe/" + recipe.getId() + "/fermentables/");
            List list = recipe.getFermentables().stream().map((Fermentable fermentable) -> {
                Link link1 = linkTo(methodOn(FermentableController.class).findOne(fermentable.getId())).withSelfRel();
                FermentableResource fermentableResource = new FermentableResource(fermentable);
                fermentableResource.add(link1);
                return fermentableResource;
            }).collect(Collectors.toList());
            recipeResource.embedResource("fermentables", new Resources<FermentableResource>(list, link));
        }

        if (!CollectionUtils.isEmpty(recipe.getYeasts())) {
            Link link = new Link("/api/recipe/" + recipe.getId() + "/yeasts/");
            List list = recipe.getYeasts().stream().map(yeast -> {
                Link link1 = linkTo(methodOn(YeastController.class).findOne(yeast.getId())).withSelfRel();
                YeastResource yeastResource = new YeastResource(yeast);
                yeastResource.add(link1);
                return yeastResource;
            }).collect(Collectors.toList());
            recipeResource.embedResource("yeasts", new Resources<YeastResource>(list, link));
        }

        return new ResponseEntity<Object>(recipeResource, HttpStatus.OK);
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
