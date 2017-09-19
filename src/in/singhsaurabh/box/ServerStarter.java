package in.singhsaurabh.box;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ServerStarter {
    private static final int PORT = 9002;

    static ObjectInputStream in;
    static ObjectOutputStream out;
    static Socket socket;

    public static void main(String[] args) {
        ExecutorService es = Executors.newCachedThreadPool();
        es.execute(new Server());
        try {
            socket = new Socket(InetAddress.getByName("127.0.0.1"), PORT);
            in = new ObjectInputStream(socket.getInputStream());
            out = new ObjectOutputStream(socket.getOutputStream());

            out.writeObject("hello");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
