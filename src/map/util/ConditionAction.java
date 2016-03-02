package map.util;

import player.action.Action;

/**
 * Created by schaller on 08/02/16.
 */
public class ConditionAction extends Condition {

    public Action action;
    public String context;

    public ConditionAction(Action action, String context) {
        super(ConditionTypes.action);
        this.action = action;
        this.context = context;
    }

    public ConditionAction(Action action) {
        super(ConditionTypes.action);
        this.action = action;
    }

    @Override
    public void setContext(String context) {
        this.context = context;
    }

    @Override
    public boolean pass() {
        return false; // TODO
    }
}
