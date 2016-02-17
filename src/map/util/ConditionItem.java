package map.util;

import core.AynikItemsRepo;
import player.item.Item;

/**
 * Created by schaller on 08/02/16.
 */
public class ConditionItem extends Condition {

    public String itemStr;
    private Item item;

    public ConditionItem(String itemStr) {
        super(ConditionTypes.item);
        this.itemStr = itemStr;
        this.item = AynikItemsRepo.getInstance().find(this.itemStr);
    }

    public ConditionItem(Item item) {
        super(ConditionTypes.item);
        this.item = item;
    }

    @Override
    public boolean pass() {
        return false; // TODO
    }
}
