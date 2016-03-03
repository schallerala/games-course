package core;

import java.util.ArrayList;

/**
 * Created by schaller on 02/03/16.
 */
public class AynikStory {
    private static AynikStory ourInstance = new AynikStory();

    public ArrayList<String> intro;
    public ArrayList<String> end;

    public static AynikStory getInstance() {
        return ourInstance;
    }

    private AynikStory() {
        this.intro = new ArrayList<>();
        this.end = new ArrayList<>();
    }

    public void addToIntro (String part) {
        this.intro.add(part);
    }

    public void printIntro() {
        try {
            for (String part : intro) {
                System.out.println(part);
                Thread.sleep(700);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println();
    }

    public void printEnd() {
        try {
            for (String part : end) {
                System.out.println(part);
                Thread.sleep(700);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println();
    }

    public void addToEnd(String part) {
        this.end.add(part);
    }
}
