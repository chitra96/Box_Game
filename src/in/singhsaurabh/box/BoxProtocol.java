package in.singhsaurabh.box;

import java.io.Serializable;

public class BoxProtocol implements Serializable {
    public enum Type {
        MOVE, AUTH, REG, PLAYER, PLAYERS
    }


}
