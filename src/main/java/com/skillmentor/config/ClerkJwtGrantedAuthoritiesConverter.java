package com.skillmentor.config;

import org.springframework.core.convert.converter.Converter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * Extracts role from Clerk JWT's public_metadata.role field.
 * If role == "admin", grants ROLE_ADMIN authority.
 */
public class ClerkJwtGrantedAuthoritiesConverter implements Converter<Jwt, Collection<GrantedAuthority>> {

    @Override
    public Collection<GrantedAuthority> convert(Jwt jwt) {
        List<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority("ROLE_USER"));

        // Check public_metadata.role from Clerk JWT
        Map<String, Object> publicMetadata = jwt.getClaimAsMap("public_metadata");
        if (publicMetadata != null) {
            Object role = publicMetadata.get("role");
            if ("admin".equalsIgnoreCase(String.valueOf(role))) {
                authorities.add(new SimpleGrantedAuthority("ROLE_ADMIN"));
            }
        }

        // Also check metadata claim (alternative Clerk JWT template format)
        Map<String, Object> metadata = jwt.getClaimAsMap("metadata");
        if (metadata != null) {
            Object role = metadata.get("role");
            if ("admin".equalsIgnoreCase(String.valueOf(role))) {
                authorities.add(new SimpleGrantedAuthority("ROLE_ADMIN"));
            }
        }

        return authorities;
    }
}
