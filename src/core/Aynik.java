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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/**
 * Created by schaller on 05/02/16.
 */
public class Aynik {

    private static final String GAME_FILE_PATH = "data/game-data.json";

    private static AynikCompiler compiler;

    private AynikTicker ticker;
    private AynikMap map;
    private AynikConsole console;

    private Player player;

    public Aynik(AynikTicker ticker, AynikMap map, AynikConsole console, Player player) {
        this.ticker = ticker;
        this.map = map;
        this.console = console;
        this.player = player;
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

        AynikMap map = AynikMap.getInstance();
        Player player = Player.getInstance();
        AynikStory story = AynikStory.getInstance();
        AynikTicker ticker = AynikTicker.getInstance();
        AynikConsole console = AynikConsole.getInstance();
        console.setPlayer(player);
        console.setStory(story);
        console.setTicker(ticker);
        console.setMap(map);

        Aynik game = new Aynik(ticker, map, console, player);

        console.setGame(game);

        game.startPlaying();
    }

    public void startPlaying () {
        Position originPosition;
        boolean needNewFirstLocation = true;
        this.console.startGame();

        boolean playerWannaPlay = true;

        this.randomizePlayerLocation();
        originPosition = this.player.currentPosition;
        needNewFirstLocation = this.player.currentLocation.type == LocationTypes.death;

        while (playerWannaPlay) {
            this.console.printLanding();
            this.arrivingLocation(true);
            while (this.player.isAlive() && !this.player.won()) {
                this.console.playerAction();
                this.ticker.next();
            }

            playerWannaPlay = this.console.askPlayerWannaTryAgain();
            if (playerWannaPlay) {
                if (needNewFirstLocation) {
                    this.randomizePlayerLocation();
                    originPosition = this.player.currentPosition;
                    needNewFirstLocation = this.player.currentLocation.type == LocationTypes.death;
                }
                this.player.resetPlayer(originPosition, this.map.get(originPosition));
            }
        }
    }

    public void arrivingLocation(boolean justParachute) {
        Location playerLocation = this.player.currentLocation;
        if (playerLocation.type == LocationTypes.end) {
            this.player.win = true;
            this.console.printEnd();
        } else if (this.player.currentLocation.type == LocationTypes.death) {
            LocationDeath deathLocation = (LocationDeath) playerLocation;
            if (justParachute) this.console.printUnluckyParachute();
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
        HashMap<Position, Location> row = this.map.getRow(1);
        ArrayList<Position> positionCandidates = new ArrayList<>();

        for (Map.Entry<Position, Location> positionLocationEntry : row.entrySet()) {
            Location location = positionLocationEntry.getValue();
            if (location.type == LocationTypes.obstacle) continue;

            positionCandidates.add(positionLocationEntry.getKey());
        }

        int candidatesSize = positionCandidates.size();
        int randomPositionIndex = new Random().nextInt(candidatesSize);
        Position firstPosition = positionCandidates.get(randomPositionIndex);

        this.player.changeLocation(firstPosition, row.get(firstPosition));
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
        this.arrivingLocation(false);
    }
}
