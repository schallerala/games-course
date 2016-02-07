package core;

import compiler.AynikCompiler;

import java.io.File;
import java.io.IOException;

/**
 * Created by schaller on 05/02/16.
 */
public class Aynik {

    private static final String GAME_FILE_PATH = "data/game-data.json";
    private static AynikCompiler compiler;

    public static void main (String[] args) {
        try {
            compiler = new AynikCompiler(new File(GAME_FILE_PATH));
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(0);
        }

        compiler.prepareGame(new Aynik());
    }

}
