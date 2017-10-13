package in.singhsaurabh.box;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;

/**
 * @author Saurabh Singh
 */
public class Game implements Runnable {
    ArrayList<Player> players;
    Player player, currPlayer, mainPlayer;
    boolean running = false;
    Board board;
    Client client;
    int n;
    BoardPanel bp;
    private Object lock = new Object();

    public Game(Player pl) throws UnknownHostException {
        players = new ArrayList<>();
        player = pl;
        client = new Client(InetAddress.getLocalHost().getHostAddress());
        currPlayer = Player.DUMMY;
        client.write(pl);
    }

    public Game(Player pl, String addr) throws UnknownHostException {
        players = new ArrayList<>();
        player = pl;
        client = new Client(addr);
        currPlayer = Player.DUMMY;
        client.write(pl);
    }

    public void setBoardSize() {
        int tempSize = client.getSize();
        this.n = tempSize < 2 ? 2 : tempSize;
        board = new Board(this.n);
    }

    @Override
    public void run() {
        running = true;
        while (!client.gameStarted()) {
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {

            }
        }
        players = client.getPlayers();
        currPlayer = client.getNextPlayer();
        bp.repaint();
        Edge tempEdge;
        while (!board.isAllMarked()) {
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {

            }
            currPlayer = client.getNextPlayer();
            bp.repaint();
            if (player.equals(currPlayer)) {
                synchronized (lock) {
                    bp.turn = true;
                    while (!bp.isMoveAvail()) {
                        try {
                            Thread.sleep(300);
                        } catch (InterruptedException e) {
                        }
                    }
                    tempEdge = bp.move;
                    bp.turn = false;
                    bp.move = null;
                }
                client.write(tempEdge);
            }
            tempEdge = client.getNextEdge();
            board.markEdge(tempEdge, currPlayer);
            if (!board.markSquare(currPlayer)) {
                if (player.equals(currPlayer)) {
                    client.write("Next Player");
                }
            }
            bp.repaint();
        }
        running = false;
        client.write("GAME OVER");
        currPlayer = Player.DUMMY;

        bp.repaint();

        client.setReady(false);
    }

}
