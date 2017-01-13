package beer.entity;

import javax.persistence.Embeddable;

@Embeddable
public class BatchBoilDetails {

    private Float batchSize;

    private Integer boilTime;


}
