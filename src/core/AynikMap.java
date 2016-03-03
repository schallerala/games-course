package core;

import javafx.geometry.Pos;
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

    public HashMap<Position, Location> getRow (int y) {
        HashMap<Position, Location> returnList = new HashMap<>();

        for (Position position : locations.keySet()) {
            if (position.y == y) returnList.put(position, this.locations.get(position));
        }

        return returnList;
    }

    public HashMap<Position, Location> getCol (char x) {
        HashMap<Position, Location> returnList = new HashMap<>();

        for (Position position : locations.keySet()) {
            if (position.x == x) returnList.put(position, this.locations.get(position));
        }

        return returnList;
    }
}
