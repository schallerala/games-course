package player.action;

/**
 * Created by schaller on 17/02/16.
 */
public class ActionHide extends Action {

    public String context;

    public ActionHide(String context) {
        super("Hide");

        this.context = context;
    }
}
