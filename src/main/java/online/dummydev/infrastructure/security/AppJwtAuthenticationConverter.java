package online.dummydev.infrastructure.security;

import lombok.RequiredArgsConstructor;
import online.dummydev.application.contracts.persistence.AuthenticatedUserLookup;

import org.springframework.core.convert.converter.Converter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class AppJwtAuthenticationConverter
        implements Converter<Jwt, AbstractAuthenticationToken> {

    private final AuthenticatedUserLookup userLookup;

    @Override
    public AbstractAuthenticationToken convert(Jwt jwt) {

        String clerkUserId = jwt.getSubject();
        String clerkOrgId = jwt.getClaimAsString("org_id");

        if (clerkUserId == null || clerkUserId.isBlank()
                || clerkOrgId == null || clerkOrgId.isBlank()) {
            throw new BadCredentialsException(
                    "User or organization is missing"
            );
        }

        AuthenticatedUserLookup.AuthenticatedUserData user;

        try {
            user = userLookup.resolve(clerkUserId, clerkOrgId);
        } catch (IllegalStateException ex) {
            throw new BadCredentialsException(
                    "Active application membership not found", ex
            );
        }

        AppUserPrincipal principal = new AppUserPrincipal(
                user.tenantId(),
                user.appUserId(),
                user.roleId(),
                user.roleCode(),
                clerkUserId,
                clerkOrgId
        );

        var authorities = List.of(
                new SimpleGrantedAuthority(
                        "ROLE_" + user.roleCode()
                )
        );

        JwtAuthenticationToken authentication =
                new JwtAuthenticationToken(
                        jwt,
                        authorities,
                        clerkUserId
                );

        authentication.setDetails(principal);

        return authentication;
    }
}