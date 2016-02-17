package map;

import com.fasterxml.jackson.databind.JsonNode;
import core.AynikItemsRepo;
import map.util.ConditionAction;
import map.util.ConditionTeammate;
import player.action.Action;
import player.action.ActionHide;
import player.action.ActionJetpack;
import player.action.ActionUse;
import player.item.Item;
import map.util.Condition;
import map.util.ConditionItem;

import java.util.ArrayList;

/**
 * Created by schaller on 07/02/16.
 */
public class LocationCondition extends Location {

    // if only one condition needs to be completed
    public boolean any;
    public ArrayList<Condition> conditions;
    public String context;

    public LocationCondition(JsonNode rawLocation) {
        super(LocationTypes.condition, rawLocation);
    }

    @Override
    public void init() {
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

    @Override
    protected void compileRawLocation(JsonNode rawLocation) {
        AynikItemsRepo itemRepo = AynikItemsRepo.getInstance();

        if (rawLocation.has("context")) this.context = rawLocation.findPath("context").asText();

        Item itemForCondition;
        Condition newCondition;

        for (JsonNode conditionJN : rawLocation.findPath("conditions")) {
            newCondition = null;

            switch (conditionJN.findPath("type").asText()) {

                case "teammate":
                    newCondition = new ConditionTeammate();
                    break;

                case "item":
                    itemForCondition = itemRepo.find(conditionJN.findPath("item").asText());
                    newCondition = new ConditionItem(itemForCondition);
                    break;

                case "action":
                    Action actionForCondition = null;
                    String actionStr = conditionJN.findPath("action").asText();

                    switch (actionStr) {
                        case "use":
                            itemForCondition = itemRepo.find(conditionJN.findPath("use").asText());
                            actionForCondition = new ActionUse(itemForCondition);
                            break;
                        case "hide":
                            actionForCondition = new ActionHide( conditionJN.findPath("context").asText() );
                            break;
                        case "jetpack":
                            actionForCondition = new ActionJetpack();
                    }

                    if (actionForCondition == null) {
                        System.out.println("Couldn't create condition action");
                        break; // breaks the switch
                    }
                    newCondition = new ConditionAction(actionForCondition);

                    if (conditionJN.has("context")) {
                        newCondition.setContext( conditionJN.findPath("context").asText() );
                    } else if (this.context != null) {
                        newCondition.setContext(this.context);
                    }
                    break;

            }

            if (newCondition == null) continue;

            this.addCondition(newCondition);
        }
    }

}
