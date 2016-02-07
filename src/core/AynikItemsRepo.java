package core;

/**
 * Created by schaller on 08/02/16.
 */
public class AynikItemsRepo {
    private static AynikItemsRepo ourInstance = new AynikItemsRepo();

    public static AynikItemsRepo getInstance() {
        return ourInstance;
    }

    private AynikItemsRepo() {
    }
}
