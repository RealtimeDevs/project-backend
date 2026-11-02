
package online.dummydev.persistence.config;

import online.dummydev.infrastructure.security.CurrentAppUserProvider;
import org.hibernate.context.spi.CurrentTenantIdentifierResolver;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class HibernateTenantResolver
        implements CurrentTenantIdentifierResolver<UUID> {

    private final CurrentAppUserProvider currentUser;

    public HibernateTenantResolver(
            CurrentAppUserProvider currentUser) {
        this.currentUser = currentUser;
    }

    @Override
    public UUID resolveCurrentTenantIdentifier() {
        return currentUser.getTenantId();
    }

    @Override
    public boolean validateExistingCurrentSessions() {
        return true;
    }
}
