package beer.entity;

public enum Unit {

    US("US - (Gallons, lb, oz)"), METRIC("Metric -(Liters, kg, gr)");

    String description;

    Unit(String description){
        this.description = description;
    }
}
