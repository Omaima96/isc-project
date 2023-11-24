package com.isc.authentication.security;


import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import static com.isc.authentication.security.shared.SecurityPrincipal.AUTHORITIES_CLAIM;
import static com.isc.authentication.security.shared.SecurityPrincipal.REMEMBER_ME_CLAIM;


@Component
@PropertySource("classpath:/application.yml")
public class JwtTokenUtil implements Serializable {

    @Autowired
    private KeyUtils keyUtils;

    @Value("${jwt.lifetime.default}")
    private Long tokenLifetime;

    @Value("${jwt.lifetime.remember-me}")
    private Long tokenLifetimeRM;

    public static final String ISSUER = "isc-auth-service";

    public String generateJwt(UserDetails user, Boolean rememberMe) {
        return Jwts.builder()
                .setId(UUID.randomUUID().toString())
                .setIssuer(ISSUER)
                .setSubject(user.getUsername())
                .claim(AUTHORITIES_CLAIM, user.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList()))
                .claim(REMEMBER_ME_CLAIM, rememberMe)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + (rememberMe ? tokenLifetimeRM : tokenLifetime)))
                .signWith(SignatureAlgorithm.RS256, keyUtils.getKeyPair().getPrivate())
                .compact();
    }

    public Boolean isTokenValid(String token) {
        try {
            final Date expiration = getJwtExpiration(token);
            return expiration.after(new Date());
        } catch (Exception e) {
            return Boolean.FALSE;
        }
    }

    public String getJwtSubject(String token) {
        return parseJwt(token)
                .getBody()
                .getSubject();
    }

    public Date getJwtExpiration(String token) {
        return parseJwt(token)
                .getBody()
                .getExpiration();
    }

    public List<GrantedAuthority> getJwtAuthorities(String token) {
        return AuthorityUtils.commaSeparatedStringToAuthorityList(
                StringUtils.arrayToCommaDelimitedString(
                        getJwtClaim(token, AUTHORITIES_CLAIM, List.class).toArray()
                )
        );
    }

    public Boolean getJwtRememberMe(String token) {
        return getJwtClaim(token, REMEMBER_ME_CLAIM, Boolean.class);
    }

    public <T> T getJwtClaim(String token, String claim, Class<T> claimType) {
        return parseJwt(token).getBody().get(claim, claimType);
    }

    private Jws<Claims> parseJwt(String token) {
        return Jwts.parser().setSigningKey(keyUtils.getKeyPair().getPublic()).parseClaimsJws(token);
    }

    public long getValidity() {
        return tokenLifetime;
    }
}
