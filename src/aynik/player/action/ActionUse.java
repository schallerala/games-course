package aynik.player.action;

import aynik.player.item.Item;

/**
 * Created by schaller on 17/02/16.
 */
public class ActionUse extends Action {

    public Item item;

    public ActionUse(Item item) {
        super("Use " + item.name);

        this.item = item;
    }

}
