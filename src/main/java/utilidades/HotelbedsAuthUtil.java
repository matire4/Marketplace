package utilidades;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;

public class HotelbedsAuthUtil {

    public static String generateSignature(String apiKey, String secret, long timestamp) {
        try {
            // Clean the inputs
            apiKey = apiKey.trim();
            secret = secret.trim();
            
            // Create the string to hash (apiKey + secret + timestamp)
            String stringToHash = apiKey + secret + timestamp;
            System.out.println("String to hash: " + stringToHash);
            
            // Use SHA-256 (not HMAC-SHA256)
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(stringToHash.getBytes(StandardCharsets.UTF_8));
            
            // Convert to hex string
            StringBuilder hexString = new StringBuilder();
            for (byte b : hash) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }
            
            String signature = hexString.toString();
            System.out.println("Generated hex signature: " + signature);
            
            return signature;
        } catch (Exception e) {
            throw new RuntimeException("Error generating Hotelbeds signature", e);
        }
    }
}