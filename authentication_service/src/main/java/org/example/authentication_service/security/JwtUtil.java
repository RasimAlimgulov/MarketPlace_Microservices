package org.example.authentication_service.security;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
public class JwtUtil {
private String secret="EPy_mM65kbYCRHDVtsMkxReKnkPJouUnxFl2R2Cx2d-qKgIg-bdNa-Pv_X5jMdOb1T7hq05Xb3Iz_DmdnMowTg==";
private Key key= Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));

    public String generateToken(UserDetails userDetails) {
        Map<String, Object> claims = new HashMap<>();
        return createToken(claims, userDetails.getUsername());
    }

    private String createToken(Map<String, Object> claims, String subject) {
        return Jwts.builder().setClaims(claims).setSubject(subject).setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 10))
                .signWith(key, SignatureAlgorithm.HS512).compact();
    }

    public String generateRefreshToken(String username){
        Map<String ,Object> claims=new HashMap<>();
        return Jwts.builder().setClaims(claims).setSubject(username).setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 7 * 24 * 60 * 60 * 1000))
                .signWith(key, SignatureAlgorithm.HS512).compact();
    }

    public String extractUsername(String token){
        return extractClaim(token,Claims::getSubject);
    }

    private <T>T extractClaim(String token,Function<Claims, T> claimsResolver){
    final Claims claims=extractAllClaims(token);
    return claimsResolver.apply(claims);
    }

    private Claims extractAllClaims(String token) {
    return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody();
    }

    public boolean isTokenExpired(String token){
        return extractExpiration(token).before(new Date());
    }

    public Date extractExpiration(String token){
        return extractClaim(token,Claims::getExpiration);
    }


}