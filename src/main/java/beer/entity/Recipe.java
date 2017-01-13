package beer.entity;

import javax.persistence.*;
import java.util.Set;

@Entity
public class Recipe {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @Enumerated(EnumType.STRING)
    private Unit units;

    private String name;

    @Enumerated(EnumType.STRING)
    private Type type;

    private String style;

    @OneToMany(cascade=CascadeType.ALL, orphanRemoval = false)
    private Set<Fermentable> fermentables;

    @OneToMany(cascade=CascadeType.ALL, orphanRemoval = false)
    private Set<Hop> hops;

    @OneToMany(cascade=CascadeType.ALL, orphanRemoval = false)
    private Set<Yeast> yeasts;

    public long getId() {
        return id;
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

    public Set<Fermentable> getFermentables() {
        return fermentables;
    }

    public void setFermentables(Set<Fermentable> fermentables) {
        this.fermentables = fermentables;
    }

    public Set<Hop> getHops() {
        return hops;
    }

    public void setHops(Set<Hop> hops) {
        this.hops = hops;
    }

    public Set<Yeast> getYeasts() {
        return yeasts;
    }

    public void setYeasts(Set<Yeast> yeasts) {
        this.yeasts = yeasts;
    }

    public Unit getUnits() {
        return units;
    }

    public void setUnits(Unit units) {
        this.units = units;
    }
}
