package map;

import core.parts.Item;

import java.util.ArrayList;

/**
 * Created by schaller on 07/02/16.
 */
public class LocationItemsDiscovery extends Location {

    public ArrayList<Item> items;

    public LocationItemsDiscovery(Position position) {
        super(position, LocationTypes.itemsDiscovery);

        this.items = new ArrayList<>();
    }

    public void addItem (Item newItem) {
        items.add(newItem);
    }
}
