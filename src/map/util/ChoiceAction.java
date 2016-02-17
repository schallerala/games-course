package map.util;

/**
 * Created by schaller on 12/02/16.
 */
public class ChoiceAction extends Choice {

    public String context;


    public ChoiceAction(ChoiceTypes type) {
        super(type);
    }

    public ChoiceAction(ChoiceTypes type, String context) {
        super(ChoiceTypes.action);
        this.context = context;
    }
}
