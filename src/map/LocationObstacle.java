package map;

import java.util.ArrayList;

/**
 * Created by schaller on 07/02/16.
 */
public class LocationObstacle extends Location {

    private ArrayList<Location> linkedMap;
    public String obstacle;

    public LocationObstacle(Position position, String obstacle) {
        super(position, LocationTypes.obstacle);
        this.obstacle = obstacle;

        this.linkedMap = new ArrayList<>();
    }

    public void addLinkedMap (Location linkedLocation) {
        this.linkedMap.add(linkedLocation);
    }
}
