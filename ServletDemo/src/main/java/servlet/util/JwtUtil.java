package servlet.util;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;
import java.util.Properties;
public class JwtUtil {
    private static final Logger log = LoggerFactory.getLogger(JwtUtil.class);
    private static Key secret;
    private static long expiry;

    static {
        try (InputStream input = JwtUtil.class.getClassLoader().getResourceAsStream("application.properties")) {
            Properties prop = new Properties();
            if (input == null) {
                log.error("unable to find application.properties");
            } else {
                prop.load(input);
                String secretString = prop.getProperty("jwt.secret");
                secret = Keys.hmacShaKeyFor(secretString.getBytes(StandardCharsets.UTF_8));
                expiry = Long.parseLong(prop.getProperty("jwt.expiry"));
            }
        } catch (Exception e) {
            log.error("Failed to load properties", e);
        }
    }
    public static String generateToken(String username) {
        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + expiry))
                .signWith(secret, SignatureAlgorithm.HS256)
                .compact();
    }
    public static boolean isTokenValid(String token) {
        try {
            Jwts.parserBuilder()
                .setSigningKey(secret)
                .build()
                .parseClaimsJws(token);
            return true;
        } catch (Exception e) {
            log.info("JWT is invalid ", e.getMessage());
            return false;
        }
    }
}