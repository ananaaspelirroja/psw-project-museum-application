package it.project.security;


import io.micrometer.common.lang.NonNull;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;

import java.util.*;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toSet;

public class KeycloakTokenConverter implements Converter<Jwt, AbstractAuthenticationToken> {
    @Override
    public AbstractAuthenticationToken convert(@NonNull Jwt source) {
        return new JwtAuthenticationToken(
                source,
                Stream.concat(
                                new JwtGrantedAuthoritiesConverter().convert(source).stream(),
                                extractResourceRoles(source).stream())
                        .collect(toSet()));
    }

    private Collection<? extends GrantedAuthority> extractResourceRoles(Jwt jwt) {
        // Estrarre i ruoli da realm_access
        var realmAccess = (Map<String, Object>) jwt.getClaim("realm_access");

        // Controlla se ci sono ruoli sotto realm_access
        if (realmAccess != null && realmAccess.containsKey("roles")) {
            var roles = (List<String>) realmAccess.get("roles");

            // Logging per verificare i ruoli estratti
            System.out.println("Ruoli estratti da realm_access: " + roles);

            return roles.stream()
                    .map(role -> new SimpleGrantedAuthority("ROLE_" + role.replace("-", "_")))
                    .collect(toSet());
        }

        // Restituisce una collezione vuota se non ci sono ruoli
        return Collections.emptySet();
    }

}
