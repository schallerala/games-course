package player;

import map.Location;
import map.Position;
import player.item.Item;

import java.lang.reflect.Array;
import java.util.ArrayList;

/**
 * Created by schaller on 17/02/16.
 */
public class Player {
    private static Player ourInstance = new Player();

    public String name;
    public boolean alive;

    public Location currentLocation;
    public Position currentPosition;

    public boolean asTeammate;

    public ArrayList<Item> items;

    public static Player getInstance() {
        return ourInstance;
    }

    private Player() {
        this.alive = true;
        this.asTeammate = false;
        this.items = new ArrayList<>();
    }

    public boolean setName(String name) {
        boolean goodFormattedName = name.matches("([A-Z]?[a-z0-9]{1,}[\\s\\-_]){0,3}([A-Z]?[a-z0-9]{1,})");
        if (goodFormattedName) {
            this.name = name;
        }

        return goodFormattedName;
    }

    public void changeLocation(Position pos, Location location) {
        this.currentPosition = pos;
        this.currentLocation = location;
    }

    public boolean isAlive() {
        return alive;
    }

    public void die() {
        this.alive = false;
    }

    public void getNewItem (Item item) {
        this.items.add(item);
    }
}