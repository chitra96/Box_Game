package in.singhsaurabh.box;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Collections;
import java.util.Enumeration;
import java.util.Random;

public class Util {
    static final String alphabetSet = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";

    /**
     * Return a random String of length n from a-z, A-Z and 0-9
     *
     * @param n
     * @return
     */
    public static String randomString(int n) {
        StringBuffer buf = new StringBuffer("");
        Random rnd = new Random();
        for (int i = 0; i < n; i++) {
            buf.append(alphabetSet.charAt(rnd.nextInt(alphabetSet.length())));
        }
        return buf.toString();
    }

    /**
     * This method return the IP address of System in local network starting with the given String
     * else return the localHost
     *
     * @param starting
     * @return
     * @throws SocketException
     * @throws UnknownHostException
     */
    public static InetAddress getLocalIP(String starting) throws SocketException, UnknownHostException {
        Enumeration<NetworkInterface> nets = NetworkInterface.getNetworkInterfaces();
        for (NetworkInterface netint : Collections.list(nets)) {
            Enumeration<InetAddress> em = netint.getInetAddresses();
            while (em.hasMoreElements()) {
                InetAddress temp = em.nextElement();
                if (temp.getHostAddress().startsWith(starting)) {
                    return temp;
                }
            }
        }
        return InetAddress.getLocalHost();
    }
}
