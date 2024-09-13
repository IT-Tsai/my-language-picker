package io.practice.programming_languages_picker.util;

import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.Calendar;

import static io.practice.programming_languages_picker.constants.SecurityConstants.ROLE_KEY;

@Component
public class JwtUtil {

    private static final String ISS = "IT";

    @Value("${jwt.accessExpirationMs}")
    private String accessTokenExpirationMs;

    @Value("${jwt.refreshExpirationMs}")
    private String refreshTokenExpirationMs;

    public String generateAccessToken(Authentication authentication) {
        UserDetails user = (UserDetails) authentication.getPrincipal();
        Calendar exp = Calendar.getInstance();
        exp.add(Calendar.HOUR, Integer.parseInt(accessTokenExpirationMs));

        return Jwts.builder()
                .subject(user.getUsername())
                .claim(ROLE_KEY, user.getAuthorities())
                .issuedAt(exp.getTime())
                .issuer(ISS)
                .expiration(exp.getTime())
                .signWith(getSignInKey(), Jwts.SIG.HS256)
                .compact();
    }

    public String generateRefreshToken(Authentication authentication) {
        UserDetails user = (UserDetails) authentication.getPrincipal();
        Calendar exp = Calendar.getInstance();
        exp.add(Calendar.HOUR, Integer.parseInt(refreshTokenExpirationMs));

        return Jwts.builder()
                .subject(user.getUsername())
                .claim(ROLE_KEY, user.getAuthorities())
                .issuedAt(exp.getTime())
                .issuer(ISS)
                .expiration(exp.getTime())
                .signWith(getSignInKey(), Jwts.SIG.HS256)
                .compact();
    }

    public OffsetDateTime getExpirationDateFromToken(String token) {
        return parseToken(token).getExpiration().toInstant().atOffset(ZoneOffset.UTC);
    }

    public String getEmailFromToken(String token){
        return parseToken(token).getSubject();
    }

    public Claims parseToken(String token) {
        try {
            return Jwts.parser().verifyWith(getSignInKey()).build().parseSignedClaims(token).getPayload();
        } catch (JwtException e) {
            throw new BadCredentialsException(e.getMessage(), e);
        }
    }

    public boolean validateToken(String token, UserDetails userDetails) {
        Claims claims = parseToken(token);
        return claims.getSubject().equals(userDetails.getUsername());
    }

    private SecretKey getSignInKey() {
        String SECRET = "a6oLf864kNouz6b1VCpJRnpYyTvAdT6JFVMfhhXyfRPvGhgkyy";
        byte[] keyBytes = Decoders.BASE64.decode(SECRET);
        return Keys.hmacShaKeyFor(keyBytes);
    }

}
