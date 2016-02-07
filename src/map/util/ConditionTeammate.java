package map.util;

/**
 * Created by schaller on 08/02/16.
 */
public class ConditionTeammate extends Condition {

    public ConditionTeammate() {
        super(ConditionTypes.teammate);
    }

    @Override
    public boolean pass() {
        return false; // TODO
    }
}
