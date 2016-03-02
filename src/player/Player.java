package player;

import java.util.ArrayList;

/**
 * Created by schaller on 17/02/16.
 */
public class Player {
    private static Player ourInstance = new Player();

    public ArrayList<Teammate> teammates;

    public static Player getInstance() {
        return ourInstance;
    }

    private Player() {
        this.teammates = new ArrayList<>();
    }

    public void addTeammate (Teammate teammate) {
        this.teammates.add(teammate);
    }

    public void removeTeammate (Teammate teammate) {
        this.teammates.remove(teammate);
    }

    public boolean hasTeammate (Teammate teammate) {
        return this.teammates.size() > 0;
    }
}
