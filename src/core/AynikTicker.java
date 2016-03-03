package core;

/**
 * Created by schaller on 16/02/16.
 */
public class AynikTicker {
    private static AynikTicker ourInstance = new AynikTicker();

    private int tick;

    public static AynikTicker getInstance() {
        return ourInstance;
    }

    private AynikTicker() {
        this.tick = 1;
    }

    public int getTick() {
        return tick;
    }

    public void next() {
        tick++;
    }
}
