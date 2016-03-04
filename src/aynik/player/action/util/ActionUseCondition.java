package aynik.player.action.util;

import aynik.player.item.Item;

/**
 * Created by schaller on 03/03/16.
 */
public class ActionUseCondition {

    public Item item;
    public String context;

    public ActionUseCondition(Item item, String context) {
        this.item = item;
        this.context = context;
    }
}
