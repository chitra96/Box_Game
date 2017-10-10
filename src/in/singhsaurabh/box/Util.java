package in.singhsaurabh.box;

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
}
