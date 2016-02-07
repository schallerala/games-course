package map.util;

/**
 * Created by schaller on 08/02/16.
 */
public class ConditionItem extends Condition {

    public String item;
    // TODO add item

    public ConditionItem(String item) {
        super(ConditionTypes.item);
        this.item = item;
        // TODO translate string item to object with items repo
    }

    @Override
    public boolean pass() {
        return false; // TODO
    }
}
