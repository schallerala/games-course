package player.action.util;

import java.util.ArrayList;

/**
 * Created by schaller on 02/03/16.
 */
public class AttackResult {

    public boolean success;
    public ArrayList<String> rewards;
    public String context;

    public AttackResult(boolean success, ArrayList<String> rewards, String context) {
        this.success = success;
        this.rewards = rewards;
        this.context = context;
    }
}
