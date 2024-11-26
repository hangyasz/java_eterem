package terem;

/**
 * Az osztály a terem méretét tárolja
 */

public class terem {
    private int x;//terem szélessége
    private int y;//terem magassága

    /**
     * Konstruktor
     * @param x A terem szélessége
     * @param y A terem magassága
     */
    public terem(int x, int y) {
        this.x = x;
        this.y = y;
    }

    /**
     * A terem szélességének lekérdezése
     * @return A terem szélessége
     */
    public int getX() {
        return x;
    }

    /**
     * A terem magasságának lekérdezése
     * @return A terem magassága
     */
    public int getY() {
        return y;
    }

}
