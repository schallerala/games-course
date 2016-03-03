package compiler;


import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import core.AynikItemsRepo;
import core.AynikMap;
import core.AynikStory;
import player.item.Grenade;
import player.item.Item;
import map.*;
import player.item.Shield;

import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by schaller on 05/02/16.
 */
public class AynikCompiler {

    private JsonNode jsonNode;
    private File gameData;

    public AynikCompiler(File gameData) throws CompilerMissingNodeException, IOException {
        this.init(gameData);
    }

    private void init (File gameData) throws CompilerMissingNodeException, IOException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.enable(JsonParser.Feature.ALLOW_COMMENTS);

        jsonNode = mapper.readTree(gameData).get("AYNIK");

        if (jsonNode.isMissingNode()) throw new CompilerMissingNodeException("No AYNIK game data found");
    }

    public void prepareGame() throws CompilerMissingNodeException {
        this.loadItems();
        this.loadLocations();
        this.loadStory();
    }

    private void loadLocations() throws CompilerMissingNodeException {
        JsonNode locationNode = this.jsonNode.get("locations");
        if (locationNode.isMissingNode()) throw new CompilerMissingNodeException("No locations found in the game data");

        AynikMap map = AynikMap.getInstance();

        Iterator<Map.Entry<String, JsonNode>> fields = locationNode.fields();
        while (fields.hasNext()) {
            Map.Entry<String, JsonNode> field = fields.next();
            Location newLocation = this.loadLocation(field);
            Position newPosition = null;
            try {
                newPosition = this.loadLocationPosition(field.getKey());
            } catch (Exception e) {
                continue;
            }
            if (newLocation == null) {
                System.out.println("Can't add location on " + newPosition);
                continue;
            }
            map.addLocation(newPosition, newLocation);
        }
    }

    private Position loadLocationPosition(String fieldKey) throws Exception {
        return new Position(fieldKey);
    }

    private Location loadLocation(Map.Entry<String, JsonNode> field) {
        JsonNode locationJN = field.getValue();

        Location newLocation = null;

        if ( ! locationJN.has("type")) {
            return new LocationObstacle("Unreachable");
        }

        switch (locationJN.get("type").asText()) {
            case "normal":
                newLocation = new Location(locationJN);
                break;
            case "death":
                newLocation = new LocationDeath(locationJN);
                break;
            case "itemsDiscovery":
                newLocation = new LocationItemsDiscovery(locationJN);
                break;
            case "obstacle":
                newLocation = new LocationObstacle(locationJN);
                break;
            case "end":
                newLocation = new LocationEnd();
                break;
        }

        return newLocation;
    }

    private void loadItems() {
        JsonNode items = this.jsonNode.get("items");

        AynikItemsRepo itemsRepo = AynikItemsRepo.getInstance();
        for (JsonNode item : items) {
            switch (item.asText()) {
                case "grenade":
                    itemsRepo.addItem( new Grenade() );
                    break;
                case "shield":
                    itemsRepo.addItem( new Shield() );
            }

        }
    }

    private void loadStory() {
        AynikStory story = AynikStory.getInstance();
        JsonNode storyJN = this.jsonNode.get("story");

        for (JsonNode introPart : storyJN.get("intro")) {
            story.addToIntro(introPart.asText());
        }
        for (JsonNode introPart : storyJN.get("end")) {
            story.addToEnd(introPart.asText());
        }
    }
}
