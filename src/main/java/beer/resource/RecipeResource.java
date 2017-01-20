package beer.resource;

import beer.entity.Recipe;
import beer.entity.Type;
import beer.entity.Unit;
import com.fasterxml.jackson.annotation.JsonCreator;
import org.springframework.hateoas.core.Relation;

@Relation(collectionRelation = "recipes")
public class RecipeResource extends HALResource {

    private long id;

    private Unit units;

    private String name;

    private Type type;

    private String style;

    @JsonCreator
    public RecipeResource(Recipe recipe) {
        this.id = recipe.getId();
        this.units = recipe.getUnits();
        this.name = recipe.getName();
        this.style = recipe.getStyle();
    }

    public Unit getUnits() {
        return units;
    }

    public void setUnits(Unit units) {
        this.units = units;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public String getStyle() {
        return style;
    }

    public void setStyle(String style) {
        this.style = style;
    }

}
