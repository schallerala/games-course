package map;

import com.fasterxml.jackson.databind.JsonNode;
import compiler.CompilerHelper;
import core.AynikItemsRepo;
import player.action.ActionContinue;
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
        this.actions.add(new ActionContinue());
    }

    @Override
    public void init() {
        this.items = new ArrayList<>();
    }

    @Override
    protected void compileRawLocation(JsonNode rawLocation) {
        // items
        AynikItemsRepo itemsRepo = AynikItemsRepo.getInstance();
        for (JsonNode itemJN : rawLocation.get("items")) {
            Item item = itemsRepo.find(itemJN.asText());
            this.items.add(item);
        }

        // context
        CompilerHelper compilerHelper = CompilerHelper.getInstance();

        if (rawLocation.has("context")) {
            this.context = compilerHelper.getString(rawLocation.get("context"));
        }
    }

    public void addItem (Item newItem) {
        items.add(newItem);
    }
}
