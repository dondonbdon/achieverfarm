package dev.bti.achieverfarm.common;

import dev.bti.achieverfarm.exceptions.auth.AuthException;
import dev.bti.achieverfarm.exceptions.auth.InvalidTokenException;
import dev.bti.achieverfarm.logging.auth.Type;
import dev.bti.achieverfarm.logging.auth.UserLog;
import dev.bti.achieverfarm.logging.enums.Level;
import dev.bti.achieverfarm.logging.inventory.InventoryLog;
import dev.bti.achieverfarm.logging.inventory.Operation;
import dev.bti.achieverfarm.logging.inventory.UpdateType;
import dev.bti.achieverfarm.logging.models.BasicLog;
import dev.bti.achieverfarm.logging.service.LogService;
import dev.bti.achieverfarm.models.Inventory;
import dev.bti.achieverfarm.models.Item;
import dev.bti.achieverfarm.models.Stock;
import dev.bti.achieverfarm.scheduled.JwtBlacklistManager;
import dev.bti.achieverfarm.services.item.ItemService;
import dev.bti.achieverfarm.util.LogFactory;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.SneakyThrows;
import org.springframework.security.core.userdetails.UserDetails;

import javax.crypto.SecretKey;
import java.security.SecureRandom;
import java.util.Arrays;
import java.util.Date;

public class Common {


    public static String generateRandomId() {
        final String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        final int LENGTH = 16;
        final SecureRandom RANDOM = new SecureRandom();

        StringBuilder uid = new StringBuilder(LENGTH);
        for (int i = 0; i < LENGTH; i++) {
            int index = RANDOM.nextInt(CHARACTERS.length());
            uid.append(CHARACTERS.charAt(index));
        }
        return uid.toString();
    }

    public static class Jwt {

        public static String extractUsername(String token) {
            return extractClaim(token, Claims::getSubject);
        }

        public static Date extractExpiration(String token) {
            return extractClaim(token, Claims::getExpiration);
        }

        private static <T> T extractClaim(String token, ClaimsResolver<T> claimsResolver) {
            final Claims claims = extractAllClaims(token);
            return claimsResolver.resolve(claims);
        }

        public static Claims extractAllClaims(String token) {
            return Jwts.parser()
                    .verifyWith(getSignInKey())
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();
        }

        public static boolean isTokenExpired(String token) {
            return extractExpiration(token).before(new Date());
        }

        public static boolean validToken(String token, String username) {
            return (username.equals(extractUsername(token)) && !isTokenExpired(token));
        }

        public static String createToken(String username, String... roles) {
            return Jwts.builder()
                    .subject(username)
                    .claim("roles", roles)
                    .issuedAt(new Date())
                    .expiration(new Date(System.currentTimeMillis() + 1000L * 60 * 60 * 24 * 30))
                    .signWith(getSignInKey(), Jwts.SIG.HS256)
                    .compact();
        }

        public static void invalidate(JwtBlacklistManager manager, String... tokens) {
            Arrays.stream(tokens).toList().forEach(manager::blacklistToken);
        }

        @FunctionalInterface
        public interface ClaimsResolver<T> {
            T resolve(Claims claims);
        }

        private static SecretKey getSignInKey() {
            String secretKey = "c96fLhcBF+OzWzAaR1fVizGl8x/fbTehLjShJ+gmHdE=";
            byte[] keyBytes = Decoders.BASE64.decode(secretKey);
            return Keys.hmacShaKeyFor(keyBytes);
        }
    }

    public static class Log {

        public static void auth(LogService logService, String note, Level level, String email, Type type) {
            UserLog userLog = LogFactory.get(UserLog.class, new LogFactory.UserPayload(note, level, email, type));
            logService.push(userLog);
        }

        public static void basic(LogService logService, String note) {
            BasicLog basicLog = LogFactory.get(BasicLog.class, new LogFactory.BasicPayload(note, null));
            logService.push(basicLog);
        }

        @SneakyThrows
        public static void inventory(LogService logService, ItemService itemService, Stock stock, Inventory inventory, UpdateType type, Level level, Operation operation, String note) {
            Item item = itemService.getItem(stock.getId());
            InventoryLog inventoryLog = LogFactory.get(InventoryLog.class, new LogFactory.InventoryPayload(note, level, item, operation,
                    type, stock, inventory));
            logService.push(inventoryLog);
        }
    }

    public static class Auth {

        public static void validate(String userId, UserDetails userDetails) throws AuthException {
            if (!userDetails.getUsername().equals(userId)) {
                throw new InvalidTokenException();
            }
        }
    }
}
