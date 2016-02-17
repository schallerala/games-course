package core;

import compiler.AynikCompiler;
import compiler.CompilerMissingNodeException;
import map.LocationTypes;

import java.io.File;
import java.io.IOException;

/**
 * Created by schaller on 05/02/16.
 */
public class Aynik {

    private static final String GAME_FILE_PATH = "data/game-data.json";

    private static AynikCompiler compiler;

    private AynikTicker ticker;
    private AynikItemsRepo itemsRepo;
    private AynikMap map;

    public Aynik(AynikTicker ticker, AynikItemsRepo itemsRepo, AynikMap map) {
        this.ticker = ticker;
        this.itemsRepo = itemsRepo;
        this.map = map;
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

        AynikTicker ticker = AynikTicker.getInstance();
        AynikItemsRepo itemsRepo = AynikItemsRepo.getInstance();
        AynikMap map = AynikMap.getInstance();
        Aynik game = new Aynik(ticker, itemsRepo, map);
        game.startPlaying();
    }

    public void startPlaying () {
        System.out.println("ready");
    }

}
