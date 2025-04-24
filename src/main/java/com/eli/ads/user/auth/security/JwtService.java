package com.eli.ads.user.auth.security;


import com.eli.ads.user.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.security.Key;
import java.util.Date;

@Service
@RequiredArgsConstructor
public class JwtService {

    @Value("${application.security.jwt}")
    private  String SECRET_KEY;

    @Value("${application.security.expiration}")
    private  Long TOKEN_EXPIRATION;

    public String generateToken(User user) {
        return Jwts.builder()
                .subject(user.getUsername())
                .expiration(new Date( System.currentTimeMillis() + TOKEN_EXPIRATION))
                .issuedAt(new Date(System.currentTimeMillis()))
                .claim("authorities", user.getAuthorities())
                .signWith(getSigningKey())
                .compact();
    }

    private Key getSigningKey() {
        return Keys.hmacShaKeyFor(Decoders.BASE64.decode(SECRET_KEY));
    }

    public Claims getClaimsFrom(String jwtToken) {
        return Jwts
                .parser()
                .verifyWith((SecretKey) getSigningKey())
                .build()
                .parseSignedClaims(jwtToken)
                .getPayload() ;
    }

    public boolean isValidToken(String token, UserDetails userDetails){
        return getClaimsFrom(token).getSubject().equals(userDetails.getUsername()) && ! isTokenExpired(token);
    }

    private boolean isTokenExpired(String token){
        return  getClaimsFrom(token).getExpiration().before(new Date());
    }
}
