package player.action;

import player.action.util.ActionUseCondition;
import player.item.Item;

import java.util.ArrayList;

/**
 * Created by schaller on 03/03/16.
 */
public class ActionUseConditions extends ActionUse {

    public ArrayList<ActionUseCondition> conditions;

    public ActionUseConditions(Item item) {
        super(item);

        this.conditions = new ArrayList<>();
    }

    public ActionUseConditions(ActionUse actionUse) {
        super(actionUse.item);
        this.setContext(actionUse.context);
        this.setSuccess(actionUse.success);

        this.conditions = new ArrayList<>();
    }

    public void addCondition (ActionUseCondition condition) {
        this.conditions.add(condition);
    }
}
