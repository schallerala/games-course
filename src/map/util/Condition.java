package map.util;

/**
 * Created by schaller on 07/02/16.
 */
public abstract class Condition {

    public ConditionTypes type;
    public String context;

    public Condition(ConditionTypes type) {
        this.type = type;
    }

    public void setContext(String context) {
        this.context = context;
    }

    public abstract boolean pass ();

    public boolean isPreCheck () {
        return this.type == ConditionTypes.item || this.type == ConditionTypes.teammate;
    }
}
