package in.singhsaurabh.box;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class BoardPanel extends JPanel implements MouseListener {
    static final int OFFSET = 50;
    static final int GAP = 50;
    final int R = 6;
    Object lock = new Object();
    Game game;
    Edge move;
    boolean turn = false;

    BoardPanel() {
        addMouseListener(this);
    }

    static public Dimension expectSize(int n) {
        Dimension dim = new Dimension();
        dim.setSize(2 * OFFSET + n * GAP, 2 * OFFSET + n * GAP);
        return dim;
    }

    public void setGame(Game game) {
        this.game = game;
    }

    public boolean isMoveAvail() {
        return move != null;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        doDrawing(g);
    }

    private void doDrawing(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        g2d.setColor(game.currPlayer.getColor());
        g2d.drawString(game.currPlayer.getName(), 10, 10);

        g2d.setColor(Color.black);
        for (int i = 0; i < game.n; i++) {
            for (int j = 0; j < game.n; j++) {
                g2d.drawOval(OFFSET + i * GAP - R / 2, OFFSET + j * GAP - R / 2, R, R);
            }
        }

        for (Edge e : game.board.edges) {
            if (e.isMarked()) {
                g2d.setColor(e.getPlayer().getColor());
            } else {
                g2d.setColor(Color.lightGray);
            }
            g2d.drawLine(e.getP1().getI() * GAP + OFFSET, e.getP1().getJ() * GAP + OFFSET, e.getP2().getI() * GAP + OFFSET, e.getP2().getJ() * GAP + OFFSET);
        }

        for (int i = 0; i < game.n - 1; i++) {
            for (int j = 0; j < game.n - 1; j++) {
                if (game.board.squares.get(new Point(i, j)).marked) {
                    g2d.setColor(game.board.squares.get(new Point(i, j)).getPlayer().getColor());
                    g2d.fillOval(OFFSET + i * GAP + GAP / 4, OFFSET + j * GAP + GAP / 4, GAP / 2, GAP / 2);
                }
            }
        }
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        if (turn) {
            double xVal = ((double) e.getX() - OFFSET) / GAP;
            double xFrac = xVal - Math.round(xVal);
            double yVal = ((double) e.getY() - OFFSET) / GAP;
            double yFrac = yVal - Math.round(yVal);
            int x1, x2, y1, y2;
            if (Math.abs(xFrac) < 0.3) {
                x1 = x2 = (int) Math.round(xVal);
                y1 = (int) Math.floor(yVal);
                y2 = (int) Math.ceil(yVal);
                move = new Edge(new Point(x1, y1), new Point(x2, y2));
            } else if (Math.abs(yFrac) < 0.3) {
                y1 = y2 = (int) Math.round(yVal);
                x1 = (int) Math.floor(xVal);
                x2 = (int) Math.ceil(xVal);
                move = new Edge(new Point(x1, y1), new Point(x2, y2));
            }
        }
    }

    @Override
    public void mousePressed(MouseEvent e) {

    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }
}
