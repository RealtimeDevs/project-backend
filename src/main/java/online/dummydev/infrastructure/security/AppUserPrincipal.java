package online.dummydev.infrastructure.security;

import java.util.UUID;

public record AppUserPrincipal(
        UUID tenantId,
        UUID appUserId,
        UUID roleId,
        String roleCode,
        String clerkUserId,
        String clerkOrgId
) {}