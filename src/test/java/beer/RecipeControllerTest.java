package beer;

import beer.controller.RecipeController;
import beer.entity.Recipe;
import beer.service.RecipeService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

//@SpringBootTest

@RunWith(SpringRunner.class)
@WebMvcTest(RecipeController.class)
public class RecipeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    RecipeService recipeService;


    @Test
    public void getRecipe() throws Exception {
        given(recipeService.findAll()).willReturn(new ArrayList<Recipe>());

        this.mockMvc.perform(get("/recipe").accept(MediaType.parseMediaType(MediaType.APPLICATION_JSON_UTF8_VALUE)))
                .andExpect(status().isOk());
    }

    @Test
    public void getOneRecipe() throws Exception {
        given(recipeService.findOne(1l)).willReturn(new Recipe());

        this.mockMvc.perform(get("/recipe/1").accept(MediaType.parseMediaType(MediaType.APPLICATION_JSON_UTF8_VALUE)))
                .andExpect(status().isOk());
    }

    @Test
    public void postRecipe() throws Exception {
        given(recipeService.save(Mockito.any(Recipe.class))).willReturn(new Recipe());

        this.mockMvc.perform(post("/recipe").content("{}").contentType(MediaType.APPLICATION_JSON_UTF8_VALUE).accept(MediaType.parseMediaType(MediaType.APPLICATION_JSON_UTF8_VALUE)))
                .andExpect(status().isOk());
    }

    @Test
    public void putRecipe() throws Exception {
        given(recipeService.save(Mockito.any(Recipe.class))).willReturn(new Recipe());

        this.mockMvc.perform(put("/recipe/1").content("{}").contentType(MediaType.APPLICATION_JSON_UTF8_VALUE).accept(MediaType.parseMediaType(MediaType.APPLICATION_JSON_UTF8_VALUE)))
                .andExpect(status().isOk());
    }

}
