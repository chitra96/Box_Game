package in.singhsaurabh.box;

import javax.swing.*;

public class Test3 {
    public static void main(String[] args) {
        int n = 6;
        Player pl = new Player("sa03", "sa03");
        Game g = new Game(pl);
        g.setBoardSize(n);
        JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(600, 600);

        BoardPanel bp = new BoardPanel();
        bp.setGame(g);
        g.bp = bp;

        frame.add(bp);

        new Thread(g).start();
        frame.setVisible(true);
    }
}
