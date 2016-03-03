package core;

import compiler.AynikCompiler;
import compiler.CompilerMissingNodeException;
import map.Location;
import map.LocationDeath;
import map.LocationTypes;
import map.Position;
import player.Player;

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

        Aynik game = new Aynik(ticker, itemsRepo, map, console, player, story);

        console.setGame(game);

        game.startPlaying();
    }

    public void startPlaying () {
        this.console.startGame();

        this.randomizePlayerLocation();

        this.console.printLanding();
        this.arrivingNewLocation();
        while (this.player.isAlive()) {
            this.console.playerAction();
            this.ticker.next();
        }
    }

    public void arrivingNewLocation() {
        Location playerLocation = this.player.currentLocation;
        if (this.player.currentLocation.type == LocationTypes.death) {
            LocationDeath deathLocation = (LocationDeath) playerLocation;
            this.console.print(deathLocation.story);
            this.player.die();
        }
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
}
