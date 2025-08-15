package com.example.trustcare.security;

import com.example.trustcare.enums.Role;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class JWTUtility {
    @Value("${jwt.secret}")
    private String secret;
    public String generateToken(UserDetails userDetails) {
        return Jwts.builder().setSubject(userDetails.getUsername()).claim("role", userDetails.getAuthorities().iterator().next().getAuthority()).setIssuedAt(new Date()).setExpiration(new Date(System.currentTimeMillis() +1000*60)).signWith(SignatureAlgorithm.ES256,secret).compact();
    }
    public String extractUserName(String token){
        return  Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody().getSubject();
    }
        public String extractRole(String token) {
        return (String) Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody().get("role");
    }

    public boolean validateToken(String token, UserDetails userDetails) {
        return extractUserName(token).equals(userDetails.getUsername());
    }

}