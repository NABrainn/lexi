package Helpers;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Hashing {
    public static String convertToMd5(String value) {

        if(value == null) {
            throw new NullPointerException("value is null");
        }

        try {
            var md = MessageDigest.getInstance("MD5");
            var bytes = value.getBytes();
            md.update(bytes);
            var digest = md.digest();
            return new String(digest);

        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("no such algorithm");
        }
    }
}
