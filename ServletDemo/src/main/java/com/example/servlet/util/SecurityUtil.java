package com.example.servlet.util;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Properties;

public class SecurityUtil {

    private static String SECRET_KEY;
    private static final String ALGORITHM = "AES";
    
    static {
        try (InputStream input = SecurityUtil.class.getClassLoader()
                .getResourceAsStream("application.properties")) {
            
            Properties prop = new Properties();
            if (input == null) {
                throw new RuntimeException("application.properties not found");
            }
            
            prop.load(input);
            SECRET_KEY = prop.getProperty("SECRET_KEY");
            
            if (SECRET_KEY == null || SECRET_KEY.length() != 16) {
                throw new RuntimeException("Secret key must be exactly 16 characters in application.properties");
            }
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String encrypt(String data) throws Exception {
        SecretKeySpec spec = new SecretKeySpec(SECRET_KEY.getBytes(StandardCharsets.UTF_8), ALGORITHM);
        Cipher cipher = Cipher.getInstance(ALGORITHM);
        cipher.init(Cipher.ENCRYPT_MODE, spec);
        
        byte[] encryptedBytes = cipher.doFinal(data.getBytes(StandardCharsets.UTF_8));
        return Base64.getEncoder().encodeToString(encryptedBytes);
    }

    public static String decrypt(String encryptedData) throws Exception {
        SecretKeySpec spec = new SecretKeySpec(SECRET_KEY.getBytes(StandardCharsets.UTF_8), ALGORITHM);
        Cipher cipher = Cipher.getInstance(ALGORITHM);
        cipher.init(Cipher.DECRYPT_MODE, spec);
        
        byte[] decodedBytes = Base64.getDecoder().decode(encryptedData);
        byte[] originalBytes = cipher.doFinal(decodedBytes);
        return new String(originalBytes, StandardCharsets.UTF_8);
    }
}