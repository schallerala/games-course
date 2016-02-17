package compiler;


import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import core.AynikItemsRepo;
import core.AynikMap;
import player.item.Item;
import map.*;

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

        jsonNode = mapper.readTree(gameData).findPath("AYNIK");

        if (jsonNode.isMissingNode()) throw new CompilerMissingNodeException("No AYNIK game data found");
    }

    public void prepareGame() throws CompilerMissingNodeException {
        this.loadItems();
        this.loadLocations();
    }

    private void loadLocations() throws CompilerMissingNodeException {
        JsonNode locationNode = this.jsonNode.findPath("locations");
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
        String locationPositionStr = field.getKey();
        Position locationPosition = null;
        try {
            locationPosition = new Position(locationPositionStr);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

        String locationType = locationJN.findPath("type").asText();

        Location newLocation = null;

        switch (locationType) {
            case "choice":
                newLocation = new LocationChoice(locationJN);
                break;
            case "condition":
                newLocation = new LocationCondition(locationJN);
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
            default:
                System.out.println("location type mismatch");
        }

        return newLocation;
    }

    private void loadItems() {
        JsonNode items = this.jsonNode.findPath("items");

        AynikItemsRepo itemsRepo = AynikItemsRepo.getInstance();
        for (JsonNode item : items) {
            itemsRepo.addItem( new Item(item.asText()) );
        }
    }
}
