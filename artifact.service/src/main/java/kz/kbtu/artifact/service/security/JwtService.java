package kz.kbtu.artifact.service.security;


import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class JwtService {
    @Value("${jwt.service}")
    private String secret;

    public boolean isTokenValid(String token){
        try{
            parseClaims(token);
            return true;
        } catch(JwtException e){
            return false;
        }

    }
    public String extractUsername(String token){
        return parseClaims(token).getSubject();
    }
    @SuppressWarnings("unchecked")
    public List<String> extractRoles(String token){
        return (List<String>) parseClaims(token).get("roles");
    }

    private Claims parseClaims(String token){
        return Jwts.parser()
                .verifyWith(Keys.hmacShaKeyFor(secret.getBytes()))
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }
}
