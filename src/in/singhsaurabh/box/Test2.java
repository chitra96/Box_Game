package in.singhsaurabh.box;

import javax.swing.*;
import java.net.*;

public class Test2 {
    public static void main(String[] args) throws UnknownHostException, SocketException, InterruptedException {
        String[] options = new String[]{"Scan For Host", "Enter IP Address of Host"};
        int response = JOptionPane.showOptionDialog(null, "What would you like to do?", "Title",
                JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE,
                null, options, options[0]);
        System.out.println(response);
        InetAddress addr = Util.getLocalIP("192");
        String baseIp = addr.getHostAddress().substring(0, addr.getHostAddress().lastIndexOf("."));

        for (int i = 0; i < 256; i++) {
            Thread t = new Thread(new IpScanner(baseIp + "." + i));
            t.start();
            t.join();
        }
    }

    static class IpScanner implements Runnable {
        String ip;

        public IpScanner(String ip) {
            this.ip = ip;
        }

        @Override
        public void run() {
            try {
                Socket client = new Socket();
                client.connect(new InetSocketAddress(ip, 9002), 1000);
            } catch (Exception e) {
            } finally {
            }
        }
    }
}
