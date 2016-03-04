package aynik.player;

import aynik.map.Location;
import aynik.map.Position;
import aynik.player.item.Item;

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

    public boolean usedJetpack;
    public boolean win;

    public static Player getInstance() {
        return ourInstance;
    }

    private Player() {
        this.alive = true;
        this.asTeammate = false;
        this.usedJetpack = false;
        this.items = new ArrayList<>();
        this.win = false;
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

    public boolean won() {
        return win;
    }

    public void die() {
        this.alive = false;
    }

    public void getNewItem (Item item) {
        this.items.add(item);
    }

    public boolean hasItem(Item item) {
        return this.items.contains(item);
    }

    public void addItems(ArrayList<Item> items) {
        this.items.addAll(items);
    }

    public void usedItem(Item item) {
        this.items.remove(item);
    }

    public void resetPlayer(Position originPosition, Location originLocation) {
        this.changeLocation(originPosition, originLocation);
        this.alive = true;
        this.asTeammate = false;
        this.usedJetpack = false;
        this.items = new ArrayList<>();
        this.win = false;
    }
}
