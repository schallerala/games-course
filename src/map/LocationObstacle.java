package map;

import com.fasterxml.jackson.databind.JsonNode;

import java.util.ArrayList;

/**
 * Created by schaller on 07/02/16.
 */
public class LocationObstacle extends Location {

    private ArrayList<Position> linkedLocation;
    public String obstacle;

    public LocationObstacle(JsonNode rawLocation) {
        super(LocationTypes.obstacle, rawLocation);
    }

    public LocationObstacle(String obstacle) {
        super(LocationTypes.obstacle, null);
        this.obstacle = obstacle;
    }

    @Override
    public void init() {
        this.linkedLocation = new ArrayList<>();
    }

    @Override
    protected void compileRawLocation(JsonNode rawLocation) {
        this.obstacle = rawLocation.get("obstacle").asText();

        if ( ! rawLocation.has("linkedLocation")) return;

        for (JsonNode position : rawLocation.get("linkedLocation")) {
            try {
                this.linkedLocation.add(new Position(position.asText()));
            } catch (Exception e) {
                System.out.println(e.getMessage());
                e.printStackTrace();
            }
        }
    }
}
