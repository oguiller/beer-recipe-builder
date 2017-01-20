package beer.resource;

import beer.entity.Hop;
import com.fasterxml.jackson.annotation.JsonCreator;
import org.springframework.hateoas.ResourceSupport;
import org.springframework.hateoas.core.Relation;

@Relation(value = "hop", collectionRelation = "hops")
public class HopResource extends ResourceSupport {

    private long id;

    private String name;

    private String country;

    private String form;

    @JsonCreator
    public HopResource(Hop hop) {
        this.id = hop.getId();
        this.name = hop.getName();
        this.country = hop.getCountry();
        this.form = hop.getForm();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getForm() {
        return form;
    }

    public void setForm(String form) {
        this.form = form;
    }
}
