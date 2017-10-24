package in.singhsaurabh.box;

import java.awt.*;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;
import java.util.List;

/**
 * @author Saurabh Singh
 */
public class Server implements Runnable {
    private static final int PORT = 9002, MAX_PLAYERS = 6;
    private static final Color[] colors = {Color.red, Color.blue, Color.magenta, Color.cyan, new Color(200, 0, 150), Color.yellow};
    private static final Map<String, InternalClient> map = new HashMap<>();
    private static boolean running = false;
    Player currPlayer, mainPlayer;
    private static List<Player> currPlayers;
    private static Iterator<Player> itr;
    private ServerSocket socket;
    private static int size = 10;

    public Server() {
        try {
            socket = new ServerSocket(PORT, 50, InetAddress.getLocalHost());
        } catch (IOException e) {

        }
    }

    public Server(String host) {
        try {
            socket = new ServerSocket(PORT, 50, InetAddress.getByName(host));
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
        if (running) return;

        currPlayers = new ArrayList<>();
        Collection<InternalClient> coll;
        synchronized (map) {
            coll = map.values();
        }
        for (InternalClient c : coll) {
            if (c.player.isReady()) {
                currPlayers.add(c.player);
            }
        }
        running = true;
        itr = currPlayers.iterator();
        nextPlayer();
    }

    public void getPlayers(ArrayList<Player> playerList) {
        Collection<InternalClient> coll;
        synchronized (map) {
            coll = map.values();
        }
        for (InternalClient c : coll) {
            if (c.player.isReady()) {
                playerList.add(c.player);
            } else {
                //work around As it was causing some issue once a player is added  without being ready
                //don't know why but this works
                playerList.add(new Player(c.player.getName(), c.player.getId()));
            }
        }
    }

    public boolean allReady() {
        boolean flag = true;
        Collection<InternalClient> coll;
        synchronized (map) {
            coll = map.values();
        }
        for (InternalClient c : coll) {
            if (!c.player.isReady()) {
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
            if (itr.hasNext()) {
                currPlayer = itr.next();
            } else {
                itr = currPlayers.iterator();
                currPlayer = itr.next();
            }
        }
    }

    public void close() {
        try {
            socket.close();
        } catch (IOException e) {
        }
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
                    if (obj instanceof String) {
                        String temp = (String) obj;
                        if (temp.equals("PLAYERS")) {
                            ArrayList<Player> playerList = new ArrayList<>();
                            getPlayers(playerList);
                            writeToPlayer(playerList, c.player);
                        } else if ("NextPlayer".equals(temp)) {
                            write(currPlayer);
                        } else if ("READY".equals(temp)) {
                            synchronized (map) {
                                map.get(c.player.getId()).player.setReady(true);
                            }
                        } else if ("NOT READY".equals(temp)) {
                            synchronized (map) {
                                map.get(c.player.getId()).player.setReady(false);
                            }
                        } else if ("Main Player".equals(temp)) {
                            write(mainPlayer);
                        } else if ("START".equals(temp)) {
                            synchronized (map) {
                                map.get(c.player.getId()).player.setReady(true);
                            }
                            startGame();
                        } else if ("IS GAME STARTED".equals(temp)) {
                            write(running);
                        } else if ("ALL READY".equals(temp)) {
                            write(allReady());
                        } else if ("Next Player".equals(temp)) {
                            nextPlayer();
                        } else if ("GAME OVER".equals(temp)) {
                            end();
                        } else if ("SQUARE SIZE".equals(temp)) {
                            int tempSize;
                            synchronized (this) {
                                tempSize = size;
                            }
                            write(tempSize);
                        } else if ("NAME".equals(temp)) {
                            synchronized (map) {
                                map.get(c.player.getId()).player.setName(c.player.getName() + 1);
                            }
                        }
                    } else if (obj instanceof Player) {
                        Player tempPl = (Player) obj;
                        c.player = tempPl;
                        synchronized (map) {
                            if (map.entrySet().size() < MAX_PLAYERS) {
                                c.player.setColor(colors[map.size()]);
                                map.put(tempPl.getId(), c);
                            }
                        }
                    } else if (obj instanceof Edge) {
                        Edge currEdge = (Edge) obj;
                        writeToAll(currEdge);
                    }
                }


            } catch (Exception e) {

            } finally {
                synchronized (map) {
                    if (c.player != null && map.containsKey(c.player.getId()))
                        map.remove(c.player.getId());
                }
                c.close();
                c = null;
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
                map.get(pl.getId()).out.writeObject(obj);
                map.get(pl.getId()).out.flush();
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
    }


    private class InternalClient {
        ObjectInputStream in;
        ObjectOutputStream out;
        Player player;
        Socket socket;

        public InternalClient(Socket socket) {
            this.socket = socket;
        }

        void close() {
            try {
                if (in != null) {
                    in.close();
                }
            } catch (IOException e) {
            }
            try {
                if (out != null) {
                    out.flush();
                    out.close();
                }
            } catch (IOException e) {
            }
            try {
                if (socket != null) {
                    socket.close();
                }
            } catch (IOException e) {
            }
        }
    }
}
