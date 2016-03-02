package map.util;

import player.action.Action;
import player.action.ActionAttack;
import player.action.util.AttackResult;

/**
 * Created by schaller on 02/03/16.
 */
public class ChoiceActionAttack extends ChoiceAction {

    public AttackResult result;

    public ChoiceActionAttack(AttackResult result) {
        super(new ActionAttack());
        this.result = result;
    }
}
