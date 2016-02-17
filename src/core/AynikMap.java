package core;

import map.Location;
import map.LocationTypes;
import map.Position;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by schaller on 16/02/16.
 */
public class AynikMap {
    private static AynikMap ourInstance = new AynikMap();

    private HashMap<Position, Location> locations;

    public static AynikMap getInstance() {
        return ourInstance;
    }

    private AynikMap() {
        locations = new HashMap<>();
    }

    public void addLocation (Position position, Location location) {
        locations.put(position, location);
    }

    public ArrayList<Location> getByType (LocationTypes type) {
        ArrayList<Location> filtered = new ArrayList<>();
        for (Location location : this.locations.values()) {
            if (location.type.equals(type)) {
                filtered.add(location);
            }
        }

        return filtered;
    }
}
