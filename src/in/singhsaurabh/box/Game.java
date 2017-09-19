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
        board = new Board(this.n);
        player = pl;
        client = new Client("127.0.0.1");
        currPlayer = Player.DUMMY;
//        System.out.println("client created");
        // add player
        client.write(pl);
//        System.out.println("game creation successful");
    }

    public void setBoardSize(int n) {
        this.n = n < 2 ? 2 : n;
    }

    @Override
    public void run() {
        Scanner in = new Scanner(System.in);
        //will call and get set of players
//        System.out.println("game run called");
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
//        System.out.println(players.size());

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
                Thread.sleep(1000);
            } catch (InterruptedException e) {

            }
//            System.out.println(board);
            currPlayer = client.getNextPlayer();
            if (player.equals(currPlayer)) {
//                System.out.println(currPlayer + " enter your move");
//                while (true) {
//                    tempEdge = new Edge(new Point(in.nextInt(), in.nextInt()), new Point(in.nextInt(), in.nextInt()));
//                    if (board.canBeMarked(tempEdge)) {
//                        break;
//                    } else {
//                        System.out.println("Could not be marked please try again");
//                    }
//                }
                synchronized (lock) {
                    bp.turn = true;
//                    System.out.println("going to wait");
                    while (!bp.isMoveAvail()) {
                        try {
                            Thread.sleep(300);
                        } catch (InterruptedException e) {
                        }
                    }
//                    System.out.println("wait over");
                    tempEdge = bp.move;
//                    System.out.println(tempEdge);
                    bp.turn = false;
                    bp.move = null;
                }
                client.write(tempEdge);
            }
//            System.out.println("Waiting for players move");
            tempEdge = client.getNextEdge();
//            System.out.println(currPlayer + " move is " + tempEdge);
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
