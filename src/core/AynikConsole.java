package core;

import map.Location;
import player.Player;
import player.action.Action;
import player.action.ActionHide;

import java.util.ArrayList;
import java.util.Observable;
import java.util.Scanner;

/**
 * Created by schaller on 02/03/16.
 */
public class AynikConsole extends Observable {
    private static AynikConsole ourInstance = new AynikConsole();
    private Scanner reader;

    public AynikStory story;
    public AynikTicker ticker;
    public Player player;
    public Aynik game;

    public static AynikConsole getInstance() {
        return ourInstance;
    }

    private AynikConsole() {
        reader = new Scanner(System.in);
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public void setStory(AynikStory story) {
        this.story = story;
    }

    public void setTicker(AynikTicker ticker) {
        this.ticker = ticker;
    }

    public void setGame(Aynik game) {
        this.game = game;
    }

    public void startGame() {
        boolean playerNameSet = false;

        while ( ! playerNameSet) {
            System.out.println("What is your name?");
            System.out.print("My name is:");
//            fixme
//            playerNameSet = player.setName(reader.nextLine().trim());
            playerNameSet = player.setName("Alain");

            if ( ! playerNameSet) System.out.println("Give a valid name please");
        }

        System.out.println(
                "\n" +
                "Hi " + player.name + ",\n" +
                "Welcome in Aynik\n" +
                "Wish you good luck to escape all this terror!" +
                "\n\n\n"
        );

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        this.story.printIntro();
    }

    public void printLanding() {
        System.out.print("You have been dropped in ");
        System.out.println(this.player.currentPosition);
    }

    public void print(String story) {
        System.out.println(story);
    }

    public void playerAction() {
        Location playerLocation = this.player.currentLocation;
        ArrayList<Action> availableAction = new ArrayList<>();

//        availableAction.add(new )
//
//        if (playerLocation.canHide) {
//            availableAction.add(new ActionHide())
//        }


        boolean newLocation = false;

        if (newLocation) {
            game.arrivingNewLocation();
        }
    }
}
