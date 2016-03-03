package player.action;

/**
 * Created by schaller on 12/02/16.
 */
public abstract class Action {

    public String action;
    public boolean success;
    public String context;

    public Action(String action) {
        this.action = action;
        this.success = false;
        this.context = "";
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public void setContext(String context) {
        this.context = context;
    }

    public boolean hasContext() {
        return this.context != null && this.context.length() > 0;
    }
}
