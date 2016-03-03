package player.action;

import player.item.Item;

/**
 * Created by schaller on 17/02/16.
 */
public class ActionUse extends Action {

    public Item item;
    public boolean any;

    public ActionUse(Item item) {
        super("Use");

        this.item = item;
        this.any = item == null;
    }

    public void setAny(boolean any) {
        this.any = any;
    }
}
