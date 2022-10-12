package com.profitableaccountingsystemapi.util;

import com.profitableaccountingsystemapi.entity.UserModel;
import com.profitableaccountingsystemapi.exception.AccessDeniedException;
import com.profitableaccountingsystemapi.repo.UserRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Component
public class JwtUtils implements Serializable  {

    private static final long serialVersionUID = 234234523523L;

    @Value("${app.jwtSecret}")
    private String jwtSecret;

    @Value("${app.expiryDuration}")
    private int expiryDuration;

    @Value("${app.refreshTokenExpirationMs}")
    private int refreshTokenExpirationMs;

    @Autowired
    private UserRepository userRepository;

    public Claims verify(String authorization) throws Exception {
        try {
            Claims claims = Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(authorization).getBody();
            return claims;
        } catch (Exception e) {
            throw new AccessDeniedException("Access Denied...");
        }

    }

    public String generateRefreshToken(UserModel userModel) {
        long milliTime = System.currentTimeMillis();
        long expiryTime = milliTime + refreshTokenExpirationMs * 1000;
        Date expiryAtNew = new Date(expiryTime);

        Date issuedAt = new Date(milliTime);

        Claims claims = Jwts.claims()
                .setIssuer(userModel.getId().toString())
                .setIssuedAt(issuedAt)
                .setExpiration(expiryAtNew);

        claims.put("name", userModel.getName());

        return Jwts.builder()
                .setClaims(claims)
                .signWith(SignatureAlgorithm.HS256, jwtSecret)
                .compact();
    }

    //retrieve username from jwt token
    public String getUsernameFromToken(String token) {
        return getClaimFromToken(token, Claims::getSubject);
    }

    //retrieve expiration date from jwt token
    public Date getExpirationDateFromToken(String token) {
        return getClaimFromToken(token, Claims::getExpiration);
    }

    public <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = getAllClaimsFromToken(token);
        return claimsResolver.apply(claims);
    }
    //for retrieveing any information from token we will need the secret key
    private Claims getAllClaimsFromToken(String token) {
        return Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token).getBody();
    }

    //check if the token has expired
    public Boolean isTokenExpired(String token) {
        final Date expiration = getExpirationDateFromToken(token);
        return expiration.before(new Date());
    }

    //generate token for user
    public String generateToken(UserDetails userDetails) {
        Map<String, Object> claims = new HashMap<>();
        return doGenerateToken(claims, userDetails.getUsername());
    }

    private String doGenerateToken(Map<String, Object> claims, String subject) {
        UserModel existUser = userRepository.findOneByEmailId(subject);
        claims.put("name", existUser.getName());

        return Jwts.builder().setClaims(claims).setSubject(subject).setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + expiryDuration * 1000))
                .signWith(SignatureAlgorithm.HS512, jwtSecret).compact();
    }

    //validate token
    public Boolean validateToken(String token, UserDetails userDetails) {
        final String username = getUsernameFromToken(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }
}
