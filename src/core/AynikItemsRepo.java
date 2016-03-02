package core;

import player.item.Item;

import java.util.ArrayList;

/**
 * Created by schaller on 08/02/16.
 */
public class AynikItemsRepo {
    private static AynikItemsRepo ourInstance = new AynikItemsRepo();

    private ArrayList<Item> items;

    public static AynikItemsRepo getInstance() {
        return ourInstance;
    }

    private AynikItemsRepo() {
        items = new ArrayList<>();
    }

    public Item find(String itemStr) {
        for (Item item : items) {
            if (item.getName().equals(itemStr)) return item;
        }

        return null;
    }

    public void addItem(Item item) {
        this.items.add(item);
    }
}
