package simulation;

public class Pair {
    private int x;
    private int y;

    public Pair() {
        this.x = 0;
        this.y = 0;
    }

    public Pair(final int x, final int y) {
        this.x = x;
        this.y = y;
    }

    public Pair(final Pair p) {
        this.x = p.x;
        this.y = p.y;
    }

    /**
     *
     * @return the x coordinate
     */
    public int getX() {
        return x;
    }

    /**
     *
     * @return the y coordinate
     */
    public int getY() {
        return y;
    }

    /**
     * sets the x field
     * @param x
     */
    public void setX(final int x) {
        this.x = x;
    }

    /**
     * sets the y field
     * @param y
     */
    public void setY(final int y) {
        this.y = y;
    }
}
