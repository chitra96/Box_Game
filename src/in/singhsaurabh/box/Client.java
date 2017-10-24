package in.singhsaurabh.box;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.util.ArrayList;

public class Client {
    private static final Object lock = new Object();
    private final int PORT = 9002;

    ObjectInputStream in;
    ObjectOutputStream out;
    Socket socket;
    boolean alive = true;
    ArrayList<Player> players;

    String host;

    public Client(String host) {
        this.host = host;
        try {
            socket = new Socket(InetAddress.getByName(host), PORT);
            in = new ObjectInputStream(socket.getInputStream());
            out = new ObjectOutputStream(socket.getOutputStream());
        } catch (IOException e) {
            System.out.println("exception 2");
        }
    }

    public synchronized void startGame() {
        write("START");
    }

    private synchronized void readPlayerList() {
        Object obj = null;
        try {
            obj = in.readObject();
            if (obj instanceof ArrayList) {
                players = (ArrayList<Player>) obj;
            }
        } catch (IOException e) {
            System.out.println("exception 3");
        } catch (ClassNotFoundException e) {
            System.out.println("exception 4");
        }
    }

    public synchronized Player readNextPlayer() {
        Object obj = null;
        Player pl = null;
        try {
            obj = in.readObject();
            if (obj instanceof Player) {
                pl = (Player) obj;
            }
        } catch (IOException e) {
            System.out.println(e.getMessage());
        } catch (ClassNotFoundException e) {
            System.out.println("exception 6");
        }
        return pl;
    }

    public synchronized Edge readNextEdge() {
        Object obj = null;
        Edge ed = null;
        try {
            obj = in.readObject();
            if (obj instanceof Edge) {
                ed = (Edge) obj;
            }
        } catch (IOException e) {
            System.out.println(e.getMessage());
        } catch (ClassNotFoundException e) {
            System.out.println("exception 8");
        }
        return ed;
    }

    public synchronized Edge getNextEdge() {
        Edge ed = null;
        ed = readNextEdge();
        while (ed == null) {
            ed = readNextEdge();
        }
        return ed;
    }

    public synchronized Player getNextPlayer() {
        Player pl = null;
        write("NextPlayer");
        pl = readNextPlayer();
        while (pl == null) {
            pl = readNextPlayer();
        }

        return pl;
    }

    public synchronized ArrayList<Player> getPlayers() {
        players = null;
        do {
            write("PLAYERS");
            readPlayerList();
        } while (players == null);

        return players;
    }

    private synchronized int readSize() {
        Object obj = null;
        int size = -1;
        try {
            obj = in.readObject();
            if (obj instanceof Integer) {
                size = (Integer) obj;
            }
        } catch (IOException e) {
            System.out.println("exception 9");
        } catch (ClassNotFoundException e) {
            System.out.println("exception 10");
        }
        return size;
    }

    public synchronized int getSize() {
        int size = -1;

        write("SQUARE SIZE");
        size = readSize();
        while (size == -1) {
            size = readSize();
        }
        return size;
    }

    public synchronized boolean gameStarted() {
        write("IS GAME STARTED");
        Object obj = null;
        boolean temp = false;
        try {
            obj = in.readObject();
            if (obj instanceof Boolean) {
                temp = (Boolean) obj;
            }
        } catch (IOException e) {
            System.out.println("exception 1");
        } catch (ClassNotFoundException e) {
            System.out.println("exception 1");
        }
        return temp;
    }

    public synchronized void setReady(boolean ready) {
        if (ready) {
            write("READY");
        } else {
            write("NOT READY");
        }

    }

    public synchronized boolean isEveryOneReady() {
        write("ALL READY");
        Object obj = null;
        boolean temp = false;
        try {
            obj = in.readObject();
            if (obj instanceof Boolean) {
                temp = (Boolean) obj;
            }
        } catch (IOException e) {
            System.out.println("exception isEveryOneReady 1");
        } catch (ClassNotFoundException e) {
            System.out.println("exception isEveryOneReady 1");
        }
        return temp;
    }

    public synchronized void write(Object obj) {
        try {
            out.writeObject(obj);
            out.flush();
        } catch (IOException e) {
        }
    }

    public void close() {
        try {
            in.close();
        } catch (IOException e) {
        }
        try {
            out.flush();
            out.close();
        } catch (IOException e) {
        }
        try {
            socket.close();
        } catch (IOException e) {
        }
    }
}
