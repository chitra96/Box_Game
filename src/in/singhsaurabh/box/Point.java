package in.singhsaurabh.box;

import java.io.Serializable;

/**
 * @author Saurabh Singh
 */
public class Point implements Comparable<Point>, Serializable {

    private int i;
    private int j;
    private boolean marked;

    public Point(int i, int j) {
        this.i = i;
        this.j = j;
        this.marked = false;
    }


    @Override
    public boolean equals(Object obj) {
        boolean flag = false;
        if (obj instanceof Point) {
            Point p = (Point) obj;
            flag = Integer.compare(p.getI(), i) == 0;
            flag = flag && Integer.compare(p.getJ(), j) == 0;
        }
        return flag;
    }

    public int getI() {
        return i;
    }

    public void setI(int i) {
        this.i = i;
    }

    public int getJ() {
        return j;
    }

    public void setJ(int j) {
        this.j = j;
    }

    public boolean isMarked() {
        return marked;
    }

    public void setMarked(boolean marked) {
        this.marked = marked;
    }

    @Override
    public String toString() {
        return "(" + i + "," + j + ")";
    }

    @Override
    public int hashCode() {
        return toString().hashCode();
    }

    @Override
    public int compareTo(Point o) {
        if (this.i == o.getI()) {
            return Integer.compare(this.j, o.getJ());
        } else if (this.j == o.getJ()) {
            return Integer.compare(this.i, o.getI());
        }
        return 0;
    }


}
