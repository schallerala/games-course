package map;

import map.util.Condition;

import java.util.ArrayList;

/**
 * Created by schaller on 07/02/16.
 */
public class LocationCondition extends Location {

    // if only one condition needs to be completed
    public boolean any;
    public ArrayList<Condition> conditions;

    public LocationCondition(Position position, boolean any) {
        super(position, LocationTypes.condition);
        this.any = any;

        this.conditions = new ArrayList<>();
    }

    public Condition addCondition(Condition newCondition) {
        return this.conditions.add(newCondition) ? newCondition : null;
    }

    @Override
    public void onArrive() {
        boolean dontPass = false;
        boolean passedOne = false;
        for (Condition condition : this.conditions) {
            if (condition.isPreCheck()) {
                dontPass = ! condition.pass();
                passedOne = passedOne || condition.pass();
            }
        }

        if (this.any && ! passedOne || dontPass) return; // TODO player die
    }
}
