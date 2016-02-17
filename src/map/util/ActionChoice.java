package map.util;

import player.action.Action;

/**
 * Created by schaller on 12/02/16.
 */
public abstract class ActionChoice {

    public Action type;

    public ActionChoice(Action type) {
        this.type = type;
    }
}
