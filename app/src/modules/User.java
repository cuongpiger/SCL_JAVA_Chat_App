package modules;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class User {
    private String iAccount = null;
    private String iPassword = null;

    public User(String pAccount, String pPassword) {
        iAccount = pAccount;
        iPassword = sha256(pPassword);
    }

    public String getiAccount() {
        return iAccount;
    }

    public String getiPassword() {
        return iPassword;
    }

    private static String sha256(String pPassword) {
        try {
            MessageDigest iSha256 = MessageDigest.getInstance("SHA-256");
            byte[] hash = iSha256.digest(pPassword.getBytes(StandardCharsets.UTF_8));
            return bytesToHex(hash);
        } catch (NoSuchAlgorithmException err) {
            return null;
        }
    }

    private static String bytesToHex(byte[] hash) {
        StringBuilder hexString = new StringBuilder(2 * hash.length);
        for (int i = 0; i < hash.length; i++) {
            String hex = Integer.toHexString(0xff & hash[i]);
            if(hex.length() == 1) {
                hexString.append('0');
            }
            hexString.append(hex);
        }
        return hexString.toString();
    }
}
