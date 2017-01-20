package beer.resource;

import beer.entity.Yeast;
import com.fasterxml.jackson.annotation.JsonCreator;
import org.springframework.hateoas.ResourceSupport;
import org.springframework.hateoas.core.Relation;


@Relation(value = "yeast", collectionRelation = "yeasts")
public class YeastResource extends ResourceSupport {

    private long id;

    private String name;

    private String laboratory;

    private String productId;

    private String attenuation;

    private float minTemp;

    private float maxTemp;

    @JsonCreator
    public YeastResource(Yeast yeast) {
        this.id = yeast.getId();
        this.name = yeast.getName();
        this.laboratory = yeast.getLaboratory();
        this.productId = yeast.getProductId();
        this.attenuation = yeast.getAttenuation();
        this.minTemp = yeast.getMinTemp();
        this.maxTemp = yeast.getMaxTemp();
    }

    public String getName() {
        return name;
    }

    public String getLaboratory() {
        return laboratory;
    }

    public String getProductId() {
        return productId;
    }

    public String getAttenuation() {
        return attenuation;
    }

    public float getMinTemp() {
        return minTemp;
    }

    public float getMaxTemp() {
        return maxTemp;
    }
}
