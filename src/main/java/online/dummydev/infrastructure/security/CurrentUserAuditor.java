package online.dummydev.infrastructure.security;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.AuditorAware;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.UUID;

@Component("auditorProvider")
@RequiredArgsConstructor
public class CurrentUserAuditor implements AuditorAware<UUID> {

    private final CurrentAppUserProvider currentUser;

    @Override
    public Optional<UUID> getCurrentAuditor() {
        try {
            return Optional.of(currentUser.getAppUserId());
        } catch (IllegalStateException ex) {
            return Optional.empty();
        }
    }
}