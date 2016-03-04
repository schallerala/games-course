package aynik.player.item;

/**
 * Created by schaller on 08/02/16.
 */
public abstract class Item {

    public String name;

    // if an object is usable in the action list
    public boolean usable;

    public Item(String name, boolean usable) {
        this.name = name;
        this.usable = usable;
    }
}
