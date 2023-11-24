package com.isc.authentication.security.shared;

import org.springframework.security.access.PermissionEvaluator;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.io.Serializable;

@Component
public class GestionalePermissionEvaluator implements PermissionEvaluator {

    private static final String AUTHORITY_SEPARATOR = "\\.";

    @Override
    public boolean hasPermission(Authentication auth, Object targetDomainObject, Object permission) {
        return hasPermission(auth, Long.parseLong(targetDomainObject.toString()), permission.toString());
    }

    @Override
    public boolean hasPermission(Authentication auth, Serializable targetId, String targetType, Object permission) {
        return hasPermission(auth, Long.parseLong(targetId.toString()), targetType);
    }

    public boolean isAdmin(Authentication auth) {
        return auth.getPrincipal() instanceof SecurityPrincipal && ((SecurityPrincipal) auth.getPrincipal()).getUserType().equals("ADMIN");
    }

}

