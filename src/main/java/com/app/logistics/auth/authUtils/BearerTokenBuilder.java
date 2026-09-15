package com.app.logistics.auth.authUtils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Component
public class BearerTokenBuilder{
    private static final String SECRET_STRING = "AxleOS_Super_Secret_Key_For_JWT_Authentication_2026_Secure!";
    private final SecretKey secretKey = Keys.hmacShaKeyFor(SECRET_STRING.getBytes(StandardCharsets.UTF_8));

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

