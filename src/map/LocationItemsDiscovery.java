package map;

import com.fasterxml.jackson.databind.JsonNode;
import core.AynikItemsRepo;
import player.item.Item;

import java.util.ArrayList;

/**
 * Created by schaller on 07/02/16.
 */
public class LocationItemsDiscovery extends Location {

    public ArrayList<Item> items;
    public String context;

    public LocationItemsDiscovery(JsonNode rawLocation) {
        super(LocationTypes.itemsDiscovery, rawLocation);
    }

    @Override
    public void init() {
        this.items = new ArrayList<>();
    }

    @Override
    protected void compileRawLocation(JsonNode rawLocation) {
        // items
        AynikItemsRepo itemsRepo = AynikItemsRepo.getInstance();
        for (JsonNode itemJN : rawLocation.findPath("items")) {
            Item item = itemsRepo.find(itemJN.asText());
            this.items.add(item);
        }

        // context
        JsonNode contextJN = rawLocation.findPath("context");
        if (contextJN.isArray()) {
            this.context = "";
            for (JsonNode contextJNItem : contextJN) {
                this.context += contextJNItem.asText() + " ";
            }
            this.context.trim();
        }
        else
        {
            this.context = contextJN.asText();
        }

    }

    public void addItem (Item newItem) {
        items.add(newItem);
    }
}
