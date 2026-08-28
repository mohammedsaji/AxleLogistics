package com.app.logistics.auth.authUtils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;

@Component
public class BearerTokenBuilder{
    public SecretKey secretKey = Jwts.SIG.HS256.key().build();

    public String builtBearerToken(String username, String role){

        return Jwts.builder()
                .subject(username)
                .claim("Role",role)
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis()+1000*60*60))
                .signWith(secretKey)
                .compact();
    }

    public Object[] parseBearerToken(String bearerToken){
        Claims claims =  Jwts.parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims((CharSequence)bearerToken)
                .getPayload();

        String username = claims.getSubject();
        String role = claims.get("Role",String.class);
        Date expirationTime = claims.getExpiration();

        return new Object[]{username,role,expirationTime};
    }
}

