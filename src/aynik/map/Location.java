package aynik.map;

import aynik.player.action.*;
import com.fasterxml.jackson.databind.JsonNode;
import aynik.compiler.CompilerHelper;
import aynik.core.AynikItemsRepo;
import aynik.player.action.util.ActionUseCondition;
import aynik.player.item.Item;

import java.util.ArrayList;

/**
 * Created by schaller on 07/02/16.
 */
public class Location {

    public LocationTypes type;
    public ArrayList<Action> actions;

    public String context;

    public Location(JsonNode rawLocation) {
        this.type = LocationTypes.normal;
        this.actions = new ArrayList<>();

        this.init();

        if (rawLocation == null) return;

        this.compileRawLocation(rawLocation);
    }

    public Location(LocationTypes locationTypes, JsonNode rawLocation) {
        this(rawLocation);
        this.type = locationTypes;
    }



    public void init () {}

    protected void compileRawLocation(JsonNode rawLocation) {
        if (rawLocation.has("context")) {
            CompilerHelper compilerHelper = CompilerHelper.getInstance();
            this.setContext(compilerHelper.getString(rawLocation.get("context")));
        }

        if ( ! rawLocation.has("actions")) {
            this.actions.add( new ActionContinue() );
            return;
        }

        for (JsonNode actionJN : rawLocation.get("actions")) {
            if (actionJN.isObject()) {
                this.compileComplexAction(actionJN);
            }
            else
            {
                switch (actionJN.asText()) {
                    case "jetpack":
                        this.actions.add( new ActionJetpack() );
                        break;
                }
            }
        }

    }

    private void compileComplexAction(JsonNode actionJN) {
        AynikItemsRepo itemsRepo = AynikItemsRepo.getInstance();
        CompilerHelper compilerHelper = CompilerHelper.getInstance();

        String context = compilerHelper.getString(actionJN.get("context"));

        switch (actionJN.get("type").asText()) {
            case "attack":
                ActionAttack actionAttack = ActionAttack.compileRawAttack(actionJN);
                this.actions.add(actionAttack);
                break;
            case "hide":
                ActionHide actionHide = new ActionHide(context);
                actionHide.setSuccess(actionJN.findPath("success").asBoolean(true));
                this.actions.add(actionHide);
                break;
            case "escape":
                ActionEscape actionEscape = new ActionEscape();
                if (context != null) actionEscape.setContext(context);
                actionEscape.setSuccess(actionJN.findPath("success").asBoolean(true));
                this.actions.add(actionEscape);
                break;
            case "use":
                Item itemToUse = itemsRepo.find(actionJN.findPath("item").asText());

                ActionUse actionUse = new ActionUse(itemToUse);
                actionUse.setSuccess(actionJN.findPath("success").asBoolean(false));
                if (context != null) actionUse.setContext(context);

                if (actionJN.has("conditions")) {
                    ActionUseConditions actionUseConditions = new ActionUseConditions(actionUse);
                    for (JsonNode conditionJN : actionJN.get("conditions")) {
                        Item itemNeeded = itemsRepo.find(conditionJN.get("item").asText());
                        String itemContext = compilerHelper.getString(conditionJN.get("context"));
                        actionUseConditions.conditions.add(new ActionUseCondition(itemNeeded, itemContext));
                    }
                    this.actions.add(actionUseConditions);
                }
                else
                {
                    this.actions.add(actionUse);
                }
        }
    }

    public void setContext(String context) {
        this.context = context;
    }
}
