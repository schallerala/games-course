package map;

/**
 * Created by schaller on 07/02/16.
 */
public class LocationDeath extends Location {

    public String story;

    public LocationDeath(Position position, String story) {
        super(position, LocationTypes.death);
        this.story = story;
    }
}
