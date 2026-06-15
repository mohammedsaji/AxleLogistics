package com.app.projectlogistics.UtilityFiles;

import com.app.projectlogistics.ValueObject.AccountVO;
import io.jsonwebtoken.*;
import java.nio.charset.StandardCharsets;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
public class JwTokenUtilities {

    private static final String secretSigningKey = "Project-Logistic-Analysis-Secret-Signing-Key";

    private byte[] getSigningKeyBytes() {
        return secretSigningKey.getBytes(StandardCharsets.UTF_8);
    }

    public String generateJwToken(AccountVO accountVO){

        Map<String,Object> claims = new HashMap<>();
        String role = accountVO.getAccountRole();
        claims.put("ROLE",role);

        JwtBuilder jwtBuilder = Jwts.builder();
        jwtBuilder.setClaims(claims);
        jwtBuilder.setSubject(accountVO.getAccountUsername());
        jwtBuilder.setIssuedAt(new Date());
        jwtBuilder.setExpiration(new Date(System.currentTimeMillis() + (1000*60*60)));
        jwtBuilder.signWith(SignatureAlgorithm.HS256,getSigningKeyBytes());
        return jwtBuilder.compact();
    }

    public Boolean verifyJWToken(String employeeName, String jwToken){

        if(jwToken== null || jwToken.trim().isEmpty() || employeeName == null ||  employeeName.trim().isEmpty()){
            System.out.println(" JWT Validation rejected : username or token missing..!");
            return false;
        }
        try{
            JwtParser jwtParser = Jwts.parser();
            jwtParser.setSigningKey(getSigningKeyBytes());
            Claims claims = jwtParser.parseClaimsJws(jwToken).getBody();
            String extractedNamefromJwt = claims.getSubject();
            Date extractedExpirationDate = claims.getExpiration();

            if(extractedNamefromJwt != null
                    && !extractedNamefromJwt.equalsIgnoreCase("")
                    && employeeName.equalsIgnoreCase(extractedNamefromJwt)
                    && extractedExpirationDate.after(new Date())){
                return true;
            }
        }catch(Exception jwt){
            System.out.println("JWT Validation exception : "+jwt.getLocalizedMessage());
            jwt.printStackTrace();
        }
        return false;
    }

    public String getRoleFromToken(String jwToken) {
        try {
            JwtParser jwtParser = Jwts.parser();
            jwtParser.setSigningKey(getSigningKeyBytes());
            Claims claims = jwtParser.parseClaimsJws(jwToken).getBody();
            return claims.get("ROLE", String.class);
        } catch (Exception e) {
            return "";
        }
    }
}
