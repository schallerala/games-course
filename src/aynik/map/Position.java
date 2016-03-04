package aynik.map;

/**
 * Created by schaller on 07/02/16.
 */
public class Position {
    public char x;
    public int y;

    public Position(char x, int y) {
        this.x = Character.toUpperCase(x);
        this.y = y;
    }

    public Position(String position) throws Exception {
        if (position.length() != 2) throw new Exception("not handled position");
        this.x = position.charAt(0);
        this.y = Integer.parseInt(position.substring(1));
    }

    @Override
    public String toString() {
        return x + (y + "");
    }
}
