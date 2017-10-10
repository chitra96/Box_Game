package in.singhsaurabh.box;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.List;

public class Main extends JFrame implements MouseListener {
    BoardPanel boardPanel = new BoardPanel();
    JButton host = new JButton("Host");
    JButton join = new JButton("Join");
    JButton start = new JButton("Start");
    JButton ready = new JButton("Ready");
    JPanel panel = new JPanel(), statusPanel = new JPanel();
    JLabel statusLabel = new JLabel();
    JTextArea mainText = new JTextArea();
    Server server;
    Player player;
    Game game;
    boolean flagStarted = false, startedGame = false;

    private Main() {
        init();
        createGUI();
    }

    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Main().setVisible(true);
            }
        });
    }

    private void init() {
        String name = JOptionPane.showInputDialog(this, "Enter your name");

        player = new Player(name, Util.randomString(12));
        host.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String ip = JOptionPane.showInputDialog("Enter ip address to start server", "127.0.0.1");
                try {
                    if (ip != null && ip.length() > 10) {
                        server = new Server(ip);
                        server.mainPlayer = player;
                        new Thread(server).start();
                        game = new Game(player, ip);
                    } else {
                        server = new Server();
                        server.mainPlayer = player;
                        new Thread(server).start();
                        game = new Game(player);
                    }


                    join.setVisible(false);
                    host.setVisible(false);
                    start.setVisible(true);
                    startedGame = true;
                    player.setReady(true);
                    game.client.setReady(true);
                    updatePlayerLists();
                } catch (Exception ex) {
                    // Something is not right, Please try again
                }
            }
        });
        join.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String ans = JOptionPane.showInputDialog("Enter the address to connect", "127.0.0.1");
                try {
                    if (ans != null && ans.length() > 10) {
                        game = new Game(player, ans);
                    } else {
                        game = new Game(player);
                    }
                    join.setVisible(false);
                    host.setVisible(false);
                    ready.setVisible(true);
                    updatePlayerLists();
                } catch (Exception ex) {
                    // Something is not right, Please try again
                }
            }
        });
        ready.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                game.client.setReady(true);
                ready.setVisible(false);
                statusLabel.setText("Waiting for game start");
                repaint();
            }
        });
        start.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                start.setVisible(false);
                game.client.startGame();
                flagStarted = true;
            }
        });


    }

    private void updatePlayerLists() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                StringBuilder sb = new StringBuilder("");
                while (!game.client.gameStarted() && !flagStarted) {
                    sb.delete(0, sb.length());
                    List<Player> tempList = game.client.getPlayers();
                    sb.append("Currently connected player\n");
                    for (Player pl : tempList) {
                        sb.append(pl.toString() + " " + pl.isReady() + "\n");
                    }
                    mainText.setText(sb.toString());
                    try {
                        Thread.sleep(2000);
                    } catch (InterruptedException e) {

                    }
                    repaint();
                }
                preGameStart();
            }
        }).start();
    }

    private void preGameStart() {
        flagStarted = true;
        game.setBoardSize();
        game.bp = boardPanel;
        boardPanel.game = game;
        statusLabel.setText("");
        new Thread(game).start();
        panel.remove(mainText);
        panel.add(boardPanel);
        panel.setMinimumSize(boardPanel.getMinimumSize());
        panel.revalidate();
        panel.repaint();
        repaint();
    }

    private void createGUI() {
        setLayout(new BoxLayout(getContentPane(), BoxLayout.PAGE_AXIS));
        add(new JLabel(player.getName()));
        mainText.setText("Welcome to Dots and Squares");
        panel.setLayout(new BoxLayout(panel, BoxLayout.PAGE_AXIS));
        panel.add(mainText);
        add(panel);
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.LINE_AXIS));
        buttonPanel.add(host);
        buttonPanel.add(join);
        buttonPanel.add(start);
        buttonPanel.add(ready);
        start.setVisible(false);
        ready.setVisible(false);
        add(buttonPanel);

        statusPanel.setLayout(new BoxLayout(statusPanel, BoxLayout.LINE_AXIS));
        statusPanel.add(statusLabel);
        add(statusPanel);

        setTitle(player.getName() + " Dots and Square");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setMinimumSize(new Dimension(BoardPanel.expectSize(5).width, BoardPanel.expectSize(5).height));
        pack();
    }

    @Override
    public void mouseClicked(MouseEvent e) {

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
