package core;

import compiler.AynikCompiler;
import compiler.CompilerMissingNodeException;
import map.*;
import player.Player;
import player.action.*;
import player.action.util.ActionUseCondition;
import player.item.Item;

import java.io.File;
import java.io.IOException;
import java.util.*;

/**
 * Created by schaller on 05/02/16.
 */
public class Aynik {

    private static final String GAME_FILE_PATH = "data/game-data.json";

    private static AynikCompiler compiler;

    private AynikTicker ticker;
    private AynikItemsRepo itemsRepo;
    private AynikMap map;
    private AynikConsole console;
    private AynikStory story;

    private Player player;

    public Aynik(AynikTicker ticker, AynikItemsRepo itemsRepo, AynikMap map, AynikConsole console, Player player, AynikStory story) {
        this.ticker = ticker;
        this.itemsRepo = itemsRepo;
        this.map = map;
        this.console = console;
        this.player = player;
        this.story = story;
    }

    public static void main (String[] args) {
        try {
            compiler = new AynikCompiler(new File(GAME_FILE_PATH));
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(0);
        } catch (CompilerMissingNodeException e) {
            e.printStackTrace();
            System.exit(0);
        }

        try {
            compiler.prepareGame();
        } catch (CompilerMissingNodeException e) {
            e.printStackTrace();
        }

        AynikItemsRepo itemsRepo = AynikItemsRepo.getInstance();
        AynikMap map = AynikMap.getInstance();
        Player player = Player.getInstance();
        AynikStory story = AynikStory.getInstance();
        AynikTicker ticker = AynikTicker.getInstance();
        AynikConsole console = AynikConsole.getInstance();
        console.setPlayer(player);
        console.setStory(story);
        console.setTicker(ticker);
        console.setMap(map);

        Aynik game = new Aynik(ticker, itemsRepo, map, console, player, story);

        console.setGame(game);

        game.startPlaying();
    }

    public void startPlaying () {
        this.console.startGame();

        this.randomizePlayerLocation();

        this.console.printLanding();
        this.arrivingLocation();
        while (this.player.isAlive()) {
            this.console.playerAction();
            this.ticker.next();
        }
    }

    public void arrivingLocation() {
        Location playerLocation = this.player.currentLocation;
        if (this.player.currentLocation.type == LocationTypes.death) {
            LocationDeath deathLocation = (LocationDeath) playerLocation;
            this.playerDie();
            this.console.printContext(deathLocation.story);
        } else {
            if (this.player.currentLocation.type == LocationTypes.itemsDiscovery) {
                LocationItemsDiscovery currentLocationItemsDiscovery = (LocationItemsDiscovery) this.player.currentLocation;
                this.player.addItems(currentLocationItemsDiscovery.items);
                this.console.printGotItems(currentLocationItemsDiscovery.items);
            }

            this.console.printNewLocation();
        }
    }

    private void playerDie() {
        this.console.playerDie();
        this.player.die();
    }

    private void randomizePlayerLocation() {
//        HashMap<Position, Location> row = this.map.getRow(1);
//        ArrayList<Position> positionCandidates = new ArrayList<>();
//
//        for (Map.Entry<Position, Location> positionLocationEntry : row.entrySet()) {
//            Location location = positionLocationEntry.getValue();
//            if (location.type == LocationTypes.obstacle) continue;
//
//            positionCandidates.add(positionLocationEntry.getKey());
//        }
//
//        int candidatesSize = positionCandidates.size();
//        int randomPositionIndex = new Random().nextInt(candidatesSize);
//        Position firstPosition = positionCandidates.get(randomPositionIndex);

//        this.player.changeLocation(firstPosition, row.get(firstPosition));
        Position firstPosition = this.map.getPosition('B', 1);
        this.player.changeLocation(firstPosition, this.map.get(firstPosition));
    }

    public void applyAction(Action action) {
        if (action.hasContext()) {
            this.console.printContext(action.context);
        }

        if ( ! action.success) {
            this.playerDie();
            return;
        }

        if (action instanceof ActionAttack) {
            ActionAttack actionAttack = (ActionAttack) action;

            if ( ! action.hasContext()) {
                this.console.printSuccessfulAttack();
            }

            if (actionAttack.rewards.size() > 0) {
                for (String reward : actionAttack.rewards) {
                    if (reward.equals("teammate")) {
                        this.console.printGotATeammate();
                        this.player.asTeammate = true;
                    }
                }
            }

            if (actionAttack.needTeammate && ! this.player.asTeammate) {
                this.playerDie();
                return;
            }

            if (actionAttack.loseTeammate) {
                this.console.printTeammateLose();
                this.player.asTeammate = false;
            }
        }

        if (action instanceof ActionJetpack) {
            this.player.usedJetpack = true;
        }

        if (action instanceof ActionUse) {
            Item item = ((ActionUse) action).item;
            this.player.usedItem(item);
            if (! action.hasContext()) this.console.printSuccessfulUse(item);

            if (action instanceof ActionUseConditions) {
                ActionUseConditions actionUseConditions = (ActionUseConditions) action;
                for (ActionUseCondition condition : actionUseConditions.conditions) {
                    if (! this.player.hasItem(condition.item)) {
                        this.console.printContext(condition.context);
                        this.playerDie();
                    }
                }

            }
        }

        if ( ! (action instanceof ActionHide)) {
            try {
                this.console.makeThePlayerMove(action);
            } catch (Exception e) {
                this.console.print("Error in the program...");
                this.playerDie();
                e.printStackTrace();
            }
        }
    }

    public void movePlayerTo(Position position) {
        this.player.changeLocation(position, this.map.get(position));
        this.arrivingLocation();
    }
}
