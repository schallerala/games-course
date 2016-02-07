package map.util;

/**
 * Created by schaller on 08/02/16.
 */
public class ConditionAction extends Condition {

    public String action;
    public String context;

    public ConditionAction(String action, String context) {
        super(ConditionTypes.action);
        this.action = action;
        this.context = context;
    }

    @Override
    public boolean pass() {
        return false; // TODO
    }
}
