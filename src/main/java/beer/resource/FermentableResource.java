package beer.resource;

import beer.entity.Fermentable;
import org.springframework.hateoas.ResourceSupport;
import org.springframework.hateoas.core.Relation;

@Relation(value = "fermentable", collectionRelation = "fermentables")
public class FermentableResource extends ResourceSupport {
    private long id;

    private String name;

    private String type;

    private String color;

    public FermentableResource(Fermentable fermentable) {
        this.id = fermentable.getId();
        this.name = fermentable.getName();
        this.type = fermentable.getType();
        this.color = fermentable.getColor();
    }


    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }

    public String getColor() {
        return color;
    }
}
