package in.singhsaurabh.box;

import java.awt.*;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;
import java.util.List;

/**
 * @author Saurabh Singh
 */
public class Server implements Runnable {
    private static final int PORT = 9002;
    private static final Color[] colors = {Color.red, Color.green, Color.blue, Color.yellow, Color.magenta, Color.cyan};
    private static Map<String, InternalClient> map = new HashMap<>();
    private static boolean running = false;
    Player currPlayer, mainPlayer;
    Game game;
    private List<Player> currPlayers;
    private Iterator<Player> itr;
    private ServerSocket socket;

    public Server() {
        try {
            socket = new ServerSocket(PORT);
        } catch (IOException e) {

        }
    }

    @Override
    public void run() {
        try {
            while (true) {
                new Handler(socket.accept()).start();
            }
        } catch (IOException e) {
        } finally {
            close();
        }
    }

    public void startGame() {
        System.out.println();
        if (running) return;

        currPlayers = new ArrayList<>();
        Collection<InternalClient> coll;
        synchronized (map) {
            coll = map.values();
        }
        for (InternalClient c : coll) {
            if (c.ready) {
                currPlayers.add(c.player);
            }
        }
        running = true;
        itr = currPlayers.iterator();
        nextPlayer();
    }

    public boolean allReady() {
        boolean flag = true;
        Collection<InternalClient> coll;
        synchronized (map) {
            coll = map.values();
        }
        for (InternalClient c : coll) {
//            System.out.println("SERVER: " + c.player + " ready " + c.ready);
            if (!c.ready) {
                flag = false;
                break;
            }
        }
        return flag;
    }

    public synchronized void end() {
        if (running) {
            running = false;
            currPlayers = null;
            itr = null;

        }
    }

    private void nextPlayer() {
        synchronized (itr) {

//            System.out.println("Server: nextPlayer: prevValue: " + currPlayer + " " + itr.hasNext());
            if (itr.hasNext()) {
                currPlayer = itr.next();
            } else {
                itr = currPlayers.iterator();
                currPlayer = itr.next();
            }
//            System.out.println("Server: nextPlayer: newValue: " + currPlayer);
        }
    }

    public void close() {
        try {
            socket.close();
        } catch (IOException e) {

        }
    }

    public void printMsg(Object obj, Player pl) {
        if (pl != null)
            System.out.println("SERVER: " + pl.getName().toUpperCase() + ": " + obj);
    }

    private class Handler extends Thread {
        private InternalClient c;

        public Handler(Socket socket) {
            c = new InternalClient(socket);
        }


        @Override
        public void run() {
            Object obj;
            try {
                c.out = new ObjectOutputStream(c.socket.getOutputStream());
                c.in = new ObjectInputStream(c.socket.getInputStream());
                while (true) {
                    obj = c.in.readObject();
                    print(obj);
                    if (obj instanceof String) {
                        String temp = (String) obj;
                        if (temp.equals("PLAYERS")) {
                            print("asked for players");
                            ArrayList<Player> playerList = new ArrayList<>();
                            Collection<InternalClient> coll;
                            synchronized (map) {
                                coll = map.values();
                            }
                            coll.forEach(c -> playerList.add(c.player));
                            write(playerList);
                        } else if ("NextPlayer".equals(temp)) {
                            write(currPlayer);
                        } else if ("READY".equals(temp)) {
                            synchronized (c) {
                                c.ready = true;
                            }
                            print(" ready " + c.ready);
                        } else if ("NOT READY".equals(temp)) {
                            synchronized (c) {
                                c.ready = false;
                            }
                            print(" ready " + c.ready);
                        } else if ("Main Player".equals(temp)) {
                            print("main Player: " + mainPlayer);
                            write(mainPlayer);
                        } else if ("START".equals(temp)) {
                            if (c.player.equals(mainPlayer)) {
                                startGame();
                            }
                        } else if ("IS GAME STARTED".equals(temp)) {
                            write(running);
                        } else if ("ALL READY".equals(temp)) {
                            print("allReady " + allReady());
                            writeToPlayer(allReady(), mainPlayer);
                        } else if ("Next Player".equals(temp)) {
                            nextPlayer();
                        }
                    } else if (obj instanceof Player) {
                        Player tempPl = (Player) obj;
                        c.player = tempPl;
                        synchronized (map) {
                            if (map.entrySet().size() <= 6) {
                                c.player.setColor(colors[map.size()]);
                                map.put(tempPl.getName(), c);
                            }
                        }
                        print("player data " + tempPl);
                    } else if (obj instanceof Edge) {
                        Edge currEdge = (Edge) obj;
                        writeToAll(currEdge);
                    }
                }


            } catch (Exception e) {

            } finally {

            }
        }

        public void write(Object obj) {
            try {
                c.out.writeObject(obj);
                c.out.flush();
            } catch (IOException e) {
            }
        }

        public synchronized void writeToPlayer(Object obj, Player pl) {
            try {
                map.get(pl.getName()).out.writeObject(obj);
                map.get(pl.getName()).out.flush();
            } catch (IOException e) {
            }

        }

        public void writeToAll(Object obj) {
            Collection<InternalClient> coll;
            synchronized (map) {
                coll = map.values();
            }
            coll.forEach(c -> {
                try {
                    c.out.writeObject(obj);
                    c.out.flush();
                } catch (IOException e) {
                }
            });
        }

        public void print(Object obj) {
            //printMsg(obj, c.player);
        }
    }


    private class InternalClient {
        ObjectInputStream in;
        ObjectOutputStream out;
        Player player;
        Socket socket;
        boolean ready = false;

        public InternalClient(Socket socket) {
            this.socket = socket;
        }

        void close() {
            try {
                in.close();
            } catch (IOException e) {
            }
            try {
                out.close();
            } catch (IOException e) {
            }
            try {
                socket.close();
            } catch (IOException e) {
            }
        }
    }
}
