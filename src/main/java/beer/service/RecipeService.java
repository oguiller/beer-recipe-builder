package beer.service;

import beer.entity.Recipe;
import beer.repository.RecipeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RecipeService {

    @Autowired
    RecipeRepository recipeRepository;

    public Recipe save(Recipe recipe1) {
        return recipeRepository.save(recipe1);
    }

    public void delete(long id) {
        recipeRepository.delete(id);
    }

    public Recipe findOne(long id) {
        return recipeRepository.findOne(id);
    }

    public Iterable findAll() {
        return recipeRepository.findAll();
    }
}
