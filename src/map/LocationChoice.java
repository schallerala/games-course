package map;

import com.fasterxml.jackson.databind.JsonNode;
import map.util.Choice;
import player.action.Action;

import java.util.ArrayList;

/**
 * Created by schaller on 07/02/16.
 */
public class LocationChoice extends Location {

    public ArrayList<Choice> choices;

    public LocationChoice(JsonNode rawLocation) {
        super(LocationTypes.choice, rawLocation);
    }

    @Override
    public void init() {
        this.choices = new ArrayList<>();
    }

    @Override
    protected void compileRawLocation(JsonNode rawLocation) {

    }

    public void addChoice(Choice newChoice) {
        this.choices.add(newChoice);
    }
}
