package map;

import com.fasterxml.jackson.databind.JsonNode;
import compiler.CompilerHelper;
import player.action.ActionContinue;

import java.util.ArrayList;

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
        CompilerHelper compilerHelper = CompilerHelper.getInstance();

        if (rawLocation.has("story")) {
            this.story = compilerHelper.getString(rawLocation.get("story"));
        }
    }
}
