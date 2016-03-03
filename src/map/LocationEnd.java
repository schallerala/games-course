package map;

import com.fasterxml.jackson.databind.JsonNode;

/**
 * Created by schaller on 04/03/16.
 */
public class LocationEnd extends Location {

    public LocationEnd() {
        super(LocationTypes.end, null);
    }

    @Override
    protected void compileRawLocation(JsonNode rawLocation) {
        // nothing to do
    }
}
