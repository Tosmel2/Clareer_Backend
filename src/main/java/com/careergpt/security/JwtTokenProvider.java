package com.careergpt.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;
import com.careergpt.config.JwtConfig;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;
import java.security.Key;
import java.util.Base64;

@Component
public class JwtTokenProvider {

    private final JwtConfig jwtConfig;
    private final Key key;

    @Autowired
    public JwtTokenProvider(JwtConfig jwtConfig) {
        this.jwtConfig = jwtConfig;
        // Decode the base64 secret and create a Key instance
        byte[] decodedKey = Base64.getDecoder().decode(jwtConfig.getSecret());
        this.key = Keys.hmacShaKeyFor(decodedKey);
    }

    public String generateToken(String email) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + jwtConfig.getExpiration());

        return Jwts.builder()
                .setSubject(email)
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .signWith(key, SignatureAlgorithm.HS512)
                .compact();
    }

    public String getEmailFromJWT(String token) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
        return claims.getSubject();
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            return true;
        } catch (JwtException | IllegalArgumentException ex) {
            return false;
        }
    }
}







// package com.careergpt.security;

// import io.jsonwebtoken.*;
// import org.springframework.stereotype.Component;
// import com.careergpt.config.JwtConfig;
// import org.springframework.beans.factory.annotation.Autowired;

// import java.util.Date;

// @Component
// public class JwtTokenProvider {

//     private final JwtConfig jwtConfig;

//     @Autowired
//     public JwtTokenProvider(JwtConfig jwtConfig) {
//         this.jwtConfig = jwtConfig;
//     }

//     public String generateToken(String email) {
//         Date now = new Date();
//         Date expiryDate = new Date(now.getTime() + jwtConfig.getExpiration());

//         return Jwts.builder()
//                 .setSubject(email)
//                 .setIssuedAt(now)
//                 .setExpiration(expiryDate)
//                 .signWith(SignatureAlgorithm.HS512, jwtConfig.getSecret())
//                 .compact();
//     }

//     public String getEmailFromJWT(String token) {
//         Claims claims = Jwts.parser()
//                 .setSigningKey(jwtConfig.getSecret())
//                 .parseClaimsJws(token)
//                 .getBody();
//         return claims.getSubject();
//     }

//     public boolean validateToken(String token) {
//         try {
//             Jwts.parser().setSigningKey(jwtConfig.getSecret()).parseClaimsJws(token);
//             return true;
//         } catch (JwtException | IllegalArgumentException ex) {
//             return false;
//         }
//     }
// }