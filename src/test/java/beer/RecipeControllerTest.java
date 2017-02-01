package beer;

import beer.entity.*;
import beer.repository.RecipeRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.restdocs.JUnitRestDocumentation;
import org.springframework.restdocs.mockmvc.RestDocumentationResultHandler;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.HashSet;
import java.util.Set;

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = Application.class)
@TestPropertySource(locations = "classpath:test-application.properties")
@WebAppConfiguration
public class RecipeControllerTest {

    @Rule
    public final JUnitRestDocumentation restDocumentation = new JUnitRestDocumentation("target/generated-snippets");

    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext context;

    @Autowired
    private RecipeRepository recipeRepository;

    @Autowired
    private ObjectMapper objectMapper;

    private RestDocumentationResultHandler document;

    @Before
    public void setUp() {
        this.document = document("{method-name}", preprocessRequest(prettyPrint()), preprocessResponse(prettyPrint()));
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.context)
                .apply(documentationConfiguration(this.restDocumentation))
                .alwaysDo(this.document)
                .build();
        createSampleRecipe("Indian Pale Ale");
        createSampleRecipe("MilkStout");
    }

    @Test
    public void listRecipes() throws Exception {
        this.mockMvc.perform(
                get("/api/recipe").accept(MediaType.APPLICATION_JSON)
        ).andExpect(status().isOk())
                .andDo(document("{method-name}", responseFields(
                        fieldWithPath("[].type").description("The recipe type (ALL_GRAIN, PARTIAL_MASH or EXTRACT)"),
                        fieldWithPath("[].style").description("The recipe style"),
                        fieldWithPath("[].units").description("The recipe units (METRIC or US)"),
                        fieldWithPath("[].name").description("The recipe name"),
                        fieldWithPath("[]._links").description("The links section"),
                        fieldWithPath("[]._links.self").description("The link to itself"),
                        fieldWithPath("[]._links.allRecipes").description("The link to all the recipes"),
                        fieldWithPath("[]._embedded.hops").description("The hops attached to the recipe"),
                        fieldWithPath("[]._embedded.fermentables").description("The fermentables used in the recipe"),
                        fieldWithPath("[]._embedded.yeasts").description("The yeasts used in the recipe")
                )));
    }

    @Test
    public void updateRecipe() throws Exception {
        Recipe originalRecipe = createSampleRecipe("Sstout");

        Recipe updatedRecipe = new Recipe();
        updatedRecipe.setName("Stout");
        updatedRecipe.setType(Type.ALL_GRAIN);
        updatedRecipe.setStyle("Test");
        updatedRecipe.setUnits(Unit.METRIC);

        this.mockMvc.perform(
                put("/api/recipe/" + originalRecipe.getId()).contentType(MediaType.APPLICATION_JSON).content(
                        this.objectMapper.writeValueAsString(updatedRecipe)
                )
        ).andExpect(status().isOk()).andDo(document("{method-name}", requestFields(
                fieldWithPath("type").description("The recipe type"),
                fieldWithPath("style").description("The recipe style"),
                fieldWithPath("units").description("The recipe units (METRIC or US)"),
                fieldWithPath("name").description("The recipe name"),
                fieldWithPath("id").description("The recipe name"),
                fieldWithPath("fermentables").description("The recipe name"),
                fieldWithPath("hops").description("The recipe name"),
                fieldWithPath("yeasts").description("The recipe name")
        )));
    }

    @Test
    public void createRecipe() throws Exception {
        Recipe recipe = createSampleRecipe("Guinness");

        this.mockMvc.perform(
                post("/api/recipe/").contentType(MediaType.APPLICATION_JSON).content(
                        this.objectMapper.writeValueAsString(recipe)
                )
        ).andExpect(status().isOk()).andDo(document("{method-name}", requestFields(
                fieldWithPath("type").description("The recipe type"),
                fieldWithPath("style").description("The recipe style"),
                fieldWithPath("units").description("The recipe units (METRIC or US)"),
                fieldWithPath("name").description("The recipe name"),
                fieldWithPath("id").description("The recipe name"),
                fieldWithPath("fermentables").description("The recipe name"),
                fieldWithPath("hops").description("The recipe name"),
                fieldWithPath("yeasts").description("The recipe name")
        )));
    }

    @Test
    public void deleteRecipe() throws Exception {

        this.document.snippets(
                responseFields(
                        fieldWithPath("code").description("The recipe type"),
                        fieldWithPath("message").description("The recipe style")
                )
        );

        this.mockMvc.perform(
                delete("/api/recipe/1").accept(MediaType.APPLICATION_JSON)
        ).andExpect(status().isOk());
    }

    @Test
    public void getRecipe() throws Exception {

        this.document.snippets(
                responseFields(
                        fieldWithPath("type").description("The recipe type"),
                        fieldWithPath("style").description("The recipe style"),
                        fieldWithPath("units").description("The recipe units"),
                        fieldWithPath("name").description("The recipe name"),
                        fieldWithPath("_links").description("The links section"),
                        fieldWithPath("_links.self").description("The link to itself"),
                        fieldWithPath("_embedded.hops").description("The hops attached to the recipe"),
                        fieldWithPath("_embedded.fermentables").description("The fermentables used in the recipe"),
                        fieldWithPath("_embedded.yeasts").description("The yeasts used in the recipe")
                )
        );

        this.mockMvc.perform(
                get("/api/recipe/2").accept(MediaType.APPLICATION_JSON)
        ).andExpect(status().isOk()).andDo(document("{method-name}", responseFields(
                fieldWithPath("type").description("The recipe type"),
                fieldWithPath("style").description("The recipe style"),
                fieldWithPath("units").description("The recipe units"),
                fieldWithPath("name").description("The recipe name"),
                fieldWithPath("_links").description("The links section"),
                fieldWithPath("_links.self").description("The link to itself"),
                fieldWithPath("_embedded.hops").description("The hops attached to the recipe"),
                fieldWithPath("_embedded.fermentables").description("The fermentables used in the recipe"),
                fieldWithPath("_embedded.yeasts").description("The yeasts used in the recipe")
        )));
    }

    private Recipe createSampleRecipe(String name) {
        Recipe recipe = new Recipe();
        recipe.setName(name);
        recipe.setType(Type.ALL_GRAIN);
        recipe.setStyle("Test");
        recipe.setUnits(Unit.METRIC);

        Hop hop = createSampleHop("HopTest");
        Set hops = new HashSet();
        hops.add(hop);

        recipe.setHops(hops);

        Fermentable fermentable = createSampleFermentable("FermentableTest");
        Set fermentables = new HashSet();
        fermentables.add(fermentable);

        recipe.setFermentables(fermentables);

        Yeast yeast = createSampleYeast("YeastTest");
        Set yeastSet = new HashSet();
        yeastSet.add(yeast);

        recipe.setYeasts(yeastSet);

        return recipeRepository.save(recipe);
    }

    private Yeast createSampleYeast(String name) {
        Yeast yeast = new Yeast();
        yeast.setName(name);
        return yeast;
    }

    private Hop createSampleHop(String name) {
        Hop hop = new Hop();
        hop.setName(name);
        hop.setCountry("U.S.A");
        return hop;
    }

    private Fermentable createSampleFermentable(String name) {
        Fermentable fermentable = new Fermentable();
        fermentable.setName("Test");
        fermentable.setColor("30L");
        fermentable.setType("Test");
        return fermentable;
    }


}
