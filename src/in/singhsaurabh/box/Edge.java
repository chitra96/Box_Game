package in.singhsaurabh.box;

import java.io.Serializable;

/**
 * @author Saurabh Singh
 */
public class Edge implements Serializable {

    private Point p1;
    private Point p2;
    private boolean marked = false;
    private Player player;

    public Edge(Point p1, Point p2) {
        if (p1.compareTo(p2) < 0) {
            this.p1 = p1;
            this.p2 = p2;
        } else if (p1.compareTo(p2) > 0) {
            this.p1 = p2;
            this.p2 = p1;
        }
    }

    public Point getP2() {
        return p2;
    }

    public void setP2(Point p2) {
        this.p2 = p2;
    }

    public Point getP1() {
        return p1;
    }

    public void setP1(Point p1) {
        this.p1 = p1;
    }

    public boolean isMarked() {
        return marked;
    }

    public void setMarked(boolean marked) {
        this.marked = marked;
    }

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
        setMarked(true);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Edge) {
            return (p1.equals(((Edge) obj).getP1()) && p2.equals(((Edge) obj).getP2()));

        }
        return false;
    }

    @Override
    public String toString() {
        return "[" + p1 + ", " + p2 + "] " + marked;
    }

    @Override
    public int hashCode() {
        return toString().hashCode();
    }


}
