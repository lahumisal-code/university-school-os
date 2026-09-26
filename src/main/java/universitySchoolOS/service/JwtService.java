package universitySchoolOS.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import universitySchoolOS.AppConstatnt.AppConstant;
import universitySchoolOS.model.UserRolePermissions;

import javax.crypto.SecretKey;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;

@Slf4j
@Service
public class JwtService {

    @Value("${jwt.secret}")
    private String secretKey;

    // Store blacklisted tokens
    private final Set<String> blacklistedTokens = ConcurrentHashMap.newKeySet();

    public String generateToken(String username) {
        // This method generate a token
        Map<String,Object> claims = new HashMap<>();

        return Jwts.builder()
                .claims()
                .add(claims)
                .subject(username)
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + 1000 * 60 * 10))     // this token will be applicable for 10 min
                .and()
                .signWith(getKey())
                .compact();
    }

    public SecretKey getKey() {
        // bellow line convert your secret key string to byte
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);

    }

    // bellow get username from token
    public String extractUserNameFromToken(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    private <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims= extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parser()
                .verifyWith(getKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();

    }

    public boolean validateToken(String token, UserDetails userDetails) {
        log.info("initialize JWT validateToken");
        final String username= extractUserNameFromToken(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }

    private boolean isTokenExpired(String token) {
        log.info("validating token expired or not....");
        return extractExpiration(token).before(new Date());
    }

    private Date extractExpiration(String token) {
        return extractClaim(token, Claims:: getExpiration);
    }
    
    // Add token to blacklist
    public void blacklistToken(String token) {
        blacklistedTokens.add(token);
    }
    
    // Check if token is blacklisted
    public boolean isTokenBlacklisted(String token) {
        return blacklistedTokens.contains(token);
    }
    
    // Clean up expired tokens from blacklist (optional - for memory management)
    public void removeExpiredTokensFromBlacklist() {
        blacklistedTokens.removeIf(token -> {
            try {
                return isTokenExpired(token);
            } catch (Exception e) {
                return true; // Remove invalid tokens
            }
        });
    }

    public boolean logout(HttpServletRequest request) {

        String authHeader = request.getHeader("Authorization");

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            log.info("Authorization header not found during logout");
            return false;
        }

        String token = authHeader.substring(7);

        if (isTokenBlacklisted(token)) {
            log.info("Token already blacklisted");
            return false;
        }

        if (isTokenExpired(token)) {
            log.info("Token already expired");
            return false;
        }

        blacklistToken(token);

        log.info("JWT token blacklisted successfully");

        return true;
    }
}
