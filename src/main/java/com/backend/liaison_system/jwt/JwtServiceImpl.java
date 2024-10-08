package com.backend.liaison_system.jwt;

import com.backend.liaison_system.user_details.LiaisonUserDetails;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import javax.crypto.SecretKey;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
@RequiredArgsConstructor
public class JwtServiceImpl {
    @Value("${auth-key}")
    private String SIGN_IN_KEY;

    public SecretKey getSignInKey() {
        byte[] bytes = Decoders.BASE64.decode(SIGN_IN_KEY);
        return Keys.hmacShaKeyFor(bytes);
    }

    private Claims extractAllClaimsFromToken(String token) {
        return Jwts
                .parser()
                .verifyWith(getSignInKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    private <T> T extractClaimsFromToken(String token, Function<Claims, T> resolver) {
        final Claims claims = extractAllClaimsFromToken(token);
        return resolver.apply(claims);
    }

    private String generateToken(Map<String, Object> claims, LiaisonUserDetails userDetails) {
        claims.put("firstname", userDetails.getFirstName());
        claims.put("lastname", userDetails.getLastName());
        claims.put("role", userDetails.getRole());

        try {
            return Jwts
                    .builder()
                    .claims(claims)
                    .id(userDetails.getId())
                    .subject(userDetails.getUsername())
                    .issuedAt(new Date(System.currentTimeMillis()))
                    .expiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 24)) // 24 hrs when you do not tick the remember me -  1 day
                    .signWith(getSignInKey())
                    .compact();
        } catch (JwtException jwtException) {
            throw new JwtException(jwtException.getMessage());
        }
    }

    public String generateToken(LiaisonUserDetails userDetails) {
        return generateToken(new HashMap<>(), userDetails);
    }

    public String extractUserEmail(String token) {
        return extractClaimsFromToken(token, Claims::getSubject);
    }
}
