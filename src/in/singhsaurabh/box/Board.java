package in.singhsaurabh.box;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * @author Saurabh Singh
 */
public class Board {

    int n;
    Set<Edge> edges;
    Map<Point, Square> squares;

    public Board(int n) {
        this.n = n;
        edges = new HashSet<>();
        squares = new HashMap<>();

        init();
    }

    private void init() {
        for (int i = 0; i < n - 1; i++) {
            for (int j = 0; j < n - 1; j++) {
                squares.put(new Point(i, j), new Square());
            }
        }
        Edge tempEdge;
        for (int i = 0; i < n - 1; i++) {
            for (int j = 1; j < n - 1; j++) {
                tempEdge = new Edge(new Point(i, j), new Point(i + 1, j));
                edges.add(tempEdge);
                squares.get(new Point(i, j)).addEdge(tempEdge);
                squares.get(new Point(i, j - 1)).addEdge(tempEdge);
                tempEdge = new Edge(new Point(j, i), new Point(j, i + 1));
                edges.add(tempEdge);
                squares.get(new Point(j, i)).addEdge(tempEdge);
                squares.get(new Point(j - 1, i)).addEdge(tempEdge);
            }
        }
        for (int i = 0; i < n - 1; i++) {
            tempEdge = new Edge(new Point(i, 0), new Point(i + 1, 0));
            edges.add(tempEdge);
            squares.get(new Point(i, 0)).addEdge(tempEdge);

            tempEdge = new Edge(new Point(i, n - 1), new Point(i + 1, n - 1));
            edges.add(tempEdge);
            squares.get(new Point(i, n - 2)).addEdge(tempEdge);

            tempEdge = new Edge(new Point(0, i), new Point(0, i + 1));
            edges.add(tempEdge);
            squares.get(new Point(0, i)).addEdge(tempEdge);

            tempEdge = new Edge(new Point(n - 1, i), new Point(n - 1, i + 1));
            edges.add(tempEdge);
            squares.get(new Point(n - 2, i)).addEdge(tempEdge);
        }
    }

    public void markEdge(Edge edge, Player pl) {
        for (Edge temp : edges) {
            if (temp.equals(edge)) {
                temp.setPlayer(pl);
                break;
            }
        }
    }

    public boolean canBeMarked(Edge edge) {
        for (Edge temp : edges) {
            if (temp.equals(edge) && !temp.isMarked()) {
                return true;
            }
        }
        return false;
    }

    public boolean markSquare(Player p) {
        boolean flag = false;
        for (Square sq : squares.values()) {
            if (sq.mark(p)) {
                flag = true;
            }
        }
        return flag;
    }

    public boolean isAllMarked() {
        for (Edge e : edges) {
            if (!e.isMarked()) return false;
        }
        return true;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (Edge e : edges) {
            sb.append(e).append("\n");
        }
        sb.append("\n");
        return sb.toString();
    }

    public String squaresToString() {
        StringBuilder sb = new StringBuilder();
        for (Square s : squares.values()) {
            sb.append(s).append("\n");
        }
        return sb.toString();
    }

}
