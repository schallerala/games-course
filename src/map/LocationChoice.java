package map;

import com.fasterxml.jackson.databind.JsonNode;
import compiler.CompilerHelper;
import map.util.Choice;
import map.util.ChoiceAction;
import map.util.ChoiceActionAttack;
import player.action.Action;
import player.action.ActionAttack;
import player.action.ActionEscape;
import player.action.ActionJetpack;
import player.action.util.AttackResult;

import java.util.ArrayList;

/**
 * Created by schaller on 07/02/16.
 */
public class LocationChoice extends Location {

    public ArrayList<Choice> choices;

    public LocationChoice(JsonNode rawLocation) {
        super(LocationTypes.choice, rawLocation);
    }

    @Override
    public void init() {
        this.choices = new ArrayList<>();
    }

    @Override
    protected void compileRawLocation(JsonNode rawLocation) {
        CompilerHelper compilerHelper = CompilerHelper.getInstance();

        Choice locationChoice;

        for (JsonNode choiceJN : rawLocation.findPath("choices")) {
            locationChoice = null;
            Action choiceAction = null;

            switch (choiceJN.findPath("type").asText()) {
                case "action":

                    switch (choiceJN.findPath("action").asText()) {
                        case "escape":
                            choiceAction = new ActionEscape();
                            break;
                        case "jetpack":
                            choiceAction = new ActionJetpack();
                            break;
                        case "attack":
                            JsonNode attackJN = choiceJN.findPath("attack");

                            boolean attackSuccess = attackJN.findPath("success").asBoolean();
                            ArrayList<String> attackRewards = new ArrayList<>();
                            for (JsonNode rewardJN : attackJN.findPath("rewards")) {
                                attackRewards.add(rewardJN.asText());
                            }
                            String attackContext = compilerHelper.getString(attackJN.findPath("context"));

                            AttackResult attackResult = new AttackResult(attackSuccess, attackRewards, attackContext);
                            locationChoice = new ChoiceActionAttack(attackResult);
                            break;
                    }
                    break;
            }

            if (locationChoice == null) {
                if (choiceAction == null) continue;

                locationChoice = new ChoiceAction(choiceAction);
            }

            this.addChoice(locationChoice);
        }
    }

    public void addChoice(Choice newChoice) {
        this.choices.add(newChoice);
    }
}
