package online.dummydev.infrastructure.security;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class CurrentAppUserProvider {

    private AppUserPrincipal current() {

        var auth = SecurityContextHolder
                .getContext()
                .getAuthentication();

        if (auth == null
                || !auth.isAuthenticated()
                || !(auth.getDetails() instanceof AppUserPrincipal user)) {
            throw new IllegalStateException(
                    "Authenticated application user required"
            );
        }

        return user;
    }

    public UUID getTenantId() {
        return current().tenantId();
    }

    public UUID getAppUserId() {
        return current().appUserId();
    }

    public UUID getRoleId() {
        return current().roleId();
    }

    public String getRoleCode() {
        return current().roleCode();
    }
}