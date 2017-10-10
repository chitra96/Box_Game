package in.singhsaurabh.box;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.util.ArrayList;

public class Client {
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
            System.out.println("client trying connecting to server");
            socket = new Socket(InetAddress.getByName(host), PORT);
            in = new ObjectInputStream(socket.getInputStream());
            out = new ObjectOutputStream(socket.getOutputStream());
            System.out.println("client successfully connected to server");
        } catch (IOException e) {
            System.out.println("exception 2");
        }
    }

    public void startGame() {
        write("START");
    }

    private void readPlayerList() {
//        System.out.println("client readPlayerList called");
        Object obj = null;
        try {
            obj = in.readObject();
//            System.out.println("client: " + obj);
            if (obj instanceof ArrayList) {
                players = (ArrayList<Player>) obj;
                //players.forEach(player -> System.out.println("CLIENT: "+player+" "+player.isReady()));
            }
        } catch (IOException e) {
            System.out.println("exception 3");
            e.printStackTrace();
            System.exit(1);
        } catch (ClassNotFoundException e) {
            System.out.println("exception 4");
        }
    }

    public Player readNextPlayer() {
//        System.out.println("client: readNextPlayer called");
        Object obj = null;
        Player pl = null;
        try {
            obj = in.readObject();
//            System.out.println("client: " + obj);
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

    public Edge readNextEdge() {
//        System.out.println("client: readNextEdge called");
        Object obj = null;
        Edge ed = null;
        try {
            obj = in.readObject();
//            System.out.println("client: " + obj);
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

    public Edge getNextEdge() {
        Edge ed = null;
        ed = readNextEdge();
        while (ed == null) {
            ed = readNextEdge();
        }
        return ed;
    }

    public Player getNextPlayer() {
        Player pl = null;
        write("NextPlayer");
        pl = readNextPlayer();
        while (pl == null) {
            pl = readNextPlayer();
        }

        return pl;
    }

    public ArrayList<Player> getPlayers() {
        players = null;
        do {
            write("PLAYERS");
            readPlayerList();
        } while (players == null);

        return players;
    }

    private int readSize() {
//        System.out.println("client: readMainPlayer called");
        Object obj = null;
        int size = -1;
        try {
            obj = in.readObject();
//            System.out.println("client: " + obj);
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

    public boolean gameStarted() {
//        System.out.println("client: gameStarted called");
        write("IS GAME STARTED");
        Object obj = null;
        boolean temp = false;
        try {
            obj = in.readObject();
//            System.out.println("client: " + obj);
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

    public void setReady(boolean ready) {
        if (ready) {
            write("READY");
        } else {
            write("NOT READY");
        }

    }

    public boolean isEveryOneReady() {
//        System.out.println("client: isEveryOneReady called");
        write("ALL READY");
        Object obj = null;
        boolean temp = false;
        try {
            obj = in.readObject();
//            System.out.println("client: " + obj);
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

    public void write(Object obj) {
//        System.out.println("client: going to write- " + obj);
        try {
            out.writeObject(obj);
            out.flush();
//            System.out.println(obj + " written");
        } catch (IOException e) {
        }
    }

    public void close() {
        try {
            out.flush();
            out.close();
            in.close();
            socket.close();
        } catch (Exception e) {

        }
    }
}
