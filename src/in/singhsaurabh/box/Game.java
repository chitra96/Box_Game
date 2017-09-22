package in.singhsaurabh.box;

import java.util.ArrayList;
import java.util.Scanner;

/**
 * @author Saurabh Singh
 */
public class Game implements Runnable {
    ArrayList<Player> players;
    Player player, mainPlayer, currPlayer;
    boolean started = false;
    Board board;
    Client client;
    int n;
    BoardPanel bp;
    private Object lock = new Object();

    public Game(Player pl) {
        players = new ArrayList<>();
        player = pl;
        client = new Client("127.0.0.1");
        currPlayer = Player.DUMMY;
        client.write(pl);
    }

    public void setBoardSize(int n) {
        this.n = n < 2 ? 2 : n;
        board = new Board(this.n);
    }

    @Override
    public void run() {
        Scanner in = new Scanner(System.in);
        //will call and get set of players
        mainPlayer = client.getMainPlayer();
        System.out.println("Main Player: " + mainPlayer);
        client.setReady(true);
        if (player.equals(mainPlayer)) {
            do {
                if (client.isEveryOneReady()) {
                    System.out.println("EveryOne ready, Wish to Continue? Type Y");
                } else {
                    System.out.println("Everyone is not ready, Wish to continue? Type Y");
                }
            } while (!"y".equalsIgnoreCase(in.next()));
            client.write("START");
        } else {
            System.out.println("Waiting for main player to start game");
        }
        players = client.getPlayers();

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
        System.out.println(board);
        System.out.println(board.squaresToString());

        client.setReady(false);

        players.forEach(p -> System.out.println(p.toString() + " Score: " + p.getScore()));
    }

}
