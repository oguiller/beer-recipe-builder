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
    public
    @ResponseBody
    HttpEntity<Object> getAllRecipes() {
        Iterable recipes = recipeService.findAll();
        List<RecipeResource> resources = new ArrayList<>();
        recipes.forEach(recipe -> {
            Link recipesLink = linkTo(methodOn(RecipeController.class).getAllRecipes()).withRel("allRecipes");
            RecipeResource recipeResource = getRecipeResource((Recipe) recipe);
            recipeResource.add(recipesLink);
            resources.add(recipeResource);
        });

        return new ResponseEntity<Object>(resources, HttpStatus.OK);
    }

    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public
    @ResponseBody
    HttpEntity<Object> get(@PathVariable long id) {
        Map result = new HashMap();
        Recipe recipe = recipeService.findOne(id);
        result.put("recipe", recipe);

        RecipeResource recipeResource = getRecipeResource(recipe);

        return new ResponseEntity<Object>(recipeResource, HttpStatus.OK);
    }

    private RecipeResource getRecipeResource(Recipe recipe) {
        RecipeResource recipeResource = new RecipeResource(recipe);
        recipeResource.add(linkTo(methodOn(RecipeController.class).get(recipe.getId())).withSelfRel());

        if (!CollectionUtils.isEmpty(recipe.getHops())) {
            Link link = linkTo(methodOn(RecipeController.class).getHops(recipe.getId())).withSelfRel();
            List list = recipe.getHops().stream().map(hop -> {
                Link self = linkTo(methodOn(HopController.class).get(hop.getId())).withSelfRel();
                HopResource hopResource = new HopResource(hop);
                hopResource.add(self);
                return hopResource;
            }).collect(Collectors.toList());

            recipeResource.embedResource("hops", new Resources<HopResource>(list, link));
        }

        if (!CollectionUtils.isEmpty(recipe.getFermentables())) {
            Link self = linkTo(methodOn(RecipeController.class).getFermentables(recipe.getId())).withSelfRel();
            List list = recipe.getFermentables().stream().map((Fermentable fermentable) -> {
                Link link1 = linkTo(methodOn(FermentableController.class).get(fermentable.getId())).withSelfRel();
                FermentableResource fermentableResource = new FermentableResource(fermentable);
                fermentableResource.add(link1);
                return fermentableResource;
            }).collect(Collectors.toList());
            recipeResource.embedResource("fermentables", new Resources<FermentableResource>(list, self));
        }

        if (!CollectionUtils.isEmpty(recipe.getYeasts())) {
            Link self = linkTo(methodOn(RecipeController.class).getYeasts(recipe.getId())).withSelfRel();
            List list = recipe.getYeasts().stream().map(yeast -> {
                Link link1 = linkTo(methodOn(YeastController.class).get(yeast.getId())).withSelfRel();
                YeastResource yeastResource = new YeastResource(yeast);
                yeastResource.add(link1);
                return yeastResource;
            }).collect(Collectors.toList());
            recipeResource.embedResource("yeasts", new Resources<YeastResource>(list, self));
        }
        return recipeResource;
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_UTF8_VALUE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public
    @ResponseBody
    HttpEntity<Object> save(@RequestBody Recipe recipe) {
        try {
            Recipe savedRecipe = recipeService.save(recipe);
            RecipeResource recipeResource = getRecipeResource(savedRecipe);
            return new ResponseEntity<Object>(recipeResource, HttpStatus.OK);
        } catch (ConstraintViolationException cve) {
            return new ResponseEntity<Object>(ControllerUtils.buildConstraintError(cve), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<Object>(ControllerUtils.buildGenericError(e), HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_UTF8_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public
    @ResponseBody
    HttpEntity<Object> update(@PathVariable long id, @RequestBody Recipe recipe) {
        Recipe existingRecipe = recipeService.findOne(id);
        Map result = new HashMap();

        if (existingRecipe == null) {
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
            BeanUtils.copyProperties(recipe, existingRecipe);
            Recipe updatedRecipe = recipeService.save(existingRecipe);
            RecipeResource recipeResource = getRecipeResource(updatedRecipe);
            return new ResponseEntity<Object>(recipeResource, HttpStatus.OK);
        } catch (ConstraintViolationException cve) {
            return new ResponseEntity<Object>(ControllerUtils.buildConstraintError(cve), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<Object>(ControllerUtils.buildGenericError(e), HttpStatus.BAD_REQUEST);
        }

    }

    @GetMapping(value = "/{id}/hops", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public
    @ResponseBody
    HttpEntity<Object> getHops(@PathVariable long id) {
        Recipe recipe = recipeService.findOne(id);
        List<HopResource> hopResources = new ArrayList<>();

        if (!CollectionUtils.isEmpty(recipe.getHops())) {
            hopResources = recipe.getHops().stream().map(hop -> {
                Link link1 = linkTo(methodOn(HopController.class).get(hop.getId())).withSelfRel();
                HopResource hopResource = new HopResource(hop);
                hopResource.add(link1);
                return hopResource;
            }).collect(Collectors.toList());
        }

        return new ResponseEntity<>(hopResources, HttpStatus.OK);
    }

    @GetMapping(value = "/{id}/fermentables", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public
    @ResponseBody
    HttpEntity<Object> getFermentables(@PathVariable long id) {
        Recipe recipe = recipeService.findOne(id);
        List<FermentableResource> fermentableResources = new ArrayList<>();

        if (!CollectionUtils.isEmpty(recipe.getFermentables())) {
            fermentableResources = recipe.getFermentables().stream().map(fermentable -> {
                Link link1 = linkTo(methodOn(FermentableController.class).get(fermentable.getId())).withSelfRel();
                FermentableResource fermentableResource = new FermentableResource(fermentable);
                fermentableResource.add(link1);
                return fermentableResource;
            }).collect(Collectors.toList());
        }

        return new ResponseEntity<>(fermentableResources, HttpStatus.OK);
    }

    @GetMapping(value = "/{id}/yeasts", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public
    @ResponseBody
    HttpEntity<Object> getYeasts(@PathVariable long id) {
        Recipe recipe = recipeService.findOne(id);
        List<YeastResource> yeastResources = new ArrayList<>();

        if (!CollectionUtils.isEmpty(recipe.getYeasts())) {
            yeastResources = recipe.getYeasts().stream().map(yeast -> {
                Link link = linkTo(methodOn(YeastController.class).get(yeast.getId())).withSelfRel();
                YeastResource yeastResource = new YeastResource(yeast);
                yeastResource.add(link);
                return yeastResource;
            }).collect(Collectors.toList());
        }

        return new ResponseEntity<>(yeastResources, HttpStatus.OK);
    }

    @DeleteMapping(value = "/{id}")
    public
    @ResponseBody
    HttpEntity<Object> delete(@PathVariable long id) {
        Map result = new HashMap();
        recipeService.delete(id);
        result.put("code", HttpStatus.OK.value());
        result.put("message", HttpStatus.OK.getReasonPhrase());
        return new ResponseEntity<Object>(result, HttpStatus.OK);
    }

}
