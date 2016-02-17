package map;

import com.fasterxml.jackson.databind.JsonNode;

/**
 * Created by schaller on 07/02/16.
 */
public class LocationDeath extends Location {

    public String story;

    public LocationDeath(JsonNode rawLocation) {
        super(LocationTypes.death, rawLocation);
    }

    @Override
    protected void compileRawLocation(JsonNode rawLocation) {
        JsonNode storyJN = rawLocation.findPath("story");
        if (storyJN.isArray()) {
            this.story = "";
            for (JsonNode storyJNItem : storyJN) {
                this.story += storyJNItem.asText() + " ";
            }
            this.story.trim();
        }
        else
        {
            this.story = storyJN.asText();
        }
    }
}
