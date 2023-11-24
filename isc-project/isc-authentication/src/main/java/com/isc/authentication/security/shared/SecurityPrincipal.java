package com.isc.authentication.security.shared;

import com.isc.authentication.security.KeyUtils;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SecurityPrincipal implements Serializable {

    private KeyUtils keyUtils;

    public static final String AUTHORITIES_CLAIM = "authorities";

    public static final String REMEMBER_ME_CLAIM = "remember-me";

    public static final String USER_TYPE_CLAIM = "user-type";

    private String token;

    public String getUsername() {
        return parseJwt()
                .getBody()
                .getSubject();
    }

    public String getUserType() {
        return getJwtClaim(USER_TYPE_CLAIM, String.class);
    }

    private <T> T getJwtClaim(String claim, Class<T> claimType) {
        return parseJwt().getBody().get(claim, claimType);
    }

    private Jws<Claims> parseJwt() {
        return Jwts.parser().setSigningKey(keyUtils.getPublicKey()).parseClaimsJws(token);
    }

}
