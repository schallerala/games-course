package map;

import com.fasterxml.jackson.databind.JsonNode;

/**
 * Created by schaller on 07/02/16.
 */
public abstract class Location {

    public LocationTypes type;

    public Location(LocationTypes type, JsonNode rawLocation) {
        this.type = type;

        this.init();
        this.compileRawLocation(rawLocation);
    }

    public void init () {}

    protected abstract void compileRawLocation(JsonNode rawLocation);

    public void onArrive () {}

}
