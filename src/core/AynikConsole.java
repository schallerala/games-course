package core;

import com.sun.org.apache.xpath.internal.operations.Bool;
import map.Location;
import map.LocationObstacle;
import map.LocationTypes;
import map.Position;
import player.Player;
import player.action.*;
import player.item.Item;

import java.lang.reflect.Array;
import java.util.*;

/**
 * Created by schaller on 02/03/16.
 */
public class AynikConsole extends Observable {
    private static AynikConsole ourInstance = new AynikConsole();
    private Scanner reader;

    public AynikStory story;
    public AynikTicker ticker;
    private AynikMap map;
    public Player player;

    public Aynik game;
    private char[] alphabet;

    public static AynikConsole getInstance() {
        return ourInstance;
    }

    private AynikConsole() {
        this.reader = new Scanner(System.in);
        this.alphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZ".toCharArray();
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
            playerNameSet = player.setName(reader.nextLine().trim());

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

    public void print(String whatToPrint) {
        System.out.println(whatToPrint);
    }

    public void playerAction() {
        Location playerLocation = this.player.currentLocation;

        if (playerLocation.actions.size() == 1 && playerLocation.actions.get(0) instanceof ActionContinue) {
            try {
                this.makeThePlayerMove(playerLocation.actions.get(0));
            } catch (Exception e) {
                System.out.println();
                e.printStackTrace();
            }
            return;
        }

        System.out.println("################################");

        HashMap<Character, Action> availableActions = new HashMap<>();

        System.out.println();
        int j = 0;
        for (Action locationAction : playerLocation.actions) {
            char actionCharacter = Character.toUpperCase(this.alphabet[j]);

            if (locationAction instanceof ActionUse) {
                ActionUse locationActionUse = (ActionUse) locationAction;
                if ( ! this.player.hasItem(locationActionUse.item)) continue;
            }
            if (locationAction instanceof  ActionJetpack) {
                if (this.player.usedJetpack) continue;
            }

            availableActions.put(actionCharacter, locationAction);
            System.out.println(
                    "\t" +
                            actionCharacter + ")" +
                            "\t" +
                            locationAction.action
            );

            j++;
        }
        System.out.println();

        String answerRegex = "[A-" + this.alphabet[j] + "]";

        boolean validAnswer = false;
        String playerSelectedAction = "";
        while ( ! validAnswer) {
            System.out.println("Which action do you choose? ");
            playerSelectedAction = this.reader.nextLine().toUpperCase().trim();
            validAnswer = playerSelectedAction.matches(answerRegex);

            if ( ! validAnswer) System.out.println("Select a valid action!\n");
        }

        this.game.applyAction(availableActions.get(playerSelectedAction.charAt(0)));
    }

    public void playerDie() {
        System.out.println();
        System.out.println("You died, try again..");
        System.out.println();
    }

    public void printTeammateLose() {
        System.out.println("Your teammate helped to survive, but lost his live.");
        System.out.println("From now on, you are alone again.");
    }

    public void makeThePlayerMove(Action action) throws Exception {
        ArrayList<Position> availablePosition = this.calculateAvailablePosition(action instanceof ActionJetpack);

        System.out.println("--------------------------------");

        HashMap<Character, Position> availablePositions = new HashMap<>();
        int j = 0;

        System.out.println();
        for (Position position : availablePosition) {
            Location reachableLocation = this.map.get(position);
            boolean positionReachable = reachableLocation.type != LocationTypes.obstacle;

            if (positionReachable) {
                availablePositions.put(this.alphabet[j], position);
                System.out.print("\t" + this.alphabet[j] + ")");
                j++;
            }
            System.out.print("\t" + position);

            if ( ! positionReachable) {
                LocationObstacle obstacle = (LocationObstacle) reachableLocation;
                System.out.print(" - " + obstacle.obstacle);
            }
            System.out.println();
        }
        System.out.println();

        String answerRegex = "[A-" + this.alphabet[j] + "]";

        boolean validAnswer = false;
        String playerSelectedPosition = "";
        while ( ! validAnswer) {
            System.out.println("Where do you wanna go?");
            playerSelectedPosition = this.reader.nextLine().toUpperCase().trim();
            validAnswer = playerSelectedPosition.matches(answerRegex);

            if ( ! validAnswer) System.out.println("Select a valid position!\n");
        }

        this.game.movePlayerTo(availablePositions.get(playerSelectedPosition.charAt(0)));
    }

    private ArrayList<Position> calculateAvailablePosition(boolean forJetpack) throws Exception {
        Position currentPosition = this.player.currentPosition;
        int currentPositionXIndex = -1;

        for (int i = 0; i < this.alphabet.length; i++) {
            char letter = this.alphabet[i];
            if (currentPosition.x == letter) {
                currentPositionXIndex = i;
                break;
            }
        }

        if (currentPositionXIndex == -1) {
            throw new Exception("Could calculate available position");
        }

        int topperLeftCornerXIndex;
        int topperLeftCornerY;

        int reachableDistance = forJetpack ? 2 : 1;

        topperLeftCornerY = currentPosition.y + reachableDistance;
        topperLeftCornerXIndex = currentPositionXIndex - reachableDistance;

        ArrayList<Position> availablePositions = new ArrayList<>();
        Position availablePosition;
        for (int i = 0; i <= reachableDistance * 2; i++) {
            if (forJetpack && (i == 0 || i == reachableDistance * 2)) continue;;

            int calculatedXIndex = topperLeftCornerXIndex + i;
            Character calculatedX = this.getCharacter(calculatedXIndex);

            if (calculatedX != null) {
                // topper row
                availablePosition = this.map.getPosition(calculatedX, topperLeftCornerY);
                if (availablePosition != null) availablePositions.add(availablePosition);

                // lower row
                availablePosition = this.map.getPosition(calculatedX, topperLeftCornerY - reachableDistance * 2);
                if (availablePosition != null) availablePositions.add(availablePosition);
            }


            if (i == 0 || i == reachableDistance * 2) continue;

            int calculatedY = topperLeftCornerY - i;

            // left col
            Character topperLeftCornerX = this.getCharacter(topperLeftCornerXIndex);
            if (topperLeftCornerX != null) {
                availablePosition = this.map.getPosition(this.getCharacter(topperLeftCornerXIndex), calculatedY);
                if (availablePosition != null) availablePositions.add(availablePosition);
            }

            // right col
            calculatedX = this.getCharacter(topperLeftCornerXIndex + reachableDistance * 2);
            if (calculatedX != null) {
                availablePosition = this.map.getPosition(calculatedX, calculatedY);
                if (availablePosition != null) availablePositions.add(availablePosition);
            }
        }

        return availablePositions;
    }

    private Character getCharacter(int calculatedXIndex) {
        return calculatedXIndex >= 0 && calculatedXIndex < this.alphabet.length ? this.alphabet[calculatedXIndex] : null;
    }

    public void setMap(AynikMap map) {
        this.map = map;
    }

    public void printNewLocation() {
        System.out.println();
        System.out.println("You are now in " + this.player.currentPosition);
        if (this.player.currentLocation.context != null) {
            System.out.println(this.player.currentLocation.context);
        }
        System.out.println();
    }

    public void printContext(String context) {
        System.out.println();
        System.out.println(context);
        System.out.println();
    }

    public void printGotATeammate() {
        System.out.println();
        System.out.println("You are not alone now, and got a teammate!");
        System.out.println();
    }

    public void printGotItems(ArrayList<Item> items) {
        System.out.println();
        System.out.println("You got some new stuff!");
        for (Item item : items) {
            System.out.println("\t" + item.name);
        }
        System.out.println();

    }

    public void printSuccessfulAttack() {
        System.out.println("Your attack was successful!");
        System.out.println();
    }

    public void printSuccessfulUse(Item item) {
        System.out.println("Use of " + item.name + " was successful");
        System.out.println();
    }

    public void printEnd() {
        System.out.println();
        this.story.printEnd();
    }

    public void printUnluckyParachute() {
        System.out.println("You were not really lucky and have been drop on a deathly location...");
    }

    public boolean askPlayerWannaTryAgain() {
        System.out.println();
        System.out.println("################################");
        System.out.println(this.player.name + " do you still wanna play?");

        HashMap<Character, Boolean> wantsToContinue = new HashMap<>();
        for (int i = 0; i < 2; i++) {
            wantsToContinue.put(this.alphabet[i], i % 2 == 0);
            System.out.println(
                    this.alphabet[i] + ")" +
                    "\t" + (i % 2 == 0 ? "Yes" : "No")
            );
        }
        System.out.println();

        boolean playerWannaContinue = false;

        String answerRegex = "[AB]";

        boolean validAnswer;
        String playAnswer;

        System.out.println("Do you wanna continue to play");
        playAnswer = this.reader.nextLine().toUpperCase().trim();
        validAnswer = playAnswer.matches(answerRegex);

        if ( ! validAnswer) {
            System.out.println("Not valid answer, guess you don't want to continue anymore");
            playerWannaContinue = false;
        }
        else playerWannaContinue = wantsToContinue.get(playAnswer.charAt(0));

        return playerWannaContinue;
    }
}
