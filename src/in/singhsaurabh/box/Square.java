package in.singhsaurabh.box;

import java.util.HashSet;
import java.util.Set;

/**
 * @author Saurabh Singh
 */
public class Square {

    Set<Edge> edges;
    boolean marked;
    private Player player;

    public Square() {
        edges = new HashSet<>();
    }

    public Square(Edge p1, Edge p2, Edge p3, Edge p4) {
        this();
        edges.add(p1);
        edges.add(p2);
        edges.add(p3);
        edges.add(p4);
    }

    public void addEdge(Edge e) {
        edges.add(e);
    }

    public boolean mark(Player own) {
        if (canBeMarked()) {
            setPlayer(own);
            own.incScore(1);
            return true;
        }
        return false;
    }


    public boolean canBeMarked() {
        if (marked) return false;
        for (Edge p : edges) {
            if (!p.isMarked()) {
                return false;
            }
        }
        return true;
    }

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
        marked = true;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(edges.size());
        sb.append("[");
        for (Edge p : edges) {
            sb.append(p).append(" ");
        }
        sb.deleteCharAt(sb.length() - 1);
        sb.append("]");
        sb.append(marked ? "Marked " + player : "Unmarked");

        return sb.toString();
    }


}
