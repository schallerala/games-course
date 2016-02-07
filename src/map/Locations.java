package map;

import java.util.HashMap;

/**
 * Created by schaller on 07/02/16.
 */
public class Locations {
    private HashMap<Position, Location> locations;

    private static Locations ourInstance = new Locations();

    public static Locations getInstance() {
        return ourInstance;
    }

    private Locations() {
        locations = new HashMap<>();
    }

    public Location addLocation (Location newLocation) {
        return locations.put(newLocation.position, newLocation);
    }
}
