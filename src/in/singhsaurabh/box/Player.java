package in.singhsaurabh.box;

import java.awt.*;
import java.io.Serializable;

/**
 * @author Saurabh Singh
 */
public class Player implements Serializable {
    static final Player DUMMY = new Player();
    private String name;
    private String id;
    private int score;
    private Color color;
    private boolean ready = false;


    private Player() {
        name = "Not Started";
        color = Color.lightGray;
    }

    public Player(String name, String id) {
        this.name = name;
        this.id = id;
        score = 0;
        color = Color.lightGray;
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public void incScore(int inc) {
        score += inc;
    }


    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public boolean isReady() {
        return ready;
    }

    public void setReady(boolean ready) {
        this.ready = ready;
    }

    @Override
    public String toString() {
        return name;
    }


    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Player) {
            return id.equals(((Player) obj).getId());
        }
        return false;
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }


}
