package beer.entity;

public enum Type {

    ALL_GRAIN("All Grain"), EXTRACT("Extract"), PARTIAL_MASH("Partial Mash");

    String description;

    Type(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
