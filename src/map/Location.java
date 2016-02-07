package map;

/**
 * Created by schaller on 07/02/16.
 */
public abstract class Location {

    public Position position;
    public LocationTypes type;

    public Location(Position position, LocationTypes type) {
        this.position = position;
        this.type = type;
    }

    public void onArrive () {}
}
