package map.util;

import player.action.Action;

/**
 * Created by schaller on 12/02/16.
 */
public class ChoiceAction extends Choice {

    public String context;
    public Action action;

    public ChoiceAction(Action action) {
        super(ChoiceTypes.action);
        this.action = action;
    }

    public void setContext(String context) {
        this.context = context;
    }
}
