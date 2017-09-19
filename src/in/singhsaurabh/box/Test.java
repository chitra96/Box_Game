package in.singhsaurabh.box;

import javax.swing.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author Saurabh Singh
 */
public class Test {
    public static void main(String[] args) {
        ExecutorService es = Executors.newCachedThreadPool();
        System.out.println("Do you want to start server? \n 1: start \n 2: continue");
        boolean flag = true;//in.nextInt();
        int n = 5;
        Server server = null;
        if (flag) {
            server = new Server();
            es.execute(server);
        }
        System.out.println("Enter name and id of player");
        Player pl = new Player("sa00", "sa00");
        Game g = new Game(pl);
        g.setBoardSize(n);
        if (flag) {
            server.mainPlayer = pl;
            server.game = g;
        }
        JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(BoardPanel.expectSize(n).width, BoardPanel.expectSize(n).height);

        BoardPanel bp = new BoardPanel();
        bp.setGame(g);
        g.bp = bp;

        frame.add(bp);

        new Thread(g).start();
        frame.setVisible(true);
    }

}
