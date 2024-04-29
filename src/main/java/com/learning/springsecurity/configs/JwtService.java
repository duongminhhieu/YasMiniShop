package com.learning.springsecurity.configs;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.function.Function;

@Service
public class JwtService {

    private static final String SECRET_KEY = "39c658a258a42e6ea83c38b59ed9f78076f2d3cf75ec8eb3d80e3aa433370e4a";

    // extract user email from jwt token
    public String extractUserEmail(String jwtToken) {
        return extractClaim(jwtToken, Claims::getSubject);
    }


    public <T> T extractClaim(String jwtToken, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(jwtToken);
        return claimsResolver.apply(claims);
    }

    /**
     * This method is used to extract all claims from a given JWT token.
     * It uses the JWT parser builder to parse the token and retrieve the body of the token which contains the claims.
     * The signing key used for parsing is retrieved from the getSignInKey() method.
     *
     * @param jwtToken The JWT token from which the claims are to be extracted.
     * @return The claims present in the JWT token.
     */
    private Claims extractAllClaims(String jwtToken) {

        return Jwts.parserBuilder()
                .setSigningKey(getSignInKey())
                .build()
                .parseClaimsJwt(jwtToken)
                .getBody();
    }

    private Key getSignInKey() {
        byte[] keyBytes = Decoders.BASE64.decode(SECRET_KEY);
        return Keys.hmacShaKeyFor(keyBytes);
    }

}
