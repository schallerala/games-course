package player.action;

import player.item.Item;

/**
 * Created by schaller on 17/02/16.
 */
public class ActionUse extends Action {

    private Item item;

    public ActionUse(Item item) {
        super("Use");

        this.item = item;
    }

}
