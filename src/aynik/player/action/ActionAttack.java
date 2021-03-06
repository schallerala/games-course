package aynik.player.action;

import com.fasterxml.jackson.databind.JsonNode;
import aynik.compiler.CompilerHelper;

import java.util.ArrayList;

/**
 * Created by schaller on 12/02/16.
 */
public class ActionAttack extends Action {

    public boolean needTeammate;
    public boolean loseTeammate;

    public ArrayList<String> rewards;

    public ActionAttack(boolean success) {
        super("Attack");
        this.success = success;
        this.needTeammate = false;
        this.loseTeammate = false;

        this.rewards = new ArrayList<>();
    }

    public void setContext(String context) {
        this.context = context;
    }

    public static ActionAttack compileRawAttack(JsonNode actionJN) {
        CompilerHelper compilerHelper = CompilerHelper.getInstance();
        
        ActionAttack actionAttack = new ActionAttack(
                actionJN.get("success").asBoolean()
        );
        
        if (actionJN.has("context")) {
            actionAttack.setContext(
                    compilerHelper.getString(actionJN.get("context"))
            );
        }

        if (actionJN.has("needTeammate") && actionJN.get("needTeammate").asBoolean()) {
            actionAttack.needTeammate = true;
        }
        if (actionJN.has("loseTeammate") && actionJN.get("loseTeammate").asBoolean()) {
            actionAttack.loseTeammate = true;
        }

        if (actionJN.has("rewards")) {
            for (JsonNode reward : actionJN.get("rewards")) {
                actionAttack.rewards.add(reward.asText());
            }
        }
        
        return actionAttack;
    }
}
