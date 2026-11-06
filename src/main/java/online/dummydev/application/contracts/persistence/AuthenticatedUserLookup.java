package online.dummydev.application.contracts.persistence;

import java.util.UUID;

public interface AuthenticatedUserLookup {

    AuthenticatedUserData resolve(
            String clerkUserId,
            String clerkOrgId
    );

    record AuthenticatedUserData(
            UUID tenantId,
            UUID appUserId,
            UUID roleId,
            String roleCode
    ) {}
}